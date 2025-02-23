package cn.nju.edu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：RaffleAwardListRequestDTO
 * 作者：tkj
 * 日期：2024/11/21
 * 描述：抽奖奖品列表请求对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleAwardListRequestDTO {

    @Deprecated
    // 抽奖策略ID
    private Long strategyId;
    // 活动id
    private Long activityId;

    private String userId;

}
