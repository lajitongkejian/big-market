package cn.nju.edu.infrastructure.persistent.repository;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyRuleEntity;
import cn.nju.edu.domain.strategy.model.vo.*;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.infrastructure.persistent.dao.*;
import cn.nju.edu.infrastructure.persistent.po.*;
import cn.nju.edu.infrastructure.persistent.redis.RedissonService;
import cn.nju.edu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称：big-market
 * 类名称：StrategyRepository
 * 作者：tkj
 * 日期：2024/11/9
 * 描述：策略仓储实现类,负责内存与硬盘存储判断、po转dto操作
 */
@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository{


    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private RedissonService redissonService;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyRuleDao strategyRuleDao;

    @Resource
    private IRuleTreeNodeDao ruleTreeNodeDao;

    @Resource
    private IRuleTreeDao ruleTreeDao;

    @Resource
    private IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Resource
    private IRaffleActivityDao raffleActivityDao;

    @Resource
    private IRaffleActivityAccountDayDao raffleActivityAccountDayDao;


    /**
     * 根据策略id查询全部奖品信息
     * @param strategyId
     * @return
     */
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        //拼接key，一个抽奖页面对应一个奖品列表，先从redis中获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY + strategyId;
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
                          .awardSubtitle(strategyAward.getAwardSubtitle())
                          .sort(strategyAward.getSort())
                          .awardTitle(strategyAward.getAwardTitle())
                          .ruleModels(strategyAward.getRuleModels())
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
        String awardsCacheKey2 = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY+ key;
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

    //构建规则树的map映射，并封装为RuleTreeVO
    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        //优先从缓存中拿数据
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO ruleTreeVOCache = redissonService.getValue(cacheKey);
        if(null != ruleTreeVOCache) return ruleTreeVOCache;



        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);
        Map<String,List<RuleTreeNodeLineVO>> map = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .build();
            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOS = map.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOS.add(ruleTreeNodeLineVO);
        }
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        Map<String,RuleTreeNodeVO> ruleTreeNodeVOMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(map.get(ruleTreeNode.getRuleKey()))
                    .build();
            ruleTreeNodeVOMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeVO);
        }
        RuleTreeVO ruleTreeVO = RuleTreeVO.builder()
                .treeDesc(ruleTree.getTreeDesc())
                .treeId(ruleTree.getTreeId())
                .treeRootRuleNode(ruleTree.getTreeRootRuleKey())
                .treeNodeMap(ruleTreeNodeVOMap)
                .treeName(ruleTree.getTreeName())
                .build();
        redissonService.setValue(cacheKey, ruleTreeVO);
        return ruleTreeVO;

    }

    @Override
    public void cacheStrategyAwardCount(String key, Integer awardCount) {
        if(redissonService.isExists(key)) return;
        redissonService.setAtomicLong(key,awardCount);

    }

    @Override
    public Boolean subtractionAwardStock(String key) {
        return subtractionAwardStock(key,null);

    }

    @Override
    public Boolean subtractionAwardStock(String key, Date endDateTime) {
        long surplus = redissonService.decr(key);
        if(surplus < 0){
            redissonService.setValue(key,0);
            return false;
        }
        String lockKey = key+"_"+surplus;
        Boolean lock;
        if(null != endDateTime){
            long expireMillis = endDateTime.getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
            lock = redissonService.setNx(lockKey,expireMillis,TimeUnit.MILLISECONDS);
        }else{
            lock = redissonService.setNx(lockKey);
        }
        if(!lock){
            log.info("策略奖品库存加锁失败",lockKey);
        }
        return lock;
    }

    @Override
    public void awardStockConsumeSendQueue(StrategyAwardStockKeyVO build) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
        //创建阻塞消息队列信息
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redissonService.getBlockingQueue(cacheKey);
        //根据队列信息创建延迟队列
        RDelayedQueue<StrategyAwardStockKeyVO> delayedQueue = redissonService.getDelayedQueue(blockingQueue);
        //将库存扣减对象加入到延迟队列
        delayedQueue.offer(build,3, TimeUnit.SECONDS);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
        RBlockingQueue<StrategyAwardStockKeyVO> blockingQueue = redissonService.getBlockingQueue(cacheKey);
        return blockingQueue.poll();

    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = StrategyAward.builder()
                .strategyId(strategyId)
                .awardId(awardId)
                .build();
        strategyAwardDao.updateStrategyAwardStock(strategyAward);
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY +strategyId+"_"+awardId;
        StrategyAwardEntity strategyAwardEntity = redissonService.getValue(cacheKey);
        if(null!=strategyAwardEntity){ return strategyAwardEntity; }
        StrategyAward strategyAward = StrategyAward.builder()
                .strategyId(strategyId)
                .awardId(awardId)
                .build();
        StrategyAward strategyAwardRes = strategyAwardDao.queryStrategyAward(strategyAward);
        strategyAwardEntity = StrategyAwardEntity.builder()
                  .strategyId(strategyAwardRes.getStrategyId())
                  .awardId(strategyAwardRes.getAwardId())
                  .awardCount(strategyAwardRes.getAwardCount())
                  .awardCountSurplus(strategyAwardRes.getAwardCountSurplus())
                  .awardRate(strategyAwardRes.getAwardRate())
                  .sort(strategyAwardRes.getSort())
                  .awardTitle(strategyAwardRes.getAwardTitle())
                  .awardSubtitle(strategyAwardRes.getAwardSubtitle())
                  .build();
        redissonService.setValue(cacheKey, strategyAwardEntity);
        return strategyAwardEntity;
    }

    @Override
    public Long queryStrategyIdByActivityId(Long activityId) {
        return raffleActivityDao.queryStrategyIdByActivityId(activityId);
    }

    @Override
    public Integer queryTodayUserRaffleCount(Long strategyId, String userId) {
        Long activityId = raffleActivityDao.queryActivityIdByStrategyId(strategyId);
        RaffleActivityAccountDay raffleActivityAccountDay = new RaffleActivityAccountDay();
        raffleActivityAccountDay.setActivityId(activityId);
        raffleActivityAccountDay.setUserId(userId);
        raffleActivityAccountDay.setActivityId(activityId);
        raffleActivityAccountDay.setDay(raffleActivityAccountDay.currentDay());

        RaffleActivityAccountDay response = raffleActivityAccountDayDao.queryActivityAccountDayByUserId(raffleActivityAccountDay);
        if(null == response) return 0;
        return response.getDayCount() - response.getDayCountSurplus();
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        if(null == treeIds || treeIds.length == 0) return new HashMap<>();
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleLocks(treeIds);
        Map<String, Integer> map = new HashMap<>();
        for(RuleTreeNode ruleTreeNode : ruleTreeNodes){
            map.put(ruleTreeNode.getTreeId(),Integer.valueOf(ruleTreeNode.getRuleValue()));
        }
        return map;
    }
}
