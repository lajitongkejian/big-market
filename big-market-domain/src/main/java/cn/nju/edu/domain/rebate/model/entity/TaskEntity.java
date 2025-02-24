package cn.nju.edu.domain.rebate.model.entity;

import cn.nju.edu.domain.award.event.SendAwardMessageEvent;
import cn.nju.edu.domain.award.model.vo.TaskStateVO;
import cn.nju.edu.domain.rebate.event.SendRebateMessageEvent;
import cn.nju.edu.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：TaskEntity
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    /** 活动ID */
    private String userId;
    /** 消息主题 */
    private String topic;
    /** 消息编号 */
    private String messageId;
    /** 消息主体 */
    private BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> message;
    /** 任务状态；create-创建、completed-完成、fail-失败 */
    private TaskStateVO state;

}
