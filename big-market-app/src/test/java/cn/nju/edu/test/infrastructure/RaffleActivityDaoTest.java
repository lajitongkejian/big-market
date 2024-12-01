package cn.nju.edu.test.infrastructure;

import cn.nju.edu.infrastructure.persistent.dao.IRaffleActivityDao;
import cn.nju.edu.infrastructure.persistent.po.RaffleActivity;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：RaffleActivityDaoTest
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleActivityDaoTest {

    @Resource
    IRaffleActivityDao  raffleActivityDao;
    @Test
    public void test_queryRaffleActivityByActivityId(){
        RaffleActivity raffleActivity = raffleActivityDao.queryRaffleActivityByActivityId(100301L);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivity));
    }
}
