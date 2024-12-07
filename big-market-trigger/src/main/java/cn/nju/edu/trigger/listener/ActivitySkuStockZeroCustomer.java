package cn.nju.edu.trigger.listener;

import cn.nju.edu.domain.activity.service.ISkuStock;
import cn.nju.edu.types.event.BaseEvent;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：ActivitySkuStockZeroCustomer
 * 作者：tkj
 * 日期：2024/12/7
 * 描述：
 */
@Slf4j
@Component
public class ActivitySkuStockZeroCustomer {

    @Resource
    private ISkuStock skuStock;


    @Value("${spring.rabbitmq.topic.activity_sku_stock_zero}")
    private String topic;

    @RabbitListener(queuesToDeclare = @Queue(value = "activity_sku_stock_zero"))
    public void listener(String message) {
        try{
            BaseEvent.EventMessage<Long> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<Long>>(){});
            Long sku = eventMessage.getData();
            skuStock.clearActivitySkuStock(sku);
            skuStock.clearQueueValue();
            log.info("监听sku库存为0消息 topic:{} message:{}",topic,message);
        }catch (Exception e){
            log.error("监听sku库存为0消息失败 topic:{} message:{}");
            throw e;
        }
    }
}
