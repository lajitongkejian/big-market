package cn.nju.edu.domain.strategy.service.rule.chain;

/**
 * 项目名称：big-market
 * 类名称：ILogicChainArmory
 * 作者：tkj
 * 日期：2024/11/16
 * 描述：
 */
public interface ILogicChainArmory {
    ILogicChain appendNext(ILogicChain chain);

    ILogicChain next();
}
