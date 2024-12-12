package cn.nju.edu.domain.activity.service.armory;

import cn.nju.edu.domain.activity.model.entity.ActivitySkuEntity;
import cn.nju.edu.domain.activity.repository.IActivityRepository;
import cn.nju.edu.types.common.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：ActivityArmory
 * 作者：tkj
 * 日期：2024/12/5
 * 描述：缓存sku库存，以及提供库存扣减方法
 */
@Service
public class ActivityArmory implements IActivityArmory,IActivityDispatch{

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean assembleActivitySku(Long sku) {
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(sku);
        cacheActivitySkuStockCount(sku,activitySkuEntity.getStockCount());
        //查询预热缓存的数据
        activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        return true;
    }

    private void cacheActivitySkuStockCount(Long sku, Integer stockCount) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        activityRepository.cacheActivitySkuStockCount(cacheKey,stockCount);

    }

    @Override
    public boolean subtractActivitySkuStock(Long sku, Date endDateTime) {
        String cacheKey = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return activityRepository.subtractActivitySkuStock(sku,cacheKey,endDateTime);
    }
}
