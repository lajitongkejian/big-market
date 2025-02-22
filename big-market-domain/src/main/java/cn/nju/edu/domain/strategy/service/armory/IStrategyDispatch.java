package cn.nju.edu.domain.strategy.service.armory;

/**
 * 项目名称：big-market
 * 类名称：IStrategyDispath
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：负责策略抽奖的服务模块，专门用于生成随机数并且抽奖完对对应奖品库存进行扣减
 */
public interface IStrategyDispatch {
    //根据策略id进行奖品抽取，也就是抽奖方法

    Integer getRandomAwardId(Long strategyId);
    //权重过滤规则抽奖
    Integer getRandomAwardId2(Long strategyId,String ruleWeightValue);
    //无过滤规则
    Integer getRandomAwardId2(Long strategyId);
    //库存扣减方法
    Boolean subtractionAwardStock(Long strategyId,Integer awardId);

}
