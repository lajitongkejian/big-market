package cn.nju.edu.domain.strategy.service.rule.chain.impl;

import cn.nju.edu.domain.strategy.model.entity.RuleActionEntity;
import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.chain.AbstractLogicChain;
import cn.nju.edu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import cn.nju.edu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：BlackListLogicChain
 * 作者：tkj
 * 日期：2024/11/16
 * 描述：黑名单过滤责任链
 */
@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {


    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单过滤开始 userId:{}, strategyId:{} ,ruleModel:{}", userId, strategyId, ruleModel());
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId,ruleModel());
        String[] ruleValues = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(ruleValues[0]);
        String[] blackUserIds = ruleValues[1].split(Constants.SPLIT);
        for(String blackUserId : blackUserIds){
            if(userId.equals(blackUserId)){
                log.info("抽奖责任链-黑名单拦截 userId:{}, strategyId:{} ,ruleModel:{} ,awardId:{}", userId, strategyId, ruleModel(),awardId);
                return awardId;
            }
        }
        log.info("抽奖责任链-黑名单过滤通过 userId:{}, strategyId:{} ,ruleModel:{}", userId, strategyId, ruleModel());

        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }
}
