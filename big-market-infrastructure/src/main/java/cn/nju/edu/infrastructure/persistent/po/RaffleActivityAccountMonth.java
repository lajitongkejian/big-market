package cn.nju.edu.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：RaffleActivityAccountMonth
 * 作者：tkj
 * 日期：2024/12/11
 * 描述：抽奖活动账户表-月次数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleActivityAccountMonth {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

    /** 自增ID */
    private String id;
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 月（yyyy-mm） */
    private String month;
    /** 月次数 */
    private Integer monthCount;
    /** 月次数-剩余 */
    private Integer monthCountSurplus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

    public String currentMonth() {
        return sdf.format(new Date());
    }
}

