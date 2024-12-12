package cn.nju.edu.domain.activity.service.quota.rule;

import cn.nju.edu.domain.activity.model.entity.ActivityCountEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityEntity;
import cn.nju.edu.domain.activity.model.entity.ActivitySkuEntity;

/**
 * 项目名称：big-market
 * 类名称：IActionChain
 * 作者：tkj
 * 日期：2024/12/2
 * 描述：
 */
public interface IActionChain extends IActionChainArmory {

    boolean action(ActivityCountEntity activityCountEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity);
}
