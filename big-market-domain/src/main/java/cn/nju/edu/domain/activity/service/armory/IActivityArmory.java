package cn.nju.edu.domain.activity.service.armory;

/**
 * 项目名称：big-market
 * 类名称：IActivityArmory
 * 作者：tkj
 * 日期：2024/12/5
 * 描述：
 */
public interface IActivityArmory {
    boolean assembleActivitySku(Long sku);

    boolean assembleActivitySkuByActivityId(Long activityId);
}
