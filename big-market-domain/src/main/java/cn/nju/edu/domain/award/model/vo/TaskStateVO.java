package cn.nju.edu.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称：big-market
 * 类名称：TaskStateVO
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */
@Getter
@AllArgsConstructor
public enum TaskStateVO {

    create("create", "创建"),
    complete("complete", "发送完成"),
    fail("fail", "发送失败"),
    ;

    private final String code;
    private final String desc;

}
