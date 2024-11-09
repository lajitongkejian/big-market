package cn.nju.edu.infrastructure.persistent.dao;

import cn.nju.edu.infrastructure.persistent.po.Award;
import cn.nju.edu.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：IStrategyDao
 * 作者：tkj
 * 日期：2024/11/9
 */
@Mapper
public interface IStrategyDao {
    List<Strategy> queryStrategyList();
}