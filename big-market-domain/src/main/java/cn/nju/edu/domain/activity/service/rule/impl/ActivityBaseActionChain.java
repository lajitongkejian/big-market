package cn.nju.edu.domain.activity.service.rule.impl;

import cn.nju.edu.domain.activity.model.entity.ActivityCountEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityEntity;
import cn.nju.edu.domain.activity.model.entity.ActivitySkuEntity;
import cn.nju.edu.domain.activity.model.vo.ActivityStateVO;
import cn.nju.edu.domain.activity.service.rule.AbstractActionChain;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：ActivityBaseActionChain
 * 作者：tkj
 * 日期：2024/12/2
 * 描述：
 */
@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {

    @Override
    public boolean action(ActivityCountEntity activityCountEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity) {
        log.info("活动责任链-基础信息【有效期、状态】校验开始。sku:{},activityId:{}",activitySkuEntity.getSku(),activitySkuEntity.getActivityId());
        //教研活动状态
        if(!ActivityStateVO.open.getCode().equals(activityEntity.getState().getCode())){
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(),ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        //校验活动日期
        Date currentDate = new Date();
        if(activityEntity.getBeginDateTime().after(currentDate) || activityEntity.getEndDateTime().before(currentDate)){
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(),ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        //校验活动库存
        if(activitySkuEntity.getStockCountSurplus() <=0){
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(),ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
        }
        return next().action( activityCountEntity,activitySkuEntity, activityEntity);
    }
}

