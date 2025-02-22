package cn.nju.edu.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：RaffleActivityAccountDay
 * 作者：tkj
 * 日期：2024/12/11
 * 描述：抽奖活动账户表-日次数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleActivityAccountDay {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /** 自增ID */
    private String id;
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 日期（yyyy-mm-dd） */
    private String day;
    /** 日次数 */
    private Integer dayCount;
    /** 日次数-剩余 */
    private Integer dayCountSurplus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

    public String currentDay() {
        return sdf.format(new Date());
    }
}

