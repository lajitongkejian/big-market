package cn.nju.edu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：RaffleFactorEntity
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：抽奖因子实体
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleFactorEntity {

    /**
     * 黑名单用户ID
     */
    private String userId;
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖奖品ID
     */
    private Integer awardId;
}
