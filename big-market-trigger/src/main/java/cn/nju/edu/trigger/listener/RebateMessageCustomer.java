package cn.nju.edu.trigger.listener;

import cn.nju.edu.domain.activity.model.entity.SkuRechargeEntity;
import cn.nju.edu.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.nju.edu.domain.rebate.event.SendRebateMessageEvent;
import cn.nju.edu.domain.rebate.model.vo.RebateTypeVO;
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
 * 类名称：RebateMessageCustomer
 * 作者：tkj
 * 日期：2025/2/25
 * 描述：监听行为返利消息
 */
@Slf4j
@Component
public class RebateMessageCustomer {
    @Value("${spring.rabbitmq.topic.send_rebate}")
    private String topic;

    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;

    @RabbitListener(queuesToDeclare = @Queue(value = "send_rebate"))
    public void listen(String message) {
        try{
            log.info("监听用户返利消息 topic:{} message:{}",topic,message);
            //1.将JSON字符串转换为对象
            BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> eventMessage;
            eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage>>() {
            }.getType());
            SendRebateMessageEvent.SendRebateMessage rebateMessage = eventMessage.getData();
            if(!RebateTypeVO.SKU.getCode().equals(rebateMessage.getRebateType())){
                log.info("监听用户行为返利消息 - 非sku奖励暂时不处理 topic: {} message: {}", topic, message);
                return;
            }
            SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
            skuRechargeEntity.setUserId(rebateMessage.getUserId());
            skuRechargeEntity.setSku(Long.parseLong(rebateMessage.getRebateConfig()));
            skuRechargeEntity.setOutBusinessNo(rebateMessage.getBizId());
            raffleActivityAccountQuotaService.createSkuRechargeOrder(skuRechargeEntity);

        }catch (Exception e){
            log.error("监听用户返利消息失败 topic:{} message:{}",topic,message,e);
            throw e;
        }
    }
}
