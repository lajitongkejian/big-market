package cn.nju.edu.trigger.api.dto;

import lombok.Data;

/**
 * 项目名称：big-market
 * 类名称：UserActivityAccountRequestDTO
 * 作者：tkj
 * 日期：2025/2/25
 * 描述：
 */

@Data
public class UserActivityAccountRequestDTO {
    private String userId;
    private Long activityId;
}
