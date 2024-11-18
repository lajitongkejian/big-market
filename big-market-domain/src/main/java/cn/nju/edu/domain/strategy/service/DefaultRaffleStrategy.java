package cn.nju.edu.domain.strategy.service;

import cn.nju.edu.domain.strategy.model.vo.RuleTreeVO;
import cn.nju.edu.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.raffle.AbstractRaffleStrategy;
import cn.nju.edu.domain.strategy.service.rule.chain.ILogicChain;
import cn.nju.edu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 项目名称：big-market
 * 类名称：DefaultRaffleStrategy
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    public DefaultRaffleStrategy(DefaultChainFactory chainFactory, DefaultTreeFactory treeFactory, IStrategyDispatch strategyDispatch, IStrategyRepository strategyRepository) {
        super(chainFactory, treeFactory, strategyDispatch, strategyRepository);
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = chainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = strategyRepository.queryStrategyAwardRuleModel(awardId, strategyId);
        if(null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }
        //
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if(null == ruleTreeVO) {
            throw new RuntimeException("存在抽奖策略配置的规则模型，但未在库表rule_tree rule_tree_node rule_tree_node_line配置");
        }
        IDecisionTreeEngine engine = treeFactory.openLogicTree(ruleTreeVO);
        return engine.process(userId, strategyId, awardId);
    }

}
