package cn.nju.edu.domain.activity.repository;

import cn.nju.edu.domain.activity.model.entity.ActivityCountEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityEntity;
import cn.nju.edu.domain.activity.model.entity.ActivitySkuEntity;
import org.springframework.stereotype.Repository;


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

}
