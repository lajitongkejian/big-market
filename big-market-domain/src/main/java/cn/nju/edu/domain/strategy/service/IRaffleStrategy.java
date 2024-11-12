package cn.nju.edu.domain.strategy.service;

import cn.nju.edu.domain.strategy.model.entity.RaffleAwardEntity;
import cn.nju.edu.domain.strategy.model.entity.RaffleFactorEntity;

/**
 * 项目名称：big-market
 * 类名称：IRaffleStrategy
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：抽奖策略接口
 */
public interface IRaffleStrategy {
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
