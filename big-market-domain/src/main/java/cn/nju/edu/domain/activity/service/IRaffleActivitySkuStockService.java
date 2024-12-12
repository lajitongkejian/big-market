package cn.nju.edu.domain.activity.service;

import cn.nju.edu.domain.activity.model.vo.ActivitySkuStockKeyVO;

/**
 * 项目名称：big-market
 * 类名称：ISkuStock
 * 作者：tkj
 * 日期：2024/12/7
 * 描述：活动sku库存处理接口，对存储抽奖次数的库存进行扣减
 */
public interface IRaffleActivitySkuStockService {
    /**
     * 获取活动sku库存消耗队列
     *
     * @return 奖品库存Key信息
     * @throws InterruptedException 异常
     */
    ActivitySkuStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 清空队列
     */
    void clearQueueValue();

    /**
     * 延迟队列 + 任务趋势更新活动sku库存
     *
     * @param sku 活动商品
     */
    void updateActivitySkuStock(Long sku);

    /**
     * 缓存库存以消耗完毕，清空数据库库存
     *
     * @param sku 活动商品
     */
    void clearActivitySkuStock(Long sku);

}
