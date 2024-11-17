package cn.nju.edu.domain.strategy.service.rule.tree;

import cn.nju.edu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * 项目名称：big-market
 * 类名称：ILogicTreeNode
 * 作者：tkj
 * 日期：2024/11/17
 * 描述：
 */
public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId);
}
