package cn.nju.edu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：RaffleActivityCount
 * 作者：tkj
 * 日期：2024/11/30
 * 描述：
 */
@Data
public class RaffleActivityCount {

    /**
     * 自增ID
     */
    private Long id;

    /**
     * 活动次数编号
     */
    private Long activityCountId;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}


