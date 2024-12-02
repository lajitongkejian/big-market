package cn.nju.edu.domain.activity.service.rule;

/**
 * 项目名称：big-market
 * 类名称：AbstractActionChain
 * 作者：tkj
 * 日期：2024/12/2
 * 描述：
 */
public abstract class AbstractActionChain implements IActionChain {

    IActionChain next;
    @Override
    public IActionChain next() {
        return next;
    }

    @Override
    public IActionChain appendNext(IActionChain chain) {
        this.next = chain;
        return next;
    }
}
