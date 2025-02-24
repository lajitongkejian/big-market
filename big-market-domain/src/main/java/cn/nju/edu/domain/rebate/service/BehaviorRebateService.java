package cn.nju.edu.domain.rebate.service;

import cn.nju.edu.domain.award.model.vo.TaskStateVO;
import cn.nju.edu.domain.rebate.event.SendRebateMessageEvent;
import cn.nju.edu.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.nju.edu.domain.rebate.model.entity.BehaviorEntity;
import cn.nju.edu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.nju.edu.domain.rebate.model.entity.TaskEntity;
import cn.nju.edu.domain.rebate.model.vo.DailyBehaviorRebateVO;
import cn.nju.edu.domain.rebate.repository.IBehaviorRebateRepository;
import cn.nju.edu.types.common.Constants;
import cn.nju.edu.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：BehaviorRebateService
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：行为返利服务实现
 */
@Service
public class BehaviorRebateService implements IBehaviorRebateService {

    @Resource
    private IBehaviorRebateRepository rebateRepository;
    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        //1.查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = rebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorType());
        if(null == dailyBehaviorRebateVOS || dailyBehaviorRebateVOS.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> orderIds = new ArrayList<>();
        List<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        //2.构建聚合对象
        for(DailyBehaviorRebateVO dailyBehaviorRebateVO : dailyBehaviorRebateVOS) {
            String bizId = behaviorEntity.getUserId() + "_" + dailyBehaviorRebateVO.getRebateType() + "_" + behaviorEntity.getOutBusinessNo();
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .bizId(bizId)
                    .userId(behaviorEntity.getUserId())
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .build();
            orderIds.add(behaviorRebateOrderEntity.getOrderId());
            //3.构建消息对象
            SendRebateMessageEvent.SendRebateMessage sendRebateMessage = SendRebateMessageEvent.SendRebateMessage.builder()
                    .bizId(bizId)
                    .userId(behaviorEntity.getUserId())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .build();
            //4.构建事件消息
            BaseEvent.EventMessage<SendRebateMessageEvent.SendRebateMessage> eventMessage = sendRebateMessageEvent.buildEventMessage(sendRebateMessage);
            //5.组装任务对象
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setMessage(eventMessage);
            taskEntity.setUserId(behaviorEntity.getUserId());
            taskEntity.setState(TaskStateVO.create);
            taskEntity.setTopic(sendRebateMessageEvent.topic());
            taskEntity.setMessageId(eventMessage.getId());
            //6，构建聚合对象并添加到集合
            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .behaviorRebateOrder(behaviorRebateOrderEntity)
                    .userId(behaviorEntity.getUserId())
                    .taskEntity(taskEntity)
                    .build();
            behaviorRebateAggregates.add(behaviorRebateAggregate);


        }
        //7.存储聚合对象数据
        rebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(),behaviorRebateAggregates);
        return orderIds;
    }
}
