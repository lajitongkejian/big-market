package cn.nju.edu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：RaffleRequestDTO
 * 作者：tkj
 * 日期：2024/11/21
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleRequestDTO {

    private Long strategyId;
}
