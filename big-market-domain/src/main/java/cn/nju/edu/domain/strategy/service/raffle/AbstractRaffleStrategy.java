package cn.nju.edu.domain.strategy.service.raffle;

import cn.nju.edu.domain.strategy.model.entity.RaffleAwardEntity;
import cn.nju.edu.domain.strategy.model.entity.RaffleFactorEntity;
import cn.nju.edu.domain.strategy.model.entity.RuleActionEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyEntity;
import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.IRaffleStrategy;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：AbstractRaffleStrategy
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {


    protected IStrategyRepository strategyRepository;


    protected IStrategyDispatch strategyDispatch;

    public AbstractRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch) {
        this.strategyRepository = strategyRepository;
        this.strategyDispatch = strategyDispatch;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(null == strategyId || userId.isEmpty()){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        StrategyEntity strategyEntity = strategyRepository.queryStrategyByStrategyId(strategyId);
        //根据策略id以及所存在的规则进行前置过滤
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = doCheckLogicBeforeRaffle(RaffleFactorEntity.builder()
                .strategyId(strategyId)
                .userId(userId)
                .build(),strategyEntity.getRuleModels());
        //如果存在被拦截情况
        if(RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())){
            if(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())){
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
                String ruleValueWeight = ruleActionEntity.getData().getRuleWeightValueKey();
                Integer awardId = strategyDispatch.getRandomAwardId2(strategyId,ruleValueWeight);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }
        //4.走默认抽奖流程
        Integer awardId = strategyDispatch.getRandomAwardId2(strategyId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();

    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckLogicBeforeRaffle(RaffleFactorEntity build, String... logics);



}
