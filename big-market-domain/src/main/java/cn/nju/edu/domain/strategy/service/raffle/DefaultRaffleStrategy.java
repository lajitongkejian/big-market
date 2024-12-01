package cn.nju.edu.domain.strategy.service.raffle;

import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;
import cn.nju.edu.domain.strategy.model.vo.RuleTreeVO;
import cn.nju.edu.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.nju.edu.domain.strategy.model.vo.StrategyAwardStockKeyVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.AbstractRaffleStrategy;
import cn.nju.edu.domain.strategy.service.IRaffleStock;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.IRaffleAward;
import cn.nju.edu.domain.strategy.service.rule.chain.ILogicChain;
import cn.nju.edu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：DefaultRaffleStrategy
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleAward, IRaffleStock {

    public DefaultRaffleStrategy(DefaultChainFactory chainFactory, DefaultTreeFactory treeFactory, IStrategyDispatch strategyDispatch, IStrategyRepository strategyRepository) {
        super(chainFactory, treeFactory, strategyDispatch, strategyRepository);
    }

    //前置规则过滤，责任链
    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = chainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    //抽奖中规则过滤，规则树，所以已经进行了随机抽奖获取了awardId，但可以在后续规则中修改最终抽奖结果
    //每个awardId都可以配置规则树，规则树是对应的奖品来进行配置的
    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRepository.queryStrategyAwardRuleModel(awardId, strategyId);
        //无规则树配置，直接返回抽奖结果即可
        if(null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }
        //根据rulemodel来查询哪一棵规则树
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if(null == ruleTreeVO) {
            throw new RuntimeException("存在抽奖策略配置的规则模型，但未在库表rule_tree rule_tree_node rule_tree_node_line配置");
        }
        IDecisionTreeEngine engine = treeFactory.openLogicTree(ruleTreeVO);
        return engine.process(userId, strategyId, awardId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return strategyRepository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        strategyRepository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardByStrategyId(Long strategyId) {
        return strategyRepository.queryStrategyAwardList(strategyId);
    }
}
