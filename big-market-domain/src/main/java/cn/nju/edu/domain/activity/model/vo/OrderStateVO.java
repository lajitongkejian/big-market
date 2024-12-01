package cn.nju.edu.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称：big-market
 * 类名称：OrderStatusVO
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Getter
@AllArgsConstructor
public enum OrderStateVO {

    completed("completed","完成");

    private final String code;
    private final String info;
}
