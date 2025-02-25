package cn.nju.edu.domain.strategy.repository;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyRuleEntity;
import cn.nju.edu.domain.strategy.model.vo.RuleTreeVO;
import cn.nju.edu.domain.strategy.model.vo.RuleWeightVO;
import cn.nju.edu.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.nju.edu.domain.strategy.model.vo.StrategyAwardStockKeyVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：big-market
 * 类名称：IStrategyRepository
 * 作者：tkj
 * 日期：2024/11/9
 * 描述：策略仓储接口
 */
public interface IStrategyRepository {

    //查询无规则过滤奖品列表
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);
    //查询有规则过滤奖品列表
    List<StrategyAwardEntity> queryStrategyAwardList(String key);

    void storeLotteryStrategyAwards(Long strategyId, HashMap<Integer, Integer> map, Integer range);

    Integer getLotteryStrategyAwards(Long strategyId,Integer rateKey);

    Integer getRateRange(Long strategyId);

    //alias算法

    void storeLotteryStrategyAwards2(String key, List<BigDecimal> scaledAwardRates, List<Integer> alias, List<Integer> awards,List<StrategyAwardEntity> strategyAwardEntities);

    List<BigDecimal> getScaledAwardRates(String key);

    List<Integer> getLotteryAliasList(String key);

    List<Integer> getLotteryAwardsList(String key);


    //根据strategyId获取Strategy
    StrategyEntity queryStrategyByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId,String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModel(Integer awardId, Long strategyId);

    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    void cacheStrategyAwardCount(String key, Integer awardCount);

    Boolean subtractionAwardStock(String key);

    Boolean subtractionAwardStock(String key, Date endDateTime);

    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO build);

    StrategyAwardStockKeyVO takeQueueValue();

    void updateStrategyAwardStock(Long strategyId, Integer awardId);

    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Integer awardId);

    Long queryStrategyIdByActivityId(Long activityId);

    Integer queryTodayUserRaffleCount(Long strategyId, String userId);

    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

    Integer queryActivityAccountTotalUseCount(String userId, Long strategyId);

    List<RuleWeightVO> queryAwardRuleWeight(Long strategyId);
}
