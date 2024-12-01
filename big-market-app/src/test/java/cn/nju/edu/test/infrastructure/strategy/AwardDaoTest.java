package cn.nju.edu.test.infrastructure.strategy;

import cn.nju.edu.infrastructure.persistent.dao.IAwardDao;
import cn.nju.edu.infrastructure.persistent.po.Award;
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
public class AwardDaoTest {

    @Resource
    private IAwardDao awardDao;

    @Test
    public void test_queryAwardList(){
        List<Award> list = awardDao.queryAwardList();
        log.info("测试结果:{}", JSON.toJSONString(list));
    }
}
