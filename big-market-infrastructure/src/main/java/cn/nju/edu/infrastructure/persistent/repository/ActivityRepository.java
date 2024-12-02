package cn.nju.edu.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.nju.edu.domain.activity.model.aggregate.CreateOrderAggregate;
import cn.nju.edu.domain.activity.model.entity.ActivityCountEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityOrderEntity;
import cn.nju.edu.domain.activity.model.entity.ActivitySkuEntity;
import cn.nju.edu.domain.activity.model.vo.ActivityStateVO;
import cn.nju.edu.domain.activity.repository.IActivityRepository;
import cn.nju.edu.infrastructure.persistent.dao.*;
import cn.nju.edu.infrastructure.persistent.po.*;
import cn.nju.edu.infrastructure.persistent.redis.IRedisService;
import cn.nju.edu.infrastructure.persistent.redis.RedissonService;
import cn.nju.edu.types.common.Constants;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：ActivityRepository
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Repository
@Slf4j
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IRedisService redisService;
    @Resource
    private IRaffleActivityDao raffleActivityDao;
    @Resource
    private IRaffleActivitySkuDao raffleActivitySkuDao;
    @Resource
    private IRaffleActivityCountDao raffleActivityCountDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy routerStrategy;
    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;
    @Resource
    private IRaffleActivityAccountDao raffleActivityAccountDao;





    @Override
    public ActivitySkuEntity queryActivitySku(Long sku) {
        RaffleActivitySku raffleActivitySku = raffleActivitySkuDao.queryRaffleActivitySkuBySku(sku);
        return ActivitySkuEntity.builder()
                .sku(raffleActivitySku.getSku())
                .activityId(raffleActivitySku.getActivityId())
                .activityCountId(raffleActivitySku.getActivityCountId())
                .stockCount(raffleActivitySku.getStockCount())
                .stockCountSurplus(raffleActivitySku.getStockCountSurplus())
                .build();

    }

    @Override
    public ActivityEntity queryRaffleActivityByActivityId(Long activityId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (null != activityEntity) return activityEntity;
        // 从库中获取数据
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(activityId);
        activityEntity = ActivityEntity.builder()
                .activityId(raffleActivity.getActivityId())
                .activityName(raffleActivity.getActivityName())
                .activityDesc(raffleActivity.getActivityDesc())
                .beginDateTime(raffleActivity.getBeginDateTime())
                .endDateTime(raffleActivity.getEndDateTime())
                .strategyId(raffleActivity.getStrategyId())
                .state(ActivityStateVO.valueOf(raffleActivity.getState()))
                .build();
        redisService.setValue(cacheKey, activityEntity);
        return activityEntity;

    }

    @Override
    public ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.ACTIVITY_COUNT_KEY + activityCountId;
        ActivityCountEntity activityCountEntity = redisService.getValue(cacheKey);
        if (null != activityCountEntity) return activityCountEntity;
        // 从库中获取数据
        RaffleActivityCount raffleActivityCount = raffleActivityCountDao.queryRaffleActivityCountByActivityCountId(activityCountId);
        activityCountEntity = ActivityCountEntity.builder()
                .activityCountId(raffleActivityCount.getActivityCountId())
                .totalCount(raffleActivityCount.getTotalCount())
                .dayCount(raffleActivityCount.getDayCount())
                .monthCount(raffleActivityCount.getMonthCount())
                .build();
        redisService.setValue(cacheKey, activityCountEntity);
        return activityCountEntity;

    }

    @Override
    public void doSaveOrder(CreateOrderAggregate createOrderAggregate) {
        //创建订单po对象
        ActivityOrderEntity activityOrderEntity = createOrderAggregate.getActivityOrderEntity();
        RaffleActivityOrder activityOrder = new RaffleActivityOrder();
        activityOrder.setUserId(activityOrderEntity.getUserId());
        activityOrder.setActivityId(activityOrderEntity.getActivityId());
        activityOrder.setActivityName(activityOrderEntity.getActivityName());
        activityOrder.setStrategyId(activityOrderEntity.getStrategyId());
        activityOrder.setOrderId(activityOrderEntity.getOrderId());
        activityOrder.setOrderTime(activityOrderEntity.getOrderTime());
        activityOrder.setTotalCount(activityOrderEntity.getTotalCount());
        activityOrder.setDayCount(activityOrderEntity.getDayCount());
        activityOrder.setMonthCount(activityOrderEntity.getMonthCount());
        activityOrder.setState(activityOrderEntity.getState().getCode());
        activityOrder.setOutBusinessNo(activityOrderEntity.getOutBusinessNo());
        activityOrder.setSku(activityOrderEntity.getSku());
        //创建账户po对象
        RaffleActivityAccount raffleActivityAccount = new RaffleActivityAccount();
        raffleActivityAccount.setUserId(createOrderAggregate.getUserId());
        raffleActivityAccount.setActivityId(createOrderAggregate.getActivityId());
        raffleActivityAccount.setTotalCount(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setTotalCountSurplus(createOrderAggregate.getTotalCount());
        raffleActivityAccount.setDayCount(createOrderAggregate.getDayCount());
        raffleActivityAccount.setDayCountSurplus(createOrderAggregate.getDayCount());
        raffleActivityAccount.setMonthCount(createOrderAggregate.getMonthCount());
        raffleActivityAccount.setMonthCountSurplus(createOrderAggregate.getMonthCount());
        try{
            //规定分表的区分键为userid
            routerStrategy.doRouter(createOrderAggregate.getUserId());
            //开启一个编程式事务
            transactionTemplate.execute(status -> {
                try{
                    //1.写入订单
                    raffleActivityOrderDao.insert(activityOrder);
                    //2.修改用户账户的抽奖次数
                    int success = raffleActivityAccountDao.updateAccountQuota(raffleActivityAccount);
                    //3.创建账户 - 更新为0，则账户不存在，创新新账户。
                    if(0 == success) {
                        raffleActivityAccountDao.insert(raffleActivityAccount);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    //出现主键冲突则回滚
                    status.setRollbackOnly();
                    log.error("写入订单记录，唯一索引冲突 userId: {} activityId: {} sku: {}", activityOrderEntity.getUserId(), activityOrderEntity.getActivityId(), activityOrderEntity.getSku(), e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode());
                }
            });
        }finally {
            routerStrategy.clear();
        }







    }
}
