package cn.nju.edu.infrastructure.persistent.dao;

import cn.nju.edu.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名称：big-market
 * 类名称：IRuleTreeDao
 * 作者：tkj
 * 日期：2024/11/18
 * 描述：
 */
@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTreeByTreeId(String treeId);
}
