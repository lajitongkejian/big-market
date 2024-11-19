package cn.nju.edu.domain.strategy.service;

import cn.nju.edu.domain.strategy.model.vo.StrategyAwardStockKeyVO;

/**
 * 项目名称：big-market
 * 类名称：IRaffleStock
 * 作者：tkj
 * 日期：2024/11/19
 * 描述：
 */
public interface IRaffleStock {
    /**
     * 获取奖品库存消耗队列
     *
     * @return 奖品库存Key信息
     * @throws InterruptedException 异常
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 更新奖品库存消耗记录
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

}
