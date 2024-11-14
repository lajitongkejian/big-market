package cn.nju.edu.domain.strategy.model.vo;

import cn.nju.edu.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.nju.edu.types.common.Constants;
import lombok.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目名称：big-market
 * 类名称：StrategyAwardRuleModelVO
 * 作者：tkj
 * 日期：2024/11/14
 * 描述：
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class StrategyAwardRuleModelVO {
    private String ruleModels;

    public String[] splitAfterRuleModels() {
        String[] models = ruleModels.split(Constants.SPLIT);
        return Arrays.stream(models)
                .filter(DefaultLogicFactory.LogicModel::isAfter).toArray(String[]::new);


    }

    public String[] splitProceedRuleModels() {
        String[] models = ruleModels.split(Constants.SPLIT);
        return Arrays.stream(models)
                .filter(DefaultLogicFactory.LogicModel::isProceed).toArray(String[]::new);
    }

    public String[] splitBeforeRuleModels() {
        String[] models = ruleModels.split(Constants.SPLIT);
        return Arrays.stream(models)
                .filter(DefaultLogicFactory.LogicModel::isBefore).toArray(String[]::new);
    }

}
