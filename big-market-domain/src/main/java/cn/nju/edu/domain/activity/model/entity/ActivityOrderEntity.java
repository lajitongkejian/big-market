package cn.nju.edu.domain.activity.model.entity;

import cn.nju.edu.domain.activity.model.vo.OrderStateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：ActivityOrderEntity
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityOrderEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 下单时间
     */
    private Date orderTime;

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
     * 订单状态
     */
    private OrderStateVO state;

    /**
     * 业务防重ID
     */
    private String outBusinessNo;

    /**
     * 商品sku
     */
    private Long sku;
}
