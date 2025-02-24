package cn.nju.edu.domain.rebate.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称：big-market
 * 类名称：BehaviorTypeVO
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：
 */
@Getter
@AllArgsConstructor
public enum BehaviorTypeVO {
    SIGN("sign", "签到（日历）"),
    OPENAI_PAY("openai_pay", "openai 外部支付完成"),
    ;
    private final String code;
    private final String info;


}
