package cn.nju.edu.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 项目名称：big-market
 * 类名称：AwardStateVO
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */

@Getter
@AllArgsConstructor
public enum AwardStateVO {
    create("create","创建"),
    complete("complete","发奖完成"),
    fail("fail","发奖失败"),
    ;

    private final String code;
    private final String desc;
}
