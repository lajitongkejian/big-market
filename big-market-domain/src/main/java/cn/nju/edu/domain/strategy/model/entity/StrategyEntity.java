package cn.nju.edu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 项目名称：big-market
 * 类名称：StrategyEntity
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyEntity {
    /**
     * 抽奖策略规则
     */
    private String ruleModels;
    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 抽奖策略描述
     */
    private String strategyDesc;

    public String[] getRuleModels() {
        if(!StringUtils.isBlank(ruleModels)) {
            return ruleModels.split(",");
        }
        return null;
    }

    public String getRuleWeight(){
        String[] ruleModels = getRuleModels();
        if(ruleModels != null) {
            for (String ruleModel : ruleModels) {
                if("rule_weight".equals(ruleModel)){
                    return ruleModel;
                }
            }
        }
        return null;
    }
}
