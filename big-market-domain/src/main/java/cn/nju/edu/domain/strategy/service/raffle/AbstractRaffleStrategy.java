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
import cn.nju.edu.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
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

    protected final DefaultChainFactory chainFactory;

    protected final DefaultTreeFactory treeFactory;

    public AbstractRaffleStrategy(DefaultChainFactory chainFactory, DefaultTreeFactory treeFactory, IStrategyDispatch strategyDispatch, IStrategyRepository strategyRepository) {
        this.chainFactory = chainFactory;
        this.treeFactory = treeFactory;
        this.strategyDispatch = strategyDispatch;
        this.strategyRepository = strategyRepository;
    }

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {
        //1、参数校验
        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(null == strategyId || userId.isEmpty()){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        //2、责任链过滤黑名单、权重积分门槛、默认抽奖，会产生一个chainfactory的strategyawardVO结果
        DefaultChainFactory.StrategyAwardVO strategyAwardVO = raffleLogicChain(userId,strategyId);
        log.info("抽奖策略计算：责任链{} {} {} {}",userId,strategyId,strategyAwardVO.getAwardId(),strategyAwardVO.getLogicModel());
        if(!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(strategyAwardVO.getLogicModel())){
            return RaffleAwardEntity.builder()
                    .awardId(strategyAwardVO.getAwardId())
                    .build();
        }
        //3、规则树过滤抽奖
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId,strategyId,strategyAwardVO.getAwardId());
        log.info("抽奖策略计算：规则树{} {} {} {}",userId,strategyId,treeStrategyAwardVO.getAwardId(),treeStrategyAwardVO.getAwardRuleValue());
        return RaffleAwardEntity.builder()
                .awardId(treeStrategyAwardVO.getAwardId())
                .strategyId(strategyId)
                .awardConfig(treeStrategyAwardVO.getAwardRuleValue())
                .build();







//        String[] proceedModels = ruleModels.splitProceedRuleModels();
//        RuleActionEntity<RuleActionEntity.RaffleProceedEntity> raffleProceedEntity= doCheckLogicProceedRaffle(RaffleFactorEntity.builder()
//                .strategyId(strategyId)
//                .awardId(awardId)
//                .userId(userId)
//                .build(), proceedModels);
//
//        if(RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(raffleProceedEntity.getCode())){
//            log.info("中奖中规则拦截，通过抽奖后规则rule_award_lucky走保底奖励");
//            return RaffleAwardEntity.builder()
//                    .awardDesc("中奖中规则拦截，通过抽奖后规则rule_award_lucky走保底奖励")
//                    .build();
//        }
//        return RaffleAwardEntity.builder()
//                .awardId(awardId)
//                .build();

    }

    /**
     * 抽奖计算，责任链抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

    /**
     * 抽奖结果过滤，决策树抽象方法
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 过滤结果【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
     */
    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);





}
