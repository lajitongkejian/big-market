package cn.nju.edu.domain.strategy.service.rule.tree.impl;

import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.nju.edu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 项目名称：big-market
 * 类名称：RuleLuckAwardLogicTreeNode
 * 作者：tkj
 * 日期：2024/11/17
 * 描述：幸运奖保底节点
 */
@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {

        log.info("规则过滤-幸运保底：userId:{} strategyId:{} awardId:{} ruleValue:{}", userId, strategyId, awardId,ruleValue);
        String[] award = ruleValue.split(Constants.COLON);
        if(award.length == 0){
            log.error("兜底奖品未配置 userId:{} strategyId:{} awardId:{} ruleValue:{}", userId, strategyId, awardId, ruleValue);
            throw new RuntimeException("兜底奖品未配置"+ruleValue);
        }
        Integer luckyAwardId = Integer.parseInt(award[0]);
        String awardRuleValue = award.length > 1 ? award[1] : "";
        log.info("规则过滤-幸运保底,兜底奖品为：userId:{} strategyId:{} awardId:{} ruleValue:{}", userId, strategyId, luckyAwardId,awardRuleValue);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardData(DefaultTreeFactory.StrategyAwardVO.builder()
                        .awardId(luckyAwardId)
                        .awardRuleValue(awardRuleValue)
                        .build())
                .build();
    }
}
