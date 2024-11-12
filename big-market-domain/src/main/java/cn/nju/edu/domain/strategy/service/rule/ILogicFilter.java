package cn.nju.edu.domain.strategy.service.rule;


import cn.nju.edu.domain.strategy.model.entity.RuleActionEntity;
import cn.nju.edu.domain.strategy.model.entity.RuleMatterEntity;

/**
 * 项目名称：big-market
 * 类名称：ILogicFilter
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：抽奖规则过滤接口
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);

}
