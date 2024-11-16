package cn.nju.edu.infrastructure.persistent.repository;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyRuleEntity;
import cn.nju.edu.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.nju.edu.infrastructure.persistent.dao.IStrategyDao;
import cn.nju.edu.infrastructure.persistent.dao.IStrategyRuleDao;
import cn.nju.edu.infrastructure.persistent.po.Strategy;
import cn.nju.edu.infrastructure.persistent.po.StrategyAward;
import cn.nju.edu.infrastructure.persistent.po.StrategyRule;
import cn.nju.edu.infrastructure.persistent.redis.RedissonService;
import cn.nju.edu.types.common.Constants;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 项目名称：big-market
 * 类名称：StrategyRepository
 * 作者：tkj
 * 日期：2024/11/9
 * 描述：策略仓储实现类,负责内存与硬盘存储判断、po转dto操作
 */
@Repository
public class StrategyRepository implements IStrategyRepository{


    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private RedissonService redissonService;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;




    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //拼接key，一个抽奖页面对应一个奖品列表，先从redis中获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities =  redissonService.getValue(cacheKey);
        if(strategyAwardEntities != null && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }
        //如果redis中不存在，就去数据库中取
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for(StrategyAward strategyAward : strategyAwards) {
             StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                          .strategyId(strategyAward.getStrategyId())
                          .awardId(strategyAward.getAwardId())
                          .awardCount(strategyAward.getAwardCount())
                          .awardCountSurplus(strategyAward.getAwardCountSurplus())
                          .awardRate(strategyAward.getAwardRate())
                          .build();
             strategyAwardEntities.add(strategyAwardEntity);
        }
        //将数据库读取到的再存入缓存中
        redissonService.setValue(cacheKey, strategyAwardEntities);
        return strategyAwardEntities;
    }



    @Override
    public void storeLotteryStrategyAwards(Long strategyId, HashMap<Integer, Integer> map, Integer range) {
        String rateRangeCacheKey = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId;
        String  strategyRateCacheKey = Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId;
        redissonService.setValue(rateRangeCacheKey, range.intValue());
        Map<Integer,Integer> cachemap =  redissonService.getMap(strategyRateCacheKey);
        cachemap.putAll(map);
    }

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(String key) {
        return redissonService.getValue(Constants.RedisKey.STRATEGY_AWARD_KEY + key);
    }

    @Override
    public void storeLotteryStrategyAwards2(String key, List<BigDecimal> scaledAwardRates, List<Integer> alias, List<Integer> awards,List<StrategyAwardEntity> strategyAwardEntities) {
        String scaledRatesCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_SCALEDRATE_KEY + key;
        String awardsCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_AWARDS_KEY + key;
        String aliasCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_ALIAS_KEY + key;
        String awardsCacheKey2 = Constants.RedisKey.STRATEGY_AWARD_KEY+ key;
        redissonService.setValue(scaledRatesCacheKey, scaledAwardRates);
        redissonService.setValue(awardsCacheKey, awards);
        redissonService.setValue(aliasCacheKey, alias);
        redissonService.setValue(awardsCacheKey2, strategyAwardEntities);
    }

    @Override
    public Integer getLotteryStrategyAwards(Long strategyId,Integer rateKey) {
        String strategyRateCacheKey = Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId;
        return redissonService.getFromMap(strategyRateCacheKey,rateKey);
    }

    @Override
    public Integer getRateRange(Long strategyId) {
        String rateRangeCacheKey = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId;
        return redissonService.getValue(rateRangeCacheKey);
    }

    //下面这三个有用
    @Override
    public List<BigDecimal> getScaledAwardRates(String key) {
        String scaledRatesCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_SCALEDRATE_KEY + key;
        return redissonService.getValue(scaledRatesCacheKey);

    }

    @Override
    public List<Integer> getLotteryAliasList(String key) {
        String aliasCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_ALIAS_KEY + key;
        return redissonService.getValue(aliasCacheKey);

    }

    @Override
    public List<Integer> getLotteryAwardsList(String key) {
        String awardsCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_AWARDS_KEY + key;
        return redissonService.getValue(awardsCacheKey);
    }

    @Override
    public StrategyEntity queryStrategyByStrategyId(Long strategyId) {
        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        StrategyEntity strategyEntity = StrategyEntity.builder()
                  .ruleModels(strategy.getRuleModels())
                  .strategyId(strategy.getStrategyId())
                  .strategyDesc(strategy.getStrategyDesc())
                  .build();
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId.toString();
        redissonService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = StrategyRule.builder()
                .strategyId(strategyId)
                .ruleModel(ruleModel)
                .build();
        StrategyRule strategyRule = strategyRuleDao.queryStrategyRule(strategyRuleReq);
        StrategyRuleEntity strategyRuleEntity = StrategyRuleEntity.builder()
                  .strategyId(strategyRule.getStrategyId())
                  .awardId(strategyRule.getAwardId())
                  .ruleType(strategyRule.getRuleType())
                  .ruleModel(strategyRule.getRuleModel())
                  .ruleValue(strategyRule.getRuleValue())
                  .ruleDesc(strategyRule.getRuleDesc())
                  .build();
        return strategyRuleEntity;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        return strategyRuleDao.queryStrategyRuleValue(StrategyRule.builder()
                        .strategyId(strategyId)
                        .ruleModel(ruleModel)
                        .awardId(awardId)
                        .build());

    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Integer awardId, Long strategyId) {
        String ruleModels =  strategyAwardDao.queryStrategyAwardRule(StrategyRule.builder()
                .awardId(awardId)
                .strategyId(strategyId)
                .build());
        return StrategyAwardRuleModelVO.builder().ruleModels(ruleModels).build();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return queryStrategyRuleValue(strategyId, null, ruleModel);
    }
}
