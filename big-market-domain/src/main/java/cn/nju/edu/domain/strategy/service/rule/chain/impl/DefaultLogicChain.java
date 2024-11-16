package cn.nju.edu.domain.strategy.service.rule.chain.impl;

import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.chain.AbstractLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：DefaultLogicChain
 * 作者：tkj
 * 日期：2024/11/16
 * 描述：幸运兜底规则,责任链中的最后一个模块
 */
@Slf4j
@Component("default")

public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Override
    public Integer logic(String userId, Long strategyId) {
        Integer awardId =  strategyDispatch.getRandomAwardId2(strategyId);
        log.info("抽奖责任链-幸运保底过滤 userId:{}, strategyId:{} ,ruleModel:{} ,awardId:{}", userId, strategyId, ruleModel(),awardId);
        return awardId;

    }

    @Override
    protected String ruleModel() {
        return "default";
    }
}
