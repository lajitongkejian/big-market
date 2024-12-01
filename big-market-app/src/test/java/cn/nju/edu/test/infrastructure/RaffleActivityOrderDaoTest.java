package cn.nju.edu.test.infrastructure;

import cn.nju.edu.infrastructure.persistent.dao.IRaffleActivityOrderDao;
import cn.nju.edu.infrastructure.persistent.po.RaffleActivity;
import cn.nju.edu.infrastructure.persistent.po.RaffleActivityOrder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：RaffleActivityOrderDaoTest
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityOrderDaoTest {
    @Resource
    private IRaffleActivityOrderDao raffleActivityOrderDao;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    public void test_insert(){
        RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
        raffleActivityOrder.setActivityId(100301L);
        raffleActivityOrder.setUserId("tkj");
        raffleActivityOrder.setActivityName("测试活动");
        raffleActivityOrder.setStrategyId(100006L);
        raffleActivityOrder.setOrderTime(new Date());
        raffleActivityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
        raffleActivityOrder.setState("not_used");
        raffleActivityOrderDao.insert(raffleActivityOrder);
    }

    @Test
    public void test_random_insert(){
        for (int i = 0; i < 5; i++) {
            RaffleActivityOrder raffleActivityOrder = new RaffleActivityOrder();
            raffleActivityOrder.setActivityId(100301L);
            raffleActivityOrder.setUserId(easyRandom.nextObject(String.class));
            raffleActivityOrder.setActivityName("测试活动");
            raffleActivityOrder.setStrategyId(100006L);
            raffleActivityOrder.setOrderTime(new Date());
            raffleActivityOrder.setOrderId(RandomStringUtils.randomNumeric(12));
            raffleActivityOrder.setState("not_used");
            raffleActivityOrderDao.insert(raffleActivityOrder);
        }
    }

    @Test
    public void test_queryRaffleActivityOrderByUserId(){
        List<RaffleActivityOrder> list = raffleActivityOrderDao.queryRaffleActivityOrderByUserId("tkj");
        log.info("测试结果{}", JSON.toJSONString(list));
    }
}
