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
     * 抽奖奖品ID - 内部流转使用
     */
    private Integer awardId;

    /**
     * 奖品配置信息
     */
    private String awardConfig;

    /**
     * 奖品排序序号
     */
    private Integer sort;

    private String awardTitle;


}
