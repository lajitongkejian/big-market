package cn.nju.edu.domain.strategy.repository;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：IStrategyRepository
 * 作者：tkj
 * 日期：2024/11/9
 * 描述：策略仓储接口
 */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);



    void storeLotteryStrategyAwards(Long strategyId, HashMap<Integer, Integer> map, Integer range);

    Integer getLotteryStrategyAwards(Long strategyId,Integer rateKey);

    Integer getRateRange(Long strategyId);

    

    void storeLotteryStrategyAwards2(Long strategyId, List<BigDecimal> scaledAwardRates, List<Integer> alias, List<Integer> awards);

    List<BigDecimal> getScaledAwardRates(Long strategyId);

    List<Integer> getLotteryAliasList(Long strategyId);

    List<Integer> getLotteryAwardsList(Long strategyId);
}
