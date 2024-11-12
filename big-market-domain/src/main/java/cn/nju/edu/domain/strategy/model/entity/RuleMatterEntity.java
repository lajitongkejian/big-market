package cn.nju.edu.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：RuleMatterEntity
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleMatterEntity {
    /**
     * 黑名单用户ID
     */
    private String userId;
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖奖品ID - 内部流转使用
     */
    private Integer awardId;
    /**
     * 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)
     */
    private String ruleModel;
}
