package cn.nju.edu.domain.rebate.event;

import cn.nju.edu.domain.rebate.event.SendRebateMessageEvent;
import cn.nju.edu.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：SendRebateMessageEvent
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：
 */
@Service
public class SendRebateMessageEvent extends BaseEvent<SendRebateMessageEvent.SendRebateMessage>{

    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Override
    public BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> buildEventMessage(SendRebateMessageEvent.SendRebateMessage data) {
        return BaseEvent.EventMessage.<SendRebateMessageEvent.SendRebateMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SendRebateMessage{
        /** 用户ID */
        private String userId;
        /** 返利描述 */
        private String rebateDesc;
        /** 返利类型 */
        private String rebateType;
        /** 返利配置 */
        private String rebateConfig;
        /** 业务ID - 唯一ID，确保幂等 */
        private String bizId;


    }
}
