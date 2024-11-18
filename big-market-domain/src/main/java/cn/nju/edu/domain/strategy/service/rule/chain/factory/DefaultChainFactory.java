package cn.nju.edu.domain.strategy.service.rule.chain.factory;

import cn.nju.edu.domain.strategy.model.entity.StrategyEntity;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.chain.ILogicChain;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 项目名称：big-market
 * 类名称：DefaultChainFactory
 * 作者：tkj
 * 日期：2024/11/16
 * 描述：
 */
@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainGroup;

    private final IStrategyRepository strategyRepository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository strategyRepository) {
        this.logicChainGroup = logicChainGroup;
        this.strategyRepository = strategyRepository;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategyEntity = strategyRepository.queryStrategyByStrategyId(strategyId);
        String[] ruleModels = strategyEntity.getRuleModels();
        if(null == ruleModels || ruleModels.length == 0) {
            return logicChainGroup.get("default");
        }

        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain logicChainHead = logicChain;
        for(int i = 1; i < ruleModels.length; i++) {
            ILogicChain next = logicChainGroup.get(ruleModels[i]);
            logicChain = logicChain.appendNext(next);
        }
        logicChain.appendNext(logicChainGroup.get("default"));
        return logicChainHead;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StrategyAwardVO {
        private Integer awardId;
        private String logicModel;

    }

    @Getter
    @AllArgsConstructor

    public enum LogicModel {
        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重规则"),
        ;

        private final String code;
        private final String info;

    }



}


