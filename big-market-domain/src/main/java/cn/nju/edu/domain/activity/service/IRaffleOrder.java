package cn.nju.edu.domain.activity.service;

import cn.nju.edu.domain.activity.model.entity.ActivityOrderEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * 项目名称：big-market
 * 类名称：IRaffleOrder
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
public interface IRaffleOrder {
    /**
     * 以sku创建抽奖活动订单，获得参与抽奖资格（可消耗的次数）
     *
     * @param activityShopCartEntity 活动sku实体，通过sku领取活动。
     * @return 活动参与记录实体
     */
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);


}
