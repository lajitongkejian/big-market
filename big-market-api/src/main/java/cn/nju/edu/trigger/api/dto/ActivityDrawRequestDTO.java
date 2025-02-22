package cn.nju.edu.trigger.api.dto;

import lombok.Data;

/**
 * 项目名称：big-market
 * 类名称：ActivityDrawRequestDTO
 * 作者：tkj
 * 日期：2025/2/22
 * 描述：
 */
@Data
public class ActivityDrawRequestDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}

