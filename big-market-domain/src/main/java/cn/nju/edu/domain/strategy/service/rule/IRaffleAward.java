package cn.nju.edu.domain.strategy.service.rule;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：IRaffleAward
 * 作者：tkj
 * 日期：2024/11/21
 * 描述：
 */
public interface IRaffleAward {
    List<StrategyAwardEntity> queryStrategyAwardByStrategyId(Long strategyId);
}
