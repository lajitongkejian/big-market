package cn.nju.edu.domain.strategy.service.rule.tree.factory;

import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.model.vo.RuleTreeVO;
import cn.nju.edu.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import cn.nju.edu.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 项目名称：big-market
 * 类名称：DefaultTreeFactory
 * 作者：tkj
 * 日期：2024/11/17
 * 描述：规则树工厂
 */
@Service
public class DefaultTreeFactory {
    private final Map<String, ILogicTreeNode> logicTreeGroup;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeGroup) {
        this.logicTreeGroup = logicTreeGroup;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    //规则树执行完业务逻辑后的最终抽奖结果,是引擎处理方法的返回结果
    public static class StrategyAwardVO{
        //抽奖奖品Id
        private Integer awardId;
        //抽奖奖品规则
        private String awardRuleValue;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeGroup, ruleTreeVO);
    }

    //单个规则节点的处理结果，是规则节点的方法的返回结果
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TreeActionEntity{
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardVO strategyAwardData;
    }

}
