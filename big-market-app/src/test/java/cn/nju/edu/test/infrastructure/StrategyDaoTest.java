package cn.nju.edu.test.infrastructure;

import cn.nju.edu.infrastructure.persistent.dao.IAwardDao;
import cn.nju.edu.infrastructure.persistent.dao.IStrategyDao;
import cn.nju.edu.infrastructure.persistent.po.Award;
import cn.nju.edu.infrastructure.persistent.po.Strategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：AwardDaoTest
 * 作者：tkj
 * 日期：2024/11/9
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyDaoTest {

    @Resource
    private IStrategyDao strategyDao;

    @Test
    public void test_queryStrategyList(){
        List<Strategy> list = strategyDao.queryStrategyList();
        log.info("测试结果:{}", JSON.toJSONString(list));
    }
}
