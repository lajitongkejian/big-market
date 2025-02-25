package cn.nju.edu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：RaffleStrategyRuleWeightRequest
 * 作者：tkj
 * 日期：2025/2/25
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleStrategyRuleWeightRequestDTO {
    private String userId;
    private Long activityId;
}
