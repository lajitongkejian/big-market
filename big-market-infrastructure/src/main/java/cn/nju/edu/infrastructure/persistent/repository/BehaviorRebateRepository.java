package cn.nju.edu.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.nju.edu.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.nju.edu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.nju.edu.domain.rebate.model.entity.TaskEntity;
import cn.nju.edu.domain.rebate.model.vo.BehaviorTypeVO;
import cn.nju.edu.domain.rebate.model.vo.DailyBehaviorRebateVO;
import cn.nju.edu.domain.rebate.repository.IBehaviorRebateRepository;
import cn.nju.edu.infrastructure.event.EventPublisher;
import cn.nju.edu.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import cn.nju.edu.infrastructure.persistent.dao.ITaskDao;
import cn.nju.edu.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import cn.nju.edu.infrastructure.persistent.po.DailyBehaviorRebate;
import cn.nju.edu.infrastructure.persistent.po.Task;
import cn.nju.edu.infrastructure.persistent.po.UserBehaviorRebateOrder;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：BehaviorRebateRepository
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：
 */
@Slf4j
@Repository
public class BehaviorRebateRepository implements IBehaviorRebateRepository {

    @Resource
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;
    @Resource
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private EventPublisher eventPublisher;



    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorType) {
        List<DailyBehaviorRebate> dailyBehaviorRebates = dailyBehaviorRebateDao.queryDailyBehaviorRebateByBehaviorType(behaviorType.getCode());
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = new ArrayList<>();
        for(DailyBehaviorRebate dailyBehaviorRebate : dailyBehaviorRebates){
            dailyBehaviorRebateVOS.add(DailyBehaviorRebateVO.builder()
                    .rebateConfig(dailyBehaviorRebate.getRebateConfig())
                    .behaviorType(dailyBehaviorRebate.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebate.getRebateDesc())
                    .rebateType(dailyBehaviorRebate.getRebateType())
                    .build());
        }
        return dailyBehaviorRebateVOS;
    }

    @Override
    public void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates) {
        try{
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try{
                    for(BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates){
                        //返利订单对象
                        BehaviorRebateOrderEntity orderEntity = behaviorRebateAggregate.getBehaviorRebateOrder();
                        UserBehaviorRebateOrder order = new UserBehaviorRebateOrder();
                        order.setUserId(orderEntity.getUserId());
                        order.setOrderId(orderEntity.getOrderId());
                        order.setBehaviorType(orderEntity.getBehaviorType());
                        order.setRebateDesc(orderEntity.getRebateDesc());
                        order.setRebateType(orderEntity.getRebateType());
                        order.setRebateConfig(orderEntity.getRebateConfig());
                        order.setBizId(orderEntity.getBizId());
                        order.setOutBusinessNo(orderEntity.getOutBusinessNo());
                        userBehaviorRebateOrderDao.insert(order);
                        //任务消息对象
                        TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                        Task task = new Task();
                        task.setTopic(taskEntity.getTopic());
                        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                        task.setState(taskEntity.getState().getCode());
                        task.setMessageId(taskEntity.getMessageId());
                        task.setUserId(taskEntity.getUserId());
                        taskDao.insert(task);
                    }
                    return 1;
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("写入返利记录，唯一索引冲突 userId: {}", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        }finally {
            dbRouter.clear();
        }

        //发送MQ消息
        for(BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates){
            TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
            Task task = new Task();
            task.setMessageId(taskEntity.getMessageId());
            task.setUserId(taskEntity.getUserId());
            try{
                eventPublisher.publish(taskEntity.getTopic(), taskEntity.getMessage());
                taskDao.updateTaskSendMessageCompleted(task);
            }catch (Exception e) {
                log.error("写入返利记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
                taskDao.updateTaskSendMessageFail(task);
            }
        }
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String businessNo, String userId) {
        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
        userBehaviorRebateOrder.setUserId(userId);
        userBehaviorRebateOrder.setOutBusinessNo(businessNo);

        List<UserBehaviorRebateOrder> userBehaviorRebateOrders =  userBehaviorRebateOrderDao.queryOrderByOutBusinessNo(userBehaviorRebateOrder);
        List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = new ArrayList<>();
        for(UserBehaviorRebateOrder order: userBehaviorRebateOrders){
             BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                          .userId(order.getUserId())
                          .orderId(order.getOrderId())
                          .behaviorType(order.getBehaviorType())
                          .rebateDesc(order.getRebateDesc())
                          .rebateType(order.getRebateType())
                          .rebateConfig(order.getRebateConfig())
                          .bizId(order.getBizId())
                          .outBusinessNo(order.getOutBusinessNo())
                          .build();
             behaviorRebateOrderEntities.add(behaviorRebateOrderEntity);
        }
        return behaviorRebateOrderEntities;

    }
}
