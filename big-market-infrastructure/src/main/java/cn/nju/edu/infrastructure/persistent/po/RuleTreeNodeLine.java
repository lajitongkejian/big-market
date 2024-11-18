package cn.nju.edu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：RuleTreeNodeLine
 * 作者：tkj
 * 日期：2024/11/18
 * 描述：
 */
@Data
public class RuleTreeNodeLine {
    /** 自增ID */
    private Long id;
    /** 规则树ID */
    private String treeId;
    /** 规则Key节点 From */
    private String ruleNodeFrom;
    /** 规则Key节点 To */
    private String ruleNodeTo;
    /** 限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围] */
    private String ruleLimitType;
    /** 限定值（到下个节点） */
    private String ruleLimitValue;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
