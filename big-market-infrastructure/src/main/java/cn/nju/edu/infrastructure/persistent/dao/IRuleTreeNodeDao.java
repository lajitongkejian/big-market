package cn.nju.edu.infrastructure.persistent.dao;

import cn.nju.edu.infrastructure.persistent.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：IRuleTreeNodeDao
 * 作者：tkj
 * 日期：2024/11/18
 * 描述：
 */
@Mapper
public interface IRuleTreeNodeDao {
    List<RuleTreeNode> queryRuleTreeNodeListByTreeId(String treeId);

    List<RuleTreeNode> queryRuleLocks(String[] treeIds);
}
