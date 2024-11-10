package cn.nju.edu.test.domain;

import cn.nju.edu.domain.strategy.service.armory.IStrategyArmory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：StrategyArmonyTest
 * 作者：tkj
 * 日期：2024/11/10
 * 描述：
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmonyTest {

    @Resource
    private IStrategyArmory strategyArmony;

    @Test
    public void test_strategyArmony(){
//        strategyArmony.assembleLotteryStrategy(100001L);
        strategyArmony.assembleLotteryStrategy2(100001L);
    }

    @Test
    public void test_getRandomAwardId(){
        int cnt1 = 0;
        for (int i = 0; i < 100; i++) {
            Integer id = strategyArmony.getRandomAwardId2(100001L);
            log.info("抽奖测试：奖品id：{}",id);
            if(id==101) cnt1++;
        }
        log.info("101被抽到了{}次",cnt1);


    }
}
