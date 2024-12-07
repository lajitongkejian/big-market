package cn.nju.edu.domain.activity.event;

import cn.nju.edu.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：ActivitySkuStockZeroMessageEvent
 * 作者：tkj
 * 日期：2024/12/7
 * 描述：
 */
@Component
public class ActivitySkuStockZeroMessageEvent extends BaseEvent<Long> {

    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;
    @Override
    public EventMessage<Long> buildEventMessage(Long data) {
        return EventMessage.<Long>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .data(data)
                .timestamp(new Date())
                .build();
    }

    @Override
    public String topic() {
        return topic;
    }
}
