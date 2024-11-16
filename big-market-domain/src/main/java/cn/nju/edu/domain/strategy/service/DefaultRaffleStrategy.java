package cn.nju.edu.domain.strategy.service;

import cn.nju.edu.domain.strategy.model.entity.RaffleFactorEntity;
import cn.nju.edu.domain.strategy.model.entity.RuleActionEntity;
import cn.nju.edu.domain.strategy.model.entity.RuleMatterEntity;
import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.raffle.AbstractRaffleStrategy;
import cn.nju.edu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.nju.edu.domain.strategy.service.rule.filter.ILogicFilter;
import cn.nju.edu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private DefaultLogicFactory logicFactory;

    public DefaultRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch, DefaultChainFactory chainFactory) {
        super(strategyRepository, strategyDispatch, chainFactory);
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckLogicBeforeRaffle(RaffleFactorEntity build, String... logics) {
        if(null == logics || logics.length == 0){
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> logicFilterMap = logicFactory.openLogicFilter();

        //1.先过滤黑名单规则
        String ruleBlackList = Arrays.stream(logics)
                .filter(logic->logic.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .findFirst()
                .orElse(null);
        if(null != ruleBlackList && !ruleBlackList.isEmpty()){
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> blackListFilter = logicFilterMap.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
//            if(null == blackListFilter){
//                throw new AppException(ResponseCode.UN_ERROR.getCode(),ResponseCode.UN_ERROR.getInfo());
//            }
            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity =
                    blackListFilter.filter(RuleMatterEntity.builder()
                            .strategyId(build.getStrategyId())
                            .userId(build.getUserId())
                            .awardId(null)
                            .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                            .build());
            if(!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())){
                return ruleActionEntity;
            }

        }
        //2.继续过滤其余前置规则
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = null;
        List<String> ruleModels = Arrays.stream(logics).
                filter(model->!DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(model))
                .collect(Collectors.toList());
        for (String ruleModel : ruleModels) {
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> ruleFilter = logicFilterMap.get(ruleModel);
            ruleActionEntity = ruleFilter.filter(RuleMatterEntity.builder()
                            .ruleModel(ruleModel)
                            .userId(build.getUserId())
                            .strategyId(build.getStrategyId())
                            .build());

            log.info("抽奖前规则过滤 userId: {} ruleModel: {} code: {} info: {}", build.getUserId(), ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
            if(!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
        }

        return ruleActionEntity;
    }

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleProceedEntity> doCheckLogicProceedRaffle(RaffleFactorEntity build, String... logics) {
        if(null == logics || logics.length == 0){
            return RuleActionEntity.<RuleActionEntity.RaffleProceedEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        Map<String, ILogicFilter<RuleActionEntity.RaffleProceedEntity>> logicFilterMap = logicFactory.openLogicFilter();
        RuleActionEntity<RuleActionEntity.RaffleProceedEntity> ruleActionEntity = null;

        for (String ruleModel : logics) {
            ILogicFilter<RuleActionEntity.RaffleProceedEntity> ruleFilter = logicFilterMap.get(ruleModel);
            ruleActionEntity = ruleFilter.filter(RuleMatterEntity.builder()
                    .ruleModel(ruleModel)
                    .userId(build.getUserId())
                    .strategyId(build.getStrategyId())
                    .awardId(build.getAwardId())
                    .build());

            log.info("抽奖中规则过滤 userId: {} ruleModel: {} code: {} info: {}", build.getUserId(), ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
            if(!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
        }

        return ruleActionEntity;

    }
}
