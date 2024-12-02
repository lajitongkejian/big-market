package cn.nju.edu.domain.activity.model.aggregate;

import cn.nju.edu.domain.activity.model.entity.ActivityAccountEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：CreateOrderAggregate
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 总次数
     */
    private Integer totalCount;
    /**
     * 日次数
     */
    private Integer dayCount;
    /**
     * 月次数
     */
    private Integer monthCount;
    /**
     * 活动订单实体
     */
    private ActivityOrderEntity activityOrderEntity;

}

