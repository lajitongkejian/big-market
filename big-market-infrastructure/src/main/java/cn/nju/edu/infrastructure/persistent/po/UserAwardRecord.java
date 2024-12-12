package cn.nju.edu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：UserAwardRecord
 * 作者：tkj
 * 日期：2024/12/11
 * 描述：用户中奖记录表
 */
@Data
public class UserAwardRecord {

    /** 自增ID */
    private String id;
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖订单ID【作为幂等使用】 */
    private String orderId;
    /** 奖品ID */
    private Integer awardId;
    /** 奖品标题（名称） */
    private String awardTitle;
    /** 中奖时间 */
    private Date awardTime;
    /** 奖品状态；create-创建、completed-发奖完成 */
    private String awardState;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}

