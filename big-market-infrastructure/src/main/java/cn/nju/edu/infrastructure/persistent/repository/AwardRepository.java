package cn.nju.edu.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.nju.edu.domain.award.model.aggregate.UserAwardRecordAggregate;
import cn.nju.edu.domain.award.model.entity.TaskEntity;
import cn.nju.edu.domain.award.model.entity.UserAwardRecordEntity;
import cn.nju.edu.domain.award.repository.IAwardRepository;
import cn.nju.edu.infrastructure.event.EventPublisher;
import cn.nju.edu.infrastructure.persistent.dao.ITaskDao;
import cn.nju.edu.infrastructure.persistent.dao.IUserAwardRecordDao;
import cn.nju.edu.infrastructure.persistent.dao.IUserRaffleOrderDao;
import cn.nju.edu.infrastructure.persistent.po.Task;
import cn.nju.edu.infrastructure.persistent.po.UserAwardRecord;
import cn.nju.edu.infrastructure.persistent.po.UserRaffleOrder;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：IAwardRepository
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */
@Slf4j
@Repository
public class AwardRepository implements IAwardRepository {

    @Resource
    private IUserAwardRecordDao userAwardRecordDao;

    @Resource
    private ITaskDao taskDao;

    @Resource
    private EventPublisher eventPublisher;

    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy routerStrategy;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        String userId = userAwardRecordEntity.getUserId();
        Long activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();

        UserAwardRecord userAwardRecord  = new UserAwardRecord();
        userAwardRecord.setUserId(userAwardRecordEntity.getUserId());
        userAwardRecord.setActivityId(userAwardRecordEntity.getActivityId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardId(userAwardRecordEntity.getAwardId());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());

        Task task = new Task();
        task.setTopic(taskEntity.getTopic());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());
        task.setMessageId(taskEntity.getMessageId());
        task.setUserId(taskEntity.getUserId());

        UserRaffleOrder userRaffleOrder = new UserRaffleOrder();
        userRaffleOrder.setUserId(userAwardRecordEntity.getUserId());
        userRaffleOrder.setOrderId(userAwardRecordEntity.getOrderId());

        //事务执行
        try{
            routerStrategy.doRouter(userId);
            transactionTemplate.execute(status -> {
                //写入数据库
                try{
                    //写入中奖记录
                    userAwardRecordDao.insert(userAwardRecord);
                    //写入任务
                    taskDao.insert(task);
                    //更新订单状态
                    int count = userRaffleOrderDao.updateUserRaffleOrderStateUsed(userRaffleOrder);
                    if(1 != count){
                        status.setRollbackOnly();
                        log.error("写入中奖记录，用户抽奖单已使用过，不可重复抽奖 userId: {} activityId: {} awardId: {}", userId, activityId, awardId);
                        throw new AppException(ResponseCode.ACTIVITY_RAFFLE_ORDER_ERROR.getCode(),ResponseCode.ACTIVITY_RAFFLE_ORDER_ERROR.getInfo());
                    }
                    return 1;

                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("写入中奖记录，唯一索引冲突 userId:{} activityId:{} awardId:{}", userId, activityId, awardId);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(),ResponseCode.INDEX_DUP.getInfo());
                }

            });
        }finally {
            routerStrategy.clear();
        }

        //发送MQ
        try{
            eventPublisher.publish(task.getTopic(), task.getMessage());
            //更新task状态
            taskDao.updateTaskSendMessageCompleted(task);
        }catch (Exception e){
            log.error("写入中奖记录,发送MQ失败：userId:{}，topic:{}",userId,task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }

    }
}
