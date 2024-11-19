package cn.nju.edu.domain.strategy.service.rule.tree.impl;

import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 项目名称：big-market
 * 类名称：RuleLockLogicTreeNode
 * 作者：tkj
 * 日期：2024/11/17
 * 描述：抽奖次数解锁
 */
@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    private Long userRaffleCount = 10L;
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-次数解锁：userId;{} strategyId;{} awardId;{} ruleValue;{}", userId, strategyId, awardId, ruleValue);
        long raffleCount = 0L;
        try{
            raffleCount = Long.parseLong(ruleValue);
        } catch (Exception e) {
            throw new RuntimeException("规则过滤。次数异常，ruleValue："+ruleValue+"配置不正确");
        }

        if(userRaffleCount >= raffleCount){
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)

                    .build();
        }

        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
