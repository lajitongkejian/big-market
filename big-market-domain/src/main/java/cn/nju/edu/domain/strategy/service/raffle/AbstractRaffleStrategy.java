package cn.nju.edu.domain.strategy.service.raffle;

import cn.nju.edu.domain.strategy.model.entity.RaffleAwardEntity;
import cn.nju.edu.domain.strategy.model.entity.RaffleFactorEntity;
import cn.nju.edu.domain.strategy.model.entity.RuleActionEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyEntity;
import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import cn.nju.edu.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.domain.strategy.service.IRaffleStrategy;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.chain.ILogicChain;
import cn.nju.edu.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.nju.edu.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;

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

    private final DefaultChainFactory chainFactory;

    public AbstractRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch, DefaultChainFactory chainFactory) {
        this.strategyRepository = strategyRepository;
        this.strategyDispatch = strategyDispatch;
        this.chainFactory = chainFactory;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(null == strategyId || userId.isEmpty()){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        ILogicChain headLogicChain = chainFactory.openLogicChain(strategyId);
        Integer awardId = headLogicChain.logic(userId,strategyId);

        //5.根据这个所抽到的奖品进行是否解锁的判断,先查询此奖品有什么抽奖规则
        StrategyAwardRuleModelVO ruleModels = strategyRepository.queryStrategyAwardRuleModel(awardId,strategyId);
        String[] proceedModels = ruleModels.splitProceedRuleModels();
        RuleActionEntity<RuleActionEntity.RaffleProceedEntity> raffleProceedEntity= doCheckLogicProceedRaffle(RaffleFactorEntity.builder()
                .strategyId(strategyId)
                .awardId(awardId)
                .userId(userId)
                .build(), proceedModels);

        if(RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(raffleProceedEntity.getCode())){
            log.info("中奖中规则拦截，通过抽奖后规则rule_award_lucky走保底奖励");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则rule_award_lucky走保底奖励")
                    .build();
        }
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();

    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckLogicBeforeRaffle(RaffleFactorEntity build, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleProceedEntity> doCheckLogicProceedRaffle(RaffleFactorEntity build, String... logics);



}
