package cn.nju.edu.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：DailyBehaviorRebateVO
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyBehaviorRebateVO {
    /** 行为类型（sign 签到、openai_pay 支付） */
    private String behaviorType;
    /** 返利描述 */
    private String rebateDesc;
    /** 返利类型（sku 活动库存充值商品、integral 用户活动积分） */
    private String rebateType;
    /** 返利配置 */
    private String rebateConfig;
}
