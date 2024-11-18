package cn.nju.edu.domain.strategy.service.rule.chain;

import cn.nju.edu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * 项目名称：big-market
 * 类名称：ILogicChain
 * 作者：tkj
 * 日期：2024/11/16
 * 描述：责任链接口
 */
public interface ILogicChain extends ILogicChainArmory{

    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);

}
