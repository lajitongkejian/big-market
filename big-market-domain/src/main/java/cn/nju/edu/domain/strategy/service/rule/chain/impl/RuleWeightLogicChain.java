package cn.nju.edu.domain.strategy.service.rule.chain.impl;

import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.chain.AbstractLogicChain;
import cn.nju.edu.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 项目名称：big-market
 * 类名称：RuleWeightLogicChain
 * 作者：tkj
 * 日期：2024/11/16
 * 描述：积分门槛过滤
 */

@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository strategyRepository;

    @Resource
    private IStrategyDispatch strategyDispatch;

    private final Long userPoints = 4500L;

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重过滤开始 userId:{}, strategyId:{} ,ruleModel:{}", userId, strategyId, ruleModel());
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId,ruleModel());
        Map<Long,String> map = splitRuleWeight(ruleValue);
        if(null == map || map.isEmpty()){
            return next().logic(userId,strategyId);
        }
        List<Long> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        Long nextValue = keys.stream()
                .filter(key -> userPoints >= key)
                .max(Comparator.naturalOrder())
                .orElse(null);
        if(null != nextValue){
            String awardIds = map.get(nextValue);
            Integer awardId = strategyDispatch.getRandomAwardId2(strategyId,String.valueOf(nextValue)+":"+awardIds);
            log.info("抽奖责任链-抽奖权重规则拦截 userId:{}, strategyId:{} ,ruleModel:{} ,awardId:{}", userId, strategyId, ruleModel(),awardId);
            return awardId;
        }
        log.info("抽奖责任链-抽奖权重规则过滤通过 userId:{}, strategyId:{} ,ruleModel:{}", userId, strategyId, ruleModel());

        return next().logic(userId,strategyId);


    }

    @Override
    protected String ruleModel() {
        return "rule_weight";
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
