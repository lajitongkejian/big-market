package cn.nju.edu.domain.strategy.service.rule.chain;

/**
 * 项目名称：big-market
 * 类名称：AbstractLogicChain
 * 作者：tkj
 * 日期：2024/11/16
 * 描述：
 */
public abstract class AbstractLogicChain implements ILogicChain{

    private ILogicChain next;


    @Override
    public ILogicChain appendNext(ILogicChain chain) {
        this.next = chain;
        return next;
    }

    @Override
    public ILogicChain next() {
        return next;
    }

    protected abstract String ruleModel();
}
