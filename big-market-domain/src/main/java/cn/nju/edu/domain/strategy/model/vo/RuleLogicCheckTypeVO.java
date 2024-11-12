package cn.nju.edu.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称：big-market
 * 类名称：RuleLogicCheckTypeVO
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {

    ALLOW("0000", "放行；执行后续的流程，不受规则引擎影响"),
    TAKE_OVER("0001","接管；后续的流程，受规则引擎执行结果影响"),
    ;

    private final String code;
    private final String info;

}
