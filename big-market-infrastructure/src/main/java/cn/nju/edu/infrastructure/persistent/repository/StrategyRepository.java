package cn.nju.edu.infrastructure.persistent.repository;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.infrastructure.persistent.dao.IStrategyAwardDao;
import cn.nju.edu.infrastructure.persistent.dao.IStrategyDao;
import cn.nju.edu.infrastructure.persistent.po.StrategyAward;
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
public class StrategyRepository implements IStrategyRepository {


    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private RedissonService redissonService;




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
    public void storeLotteryStrategyAwards2(Long strategyId, List<BigDecimal> scaledAwardRates, List<Integer> alias, List<Integer> awards) {
        String scaledRatesCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_SCALEDRATE_KEY + strategyId;
        String awardsCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_AWARDS_KEY + strategyId;
        String aliasCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_ALIAS_KEY + strategyId;
        redissonService.setValue(scaledRatesCacheKey, scaledAwardRates);
        redissonService.setValue(awardsCacheKey, awards);
        redissonService.setValue(aliasCacheKey, alias);
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

    @Override
    public List<BigDecimal> getScaledAwardRates(Long strategyId) {
        String scaledRatesCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_SCALEDRATE_KEY + strategyId;
        return redissonService.getValue(scaledRatesCacheKey);

    }

    @Override
    public List<Integer> getLotteryAliasList(Long strategyId) {
        String aliasCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_ALIAS_KEY + strategyId;
        return redissonService.getValue(aliasCacheKey);

    }

    @Override
    public List<Integer> getLotteryAwardsList(Long strategyId) {
        String awardsCacheKey = Constants.RedisKey.STRATEGY_LOTTERY_AWARDS_KEY + strategyId;
        return redissonService.getValue(awardsCacheKey);
    }
}
