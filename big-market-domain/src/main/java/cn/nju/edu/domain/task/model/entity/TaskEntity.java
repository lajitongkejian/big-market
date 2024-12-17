package cn.nju.edu.domain.task.model.entity;

import lombok.Data;

/**
 * 项目名称：big-market
 * 类名称：taskEntity
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */
@Data
public class TaskEntity {
    /** 活动ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息主体 */
    private String message;

}
