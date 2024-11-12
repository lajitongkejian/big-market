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
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 项目名称：big-market
 * 类名称：RuleWeightLogicFilter
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository strategyRepository;

    private final Long userPoints = 4500L;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-权重值 userId:{} strategyId:{} ruleModel:{}",ruleMatterEntity.getUserId(),ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());
        String userId = ruleMatterEntity.getUserId();
        String ruleValue = strategyRepository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getAwardId(),ruleMatterEntity.getRuleModel());

        Map<Long,String> map = splitRuleWeight(ruleValue);
        if(null == map || map.isEmpty()){
            return RuleActionEntity.< RuleActionEntity.RaffleBeforeEntity >builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }
        List<Long> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);

        Long nextValue = keys.stream()
                .filter(key -> key>=userPoints)
                .findFirst()
                .orElse(null);
        if(null != nextValue){
            return RuleActionEntity.< RuleActionEntity.RaffleBeforeEntity >builder()
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                            .strategyId(ruleMatterEntity.getStrategyId())
                            .ruleWeightValueKey(nextValue.toString()+":"+map.get(nextValue))
                            .build())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .build();
        }

        return RuleActionEntity.< RuleActionEntity.RaffleBeforeEntity >builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }

    public Map<Long,String> splitRuleWeight(String ruleValue){
        String[] ruleValues = ruleValue.split(Constants.SPACE);
        HashMap<Long,String> map = new HashMap<>();
        for(String ruleWeight : ruleValues) {
            if(null == ruleWeight){
                return map;
            }
            String[] parts = ruleWeight.split(Constants.COLON);
            if(parts.length != 2){
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format"+ruleWeight);
            }
            Long range = Long.parseLong(parts[0]);
            String awardIds = parts[1];
            map.put(range,awardIds);
        }
        return map;
    }
}
