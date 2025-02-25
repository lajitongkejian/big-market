package cn.nju.edu.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称：big-market
 * 类名称：RebateTypeVO
 * 作者：tkj
 * 日期：2025/2/25
 * 描述：返利类型（sku 活动库存充值商品、integral 用户活动积分）
 */
@Getter
@AllArgsConstructor
public enum RebateTypeVO {

    SKU("sku", "活动库存充值商品"),
    INTEGRAL("integral", "用户活动积分"),
    ;

    private final String code;
    private final String info;

}

