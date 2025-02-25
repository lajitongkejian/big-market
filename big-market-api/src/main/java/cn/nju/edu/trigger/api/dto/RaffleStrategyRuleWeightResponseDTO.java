package cn.nju.edu.trigger.api.dto;

import lombok.Data;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：RaffleStrategyRuleWeightResponseDTO
 * 作者：tkj
 * 日期：2025/2/25
 * 描述：
 */
@Data
public class RaffleStrategyRuleWeightResponseDTO {
    private Integer ruleWeightCount;
    private Integer userActivityAccountTotalUseCount;
    private List<StrategyAward> strategyAwards;
    @Data
    public static class StrategyAward{
        private Integer awardId;
        private String awardTitle;
    }
}
