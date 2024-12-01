package cn.nju.edu.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称：big-market
 * 类名称：ActivityStatusVO
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Getter
@AllArgsConstructor
public enum ActivityStateVO {
    create("create", "创建"),
    open("open", "开启"),
    close("close", "关闭"),
    ;


    private final String code;
    private final String info;


}
