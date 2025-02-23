package cn.nju.edu.domain.strategy.service.rule.tree.factory.engine;

import cn.nju.edu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：IDecisionTreeEngine
 * 作者：tkj
 * 日期：2024/11/17
 * 描述：
 */
public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDateTime);
}
