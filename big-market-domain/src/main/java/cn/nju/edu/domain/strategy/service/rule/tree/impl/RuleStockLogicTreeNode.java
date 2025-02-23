package cn.nju.edu.domain.strategy.service.rule.tree.impl;

import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：RuleStockLogicTreeNode
 * 作者：tkj
 * 日期：2024/11/17
 * 描述：库存处理节点
 */
@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDateTime) {
        log.info("规则过滤-库存扣减：userId:{} strategyId:{} awardId:{} ruleValue:{}", userId, strategyId, awardId,ruleValue);
        Boolean status = strategyDispatch.subtractionAwardStock(strategyId, awardId,endDateTime);
        if(status){
            //发送队列,写入延迟队列，延迟更新数据库记录
            strategyRepository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder()
                            .awardId(awardId)
                            .strategyId(strategyId)
                            .build());

            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardData(DefaultTreeFactory.StrategyAwardVO.builder()
                            .awardId(awardId)
                            .awardRuleValue("")
                            .build())
                    .build();
        }
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
