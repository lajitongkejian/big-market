package cn.nju.edu.domain.activity.service.armory;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：IActivityDispatch
 * 作者：tkj
 * 日期：2024/12/5
 * 描述：活动库存扣减
 */
public interface IActivityDispatch {
    boolean subtractActivitySkuStock(Long sku, Date endDateTime);
}
