package cn.nju.edu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：RaffleResponseDTO
 * 作者：tkj
 * 日期：2024/11/21
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleStrategyResponseDTO {
    private Integer awardId;

    //策略奖品配置的排序编号
    private Integer awardIndex;
}
