package cn.nju.edu.domain.strategy.service.armory;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * 项目名称：big-market
 * 类名称：StrategyArmory
 * 作者：tkj
 * 日期：2024/11/9
 * 描述：
 */
@Service
@Slf4j
public class StrategyArmory implements IStrategyArmory {

    @Resource
    private IStrategyRepository strategyRepository;


    @Override
    public void assembleLotteryStrategy(Long strategyId) {
        //1.查询抽奖策略的奖品列表
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);
        //2.查找奖品概率最小值
        BigDecimal minRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        //3.概率量化总和，一般值为1
        BigDecimal totalRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //4.乘以放大倍率,相当于总份数,向上取整
        BigDecimal range = totalRate.divide(minRate, 0, RoundingMode.CEILING);
        //5.将每种奖品按照份数填充进List
        ArrayList<Integer> list = new ArrayList<>(range.intValue());
        for(StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardRate = strategyAwardEntity.getAwardRate();
            for (int i = 0; i < awardRate.multiply(range).setScale(0,RoundingMode.CEILING).intValue(); i++) {
                list.add(awardId);
            }
        }
        //6.将列表内奖品乱序,其实list就是为了乱序才建立的，最终有用的也只是哈希表
        Collections.shuffle(list);
        //7.根据这个列表，将每种奖品放入哈希表中，可以直接取出
        HashMap<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(i,list.get(i));
        }
        //8.存储到Redis中
        strategyRepository.storeLotteryStrategyAwards(strategyId,map,list.size());

    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        Integer rateRange = strategyRepository.getRateRange(strategyId);
        return strategyRepository.getLotteryStrategyAwards(strategyId, new SecureRandom().nextInt(rateRange));

    }

    @Override
    public void assembleLotteryStrategy2(Long strategyId){
        //alias算法重构
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);
        int size = strategyAwardEntities.size();
        BigDecimal totalProb = BigDecimal.ZERO;
        //2.初始化概率放缩后的列表、大于1的large以及小于1的small奖品表
        List<BigDecimal> scaledAwardRates = new ArrayList<>();
        List<Integer> large = new ArrayList<>();
        List<Integer> small = new ArrayList<>();
        List<Integer> alias = new ArrayList<>(Collections.nCopies(size, -1));
        List<Integer> awards = new ArrayList<>(Collections.nCopies(size, -1));
        int i = 0;
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            BigDecimal scaledAwardRate = strategyAwardEntity.getAwardRate().multiply(BigDecimal.valueOf(size));
            scaledAwardRates.add(scaledAwardRate);
            totalProb = totalProb.add(scaledAwardRate);
            if(scaledAwardRate.compareTo(BigDecimal.ONE) < 0){
                small.add(i++);
            }else{
                large.add(i++);
            }
        }
        //3.初始化备用礼品表
        while(!small.isEmpty() && !large.isEmpty()){
            int smallIdx = small.remove(small.size() - 1);
            int largeIdx = large.remove(large.size() - 1);
            awards.set(smallIdx, smallIdx);
            alias.set(smallIdx, largeIdx);
            scaledAwardRates.set(largeIdx,scaledAwardRates.get(smallIdx).add(scaledAwardRates.get(largeIdx)).subtract(BigDecimal.ONE));
            if(scaledAwardRates.get(largeIdx).compareTo(BigDecimal.ONE) < 0){
                small.add(largeIdx);
            }else{
                large.add(largeIdx);
            }
        }
        //4.清空剩余的large、small奖品表,剩余的奖品概率都是为1的
        while(!large.isEmpty()){
            int largeIdx = large.remove(large.size() - 1);
            awards.set(largeIdx, largeIdx);
        }
        while(!small.isEmpty()){
            int smallIdx = small.remove(small.size() - 1);
            awards.set(smallIdx, smallIdx);
        }
        strategyRepository.storeLotteryStrategyAwards2(strategyId,scaledAwardRates,alias,awards);

    }
    //抽奖函数
    @Override
    public Integer getRandomAwardId2(Long strategyId) {
        SecureRandom secureRandom = new SecureRandom();
        List<BigDecimal> scaledAwardRates = strategyRepository.getScaledAwardRates(strategyId);
        List<Integer> alias = strategyRepository.getLotteryAliasList(strategyId);
        List<Integer> awards = strategyRepository.getLotteryAwardsList(strategyId);
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);
        int index = secureRandom.nextInt(strategyAwardEntities.size());

        BigDecimal rate = BigDecimal.valueOf(secureRandom.nextDouble());
        if(rate.compareTo(scaledAwardRates.get(index)) < 0){
            return strategyAwardEntities.get(awards.get(index)).getAwardId();
        }else{
            return alias.get(index) == -1 ? strategyAwardEntities.get(awards.get(index)).getAwardId() :
                    strategyAwardEntities.get(alias.get(index)).getAwardId();
        }
    }
}
