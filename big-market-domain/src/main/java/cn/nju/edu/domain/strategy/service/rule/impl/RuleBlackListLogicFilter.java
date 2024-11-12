package cn.nju.edu.domain.strategy.service.rule.impl;

import cn.nju.edu.domain.strategy.model.entity.RuleActionEntity;
import cn.nju.edu.domain.strategy.model.entity.RuleMatterEntity;
import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.annotation.LogicStrategy;
import cn.nju.edu.domain.strategy.service.rule.ILogicFilter;
import cn.nju.edu.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.nju.edu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Rule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：RuleBlackListLogicFilter
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */

@Slf4j
@Component
//过滤规则
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}",ruleMatterEntity.getUserId(),ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());

        //获取抽奖用户id，查看是否在黑名单中
        String userId = ruleMatterEntity.getUserId();
        String ruleValue = strategyRepository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getAwardId(),ruleMatterEntity.getRuleModel());
        String[] ruleValues = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(ruleValues[0]);

        String[] blackUserIds = ruleValues[1].split(Constants.SPLIT);

        for(String blackUserId : blackUserIds){
            if(userId.equals(blackUserId)){
                return RuleActionEntity.< RuleActionEntity.RaffleBeforeEntity >builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }

        return RuleActionEntity.< RuleActionEntity.RaffleBeforeEntity >builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
