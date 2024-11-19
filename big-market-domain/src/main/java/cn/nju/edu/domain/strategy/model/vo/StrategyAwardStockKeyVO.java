package cn.nju.edu.domain.strategy.model.vo;

import lombok.*;

/**
 * 项目名称：big-market
 * 类名称：StrategyAwardStockKeyVo
 * 作者：tkj
 * 日期：2024/11/19
 * 描述：奖品库存值对象信息
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyAwardStockKeyVO {
    private Long strategyId;
    private Integer awardId;

}
