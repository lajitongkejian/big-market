package cn.nju.edu.domain.strategy.service.armory;

/**
 * 项目名称：big-market
 * 类名称：IStrategyArmory
 * 作者：tkj
 * 日期：2024/11/9
 * 描述：抽奖策略装配，本质上其实是一个service
 */
public interface IStrategyArmory {
    //根据策略id装配奖品表，存入缓存
    void assembleLotteryStrategy(Long strategyId);
    //重构alias抽奖算法
    void assembleLotteryStrategy2(Long strategyId);

    //根据策略id进行奖品抽取，也就是抽奖方法
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId2(Long strategyId);
}
