package cn.nju.edu.domain.activity.service.quota.rule;

/**
 * 项目名称：big-market
 * 类名称：IActionChainArmory
 * 作者：tkj
 * 日期：2024/12/2
 * 描述：
 */
public interface IActionChainArmory {
    IActionChain appendNext(IActionChain chain);

    IActionChain next();
}
