package cn.nju.edu.domain.activity.service.rule.impl;

import cn.nju.edu.domain.activity.model.entity.ActivityCountEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityEntity;
import cn.nju.edu.domain.activity.model.entity.ActivitySkuEntity;
import cn.nju.edu.domain.activity.service.rule.AbstractActionChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        log.info("活动责任链-基础信息【有效期、状态】校验开始。");

        return next().action( activityCountEntity,activitySkuEntity, activityEntity);
    }
}

