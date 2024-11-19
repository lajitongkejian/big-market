package cn.nju.edu.trigger.job;

import cn.nju.edu.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import cn.nju.edu.domain.strategy.service.IRaffleStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：UpdataAwardStockJob
 * 作者：tkj
 * 日期：2024/11/19
 * 描述：
 */
@Slf4j
@Component
public class UpdataAwardStockJob {

    @Resource
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec(){
        try{
            log.info("定时任务，更新数据库奖品库存数量，从延迟队列中获取");
            StrategyAwardStockKeyVO strategyAwardStockKeyVO = raffleStock.takeQueueValue();
            if(null == strategyAwardStockKeyVO){
                return;
            }
            log.info("定时任务，开始更新数据库奖品库存数量： strategyId:{} awardId:{}",strategyAwardStockKeyVO.getStrategyId(),strategyAwardStockKeyVO.getAwardId());
            raffleStock.updateStrategyAwardStock(strategyAwardStockKeyVO.getStrategyId(),strategyAwardStockKeyVO.getAwardId());
        }catch (Exception e){
            log.error("定时任务更新奖品库存失败",e);
        }
    }
}
