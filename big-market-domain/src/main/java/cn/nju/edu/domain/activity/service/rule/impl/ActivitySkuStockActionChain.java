package cn.nju.edu.domain.activity.service.rule.impl;

import cn.nju.edu.domain.activity.model.entity.ActivityCountEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityEntity;
import cn.nju.edu.domain.activity.model.entity.ActivitySkuEntity;
import cn.nju.edu.domain.activity.model.vo.ActivitySkuStockKeyVO;
import cn.nju.edu.domain.activity.repository.IActivityRepository;
import cn.nju.edu.domain.activity.service.armory.IActivityDispatch;
import cn.nju.edu.domain.activity.service.rule.AbstractActionChain;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：ActivitySkuStockActionChain
 * 作者：tkj
 * 日期：2024/12/2
 * 描述：
 */
@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

    @Resource
    private IActivityDispatch activityDispatch;
    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean action(ActivityCountEntity activityCountEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。sku:{},activity:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        boolean status = activityDispatch.subtractActivitySkuStock(activitySkuEntity.getSku(),activityEntity.getEndDateTime());
        //如果扣减成功
        if(status){
            log.info("活动责任链-商品库存处理【有效期、状态、库存(sku)】成功。sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
            //写入延迟队列
            activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
                    .sku(activitySkuEntity.getSku())
                    .activityId(activityEntity.getActivityId())
                    .build());
            return true;
        }
        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(),ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());


    }
}
