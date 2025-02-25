package cn.nju.edu.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 项目名称：big-market
 * 类名称：RuleWeightVO
 * 作者：tkj
 * 日期：2025/2/25
 * 描述：
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleWeightVO {
    private String ruleValue;
    private Integer weight;
    private List<Integer> awardIds;
    private List<Award> awardList;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Award{
        private Integer awardId;
        private String awardTitle;
    }
}
