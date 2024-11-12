package cn.nju.edu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：RaffleAwardEntity
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：抽奖结果实体，已经抽完奖的结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleAwardEntity {
    /**
     * 策略id
     */
    private Long strategyId;
    /**
     * 抽奖奖品ID - 内部流转使用
     */
    private Integer awardId;

    /**
     * 奖品对接标识 - 每一个都是一个对应的发奖策略
     */
    private String awardKey;

    /**
     * 奖品配置信息
     */
    private String awardConfig;

    /**
     * 奖品内容描述
     */
    private String awardDesc;
}
