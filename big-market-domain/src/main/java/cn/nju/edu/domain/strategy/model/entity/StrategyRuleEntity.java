package cn.nju.edu.domain.strategy.model.entity;

import cn.nju.edu.types.common.Constants;
import cn.nju.edu.types.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：big-market
 * 类名称：StrategyRuleEntity
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyRuleEntity {
    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 抽奖奖品ID【规则类型为策略，则不需要奖品ID】
     */
    private Integer awardId;

    /**
     * 抽象规则类型；1-策略规则、2-奖品规则
     */
    private Integer ruleType;

    /**
     * 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)
     */
    private String ruleModel;

    /**
     * 抽奖规则比值
     */
    private String ruleValue;

    /**
     * 抽奖规则描述
     */
    private String ruleDesc;

    public Map<String , List<Integer>> getRuleWeightValues(){
        if(!"rule_weight".equals(ruleModel)) return null;
        Map<String , List<Integer>> ruleWeightValues = new HashMap<String , List<Integer>>();
        String[] ruleValuesGroups = ruleValue.split(Constants.SPACE);
        for (String ruleValuesGroup : ruleValuesGroups) {
            String[] parts = ruleValuesGroup.split(Constants.COLON);
            if(parts.length != 2){
                throw new AppException("rule_weight rule_rule invalid input format" + ruleValuesGroup);
            }
            String[] ruleValues = parts[1].split(Constants.SPLIT);
            List<Integer> ruleWeightValuesList = new ArrayList<Integer>();
            for(String ruleValue : ruleValues){
                ruleWeightValuesList.add(Integer.parseInt(ruleValue));
            }
            ruleWeightValues.put(ruleValuesGroup, ruleWeightValuesList);
        }
        return ruleWeightValues;
    }
}
