package cn.nju.edu.test.domain;

import cn.nju.edu.domain.strategy.model.entity.RaffleAwardEntity;
import cn.nju.edu.domain.strategy.model.entity.RaffleFactorEntity;
import cn.nju.edu.domain.strategy.service.IRaffleStrategy;
import cn.nju.edu.domain.strategy.service.armory.IStrategyArmory;
import cn.nju.edu.domain.strategy.service.armory.IStrategyDispatch;
import cn.nju.edu.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import cn.nju.edu.domain.strategy.service.rule.filter.impl.RuleLockLogicFilter;
import cn.nju.edu.domain.strategy.service.rule.tree.impl.RuleLuckAwardLogicTreeNode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * 项目名称：big-market
 * 类名称：RaffleStrategyTest
 * 作者：tkj
 * 日期：2024/11/13
 * 描述：
 */


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;

    @Resource
    private RuleLockLogicFilter ruleLockLogicFilter;

    @Resource
    private RuleLuckAwardLogicTreeNode ruleLuckAwardLogicTreeNode;

    @Before
    public void test_setUp(){
//        strategyArmony.assembleLotteryStrategy(100001L);
        ReflectionTestUtils.setField(ruleWeightLogicChain,"userPoints",4900L);
        ReflectionTestUtils.setField(ruleLockLogicFilter,"userRaffleCount",10L);
//        strategyArmory.assembleLotteryStrategy2(100001L);
//        strategyArmory.assembleLotteryStrategy2(100002L);
//        strategyArmory.assembleLotteryStrategy2(100003L);
        strategyArmory.assembleLotteryStrategy2(100006L);
    }

    @Test
    public void test_performRaffle(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("abc")
                .strategyId(100001L)
                .build();

        log.info("测试参数：{}", JSON.toJSONString(raffleFactorEntity));

//        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
//        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
        int cnt = 0;
        for (int i = 0; i < 100; i++) {
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
            if(raffleAwardEntity.getAwardId()==102) cnt++;
            log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
        }
        log.info("102一共被抽到{}次",cnt);

    }

    @Test
    public void test_performRaffle2(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user001")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("测试参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle3(){
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("")
                .strategyId(100001L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("测试参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle4() throws InterruptedException {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .strategyId(100006L)
                .userId("abc")
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("测试参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
        new CountDownLatch(1).await();
    }

}
