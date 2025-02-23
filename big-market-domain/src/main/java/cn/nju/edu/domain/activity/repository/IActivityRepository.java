package cn.nju.edu.domain.activity.repository;

import cn.nju.edu.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import cn.nju.edu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import cn.nju.edu.domain.activity.model.entity.*;
import cn.nju.edu.domain.activity.model.vo.ActivitySkuStockKeyVO;

import java.util.Date;
import java.util.List;


/**
 * 项目名称：big-market
 * 类名称：IActivityRepository
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */

public interface IActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);

    ActivityEntity queryRaffleActivityByActivityId(Long activityId);

    ActivityCountEntity queryRaffleActivityCountByActivityCountId(Long activityCountId);

    void doSaveOrder(CreateQuotaOrderAggregate createOrderAggregate);

    void cacheActivitySkuStockCount(String cacheKey, Integer stockCount);

    boolean subtractActivitySkuStock(Long sku, String cacheKey, Date endDateTime);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO build);

    ActivitySkuStockKeyVO takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    UserRaffleOrderEntity queryNoUsedRaffleOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);

    void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);

    ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day);

    ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month);


    ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId);

    List<ActivitySkuEntity> queryActivitySkuListByActivityId(Long activityId);

    Integer queryRaffleActivityAccountDayPartakeCount(Long activityId, String userId);
}
