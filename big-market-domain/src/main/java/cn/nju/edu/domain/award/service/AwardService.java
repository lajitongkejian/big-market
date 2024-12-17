package cn.nju.edu.domain.award.service;

import cn.nju.edu.domain.award.event.SendAwardMessageEvent;
import cn.nju.edu.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.nju.edu.domain.award.model.entity.TaskEntity;
import cn.nju.edu.domain.award.model.entity.UserAwardRecordEntity;
import cn.nju.edu.domain.award.model.vo.TaskStateVO;
import cn.nju.edu.domain.award.repository.IAwardRepository;
import cn.nju.edu.types.event.BaseEvent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：AwardService
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */
@Service
public class AwardService implements IAwardService {

    @Resource
    private IAwardRepository awardRepository;

    @Resource
    private SendAwardMessageEvent sendAwardMessageEvent;

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //1.构建实体消息对象
        SendAwardMessageEvent.SendAwardMessage sendAwardMessage = new SendAwardMessageEvent.SendAwardMessage();
        sendAwardMessage.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        sendAwardMessage.setUserId(userAwardRecordEntity.getUserId());
        sendAwardMessage.setAwardId(userAwardRecordEntity.getAwardId());
        //2.构建event
        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> eventMessage = sendAwardMessageEvent.buildEventMessage(sendAwardMessage);
        //3.构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setMessage(eventMessage);
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setState(TaskStateVO.create);
        taskEntity.setMessageId(eventMessage.getId());

        //4.构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = new UserAwardRecordAggregate();
        userAwardRecordAggregate.setTaskEntity(taskEntity);
        userAwardRecordAggregate.setUserAwardRecordEntity(userAwardRecordEntity);

        //5.保存中奖记录
        awardRepository.saveUserAwardRecord(userAwardRecordAggregate);

    }
}
