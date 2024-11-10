package cn.nju.edu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 项目名称：big-market
 * 类名称：StrategyAwardEntity
 * 作者：tkj
 * 日期：2024/11/9
 * 描述：策略奖品实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyAwardEntity {

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 抽奖奖品ID - 内部流转使用
     */
    private Integer awardId;
    /**
     * 奖品库存总量
     */
    private Integer awardCount;

    /**
     * 奖品库存剩余
     */
    private Integer awardCountSurplus;

    /**
     * 奖品中奖概率
     */
    private BigDecimal awardRate;
}