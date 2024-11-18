package cn.nju.edu.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：RuleTreeNode
 * 作者：tkj
 * 日期：2024/11/18
 * 描述：
 */
@Data
public class RuleTreeNode {
    /** 自增ID */
    private Long id;
    /** 规则树ID */
    private String treeId;
    /** 规则Key */
    private String ruleKey;
    /** 规则描述 */
    private String ruleDesc;
    /** 规则比值 */
    private String ruleValue;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
