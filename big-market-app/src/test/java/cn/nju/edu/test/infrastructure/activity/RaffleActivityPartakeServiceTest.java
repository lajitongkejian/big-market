package cn.nju.edu.test.infrastructure.activity;

import cn.nju.edu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import cn.nju.edu.domain.activity.model.entity.UserRaffleOrderEntity;
import cn.nju.edu.domain.activity.service.IRaffleActivityPartakeService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：RaffleActivityPartakeServiceTest
 * 作者：tkj
 * 日期：2024/12/12
 * 描述：
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest

public class RaffleActivityPartakeServiceTest {
    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;

    @Test
    public void test_createOrder() {
        // 请求参数
        PartakeRaffleActivityEntity partakeRaffleActivityEntity = new PartakeRaffleActivityEntity();
        partakeRaffleActivityEntity.setUserId("xiaofuge");
        partakeRaffleActivityEntity.setActivityId(100301L);
        // 调用接口
        UserRaffleOrderEntity userRaffleOrder = raffleActivityPartakeService.createOrder(partakeRaffleActivityEntity);
        log.info("请求参数：{}", JSON.toJSONString(partakeRaffleActivityEntity));
        log.info("测试结果：{}", JSON.toJSONString(userRaffleOrder));
    }

}
