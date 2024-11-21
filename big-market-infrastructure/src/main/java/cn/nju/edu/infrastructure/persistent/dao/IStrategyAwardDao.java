package cn.nju.edu.infrastructure.persistent.dao;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;
import cn.nju.edu.infrastructure.persistent.po.Award;
import cn.nju.edu.infrastructure.persistent.po.StrategyAward;
import cn.nju.edu.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：IStrategyAwardDao
 * 作者：tkj
 * 日期：2024/11/9
 */
@Mapper
public interface IStrategyAwardDao {
    void updateStrategyAwardStock(StrategyAward strategyAward);


    List<StrategyAward> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    String queryStrategyAwardRule(StrategyRule build);

    StrategyAward queryStrategyAward(StrategyAward strategyAward);
}
