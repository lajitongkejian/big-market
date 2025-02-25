package cn.nju.edu.domain.strategy.service.rule.filter.factory;

import cn.nju.edu.domain.strategy.model.entity.RuleActionEntity;
import cn.nju.edu.domain.strategy.service.annotation.LogicStrategy;
import cn.nju.edu.domain.strategy.service.rule.filter.ILogicFilter;
import com.alibaba.fastjson2.util.AnnotationUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目名称：big-market
 * 类名称：DefaultLogicFactory
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Service
public class DefaultLogicFactory {

    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WIGHT("rule_weight","【抽奖前规则】根据抽奖权重返回可抽奖范围KEY","before"),
        RULE_BLACKLIST("rule_blacklist","【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回","before"),
        RULE_LOCK("rule_lock","【抽奖中规则】,抽奖n次后解锁某些奖品","proceed"),
        RULE_LUCK_AWARD("rule_award_luck","【抽奖后规则】,幸运奖保底","after")

        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isProceed(String code){
            return "proceed".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

        public static boolean isAfter(String code){
            return "after".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

        public static boolean isBefore(String code){
            return "before".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }



    }



}
