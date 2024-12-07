package cn.nju.edu.domain.activity.repository;

import cn.nju.edu.domain.activity.model.aggregate.CreateOrderAggregate;
import cn.nju.edu.domain.activity.model.entity.ActivityCountEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityEntity;
import cn.nju.edu.domain.activity.model.entity.ActivitySkuEntity;
import cn.nju.edu.domain.activity.model.vo.ActivitySkuStockKeyVO;
import org.springframework.stereotype.Repository;

import java.util.Date;


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

    void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    void cacheActivitySkuStockCount(String cacheKey, Integer stockCount);

    boolean subtractActivitySkuStock(Long sku, String cacheKey, Date endDateTime);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO build);

    ActivitySkuStockKeyVO takeQueueValue();

    void clearQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);
}
