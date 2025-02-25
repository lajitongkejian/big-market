package cn.nju.edu.domain.strategy.service;

import cn.nju.edu.domain.strategy.model.vo.RuleWeightVO;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：big-market
 * 类名称：IRaffleRule
 * 作者：tkj
 * 日期：2025/2/23
 * 描述：抽奖规则接口
 */
public interface IRaffleRule {
    Map<String,Integer> queryAwardRuleLockCount(String[] treeIds);

    List<RuleWeightVO> queryAwardRuleWeight(Long strategyId);

    List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId);

}
