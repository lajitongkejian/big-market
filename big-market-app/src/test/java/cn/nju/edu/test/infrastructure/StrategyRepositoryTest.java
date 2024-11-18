package cn.nju.edu.test.infrastructure;

import cn.nju.edu.domain.strategy.model.vo.RuleTreeVO;
import cn.nju.edu.domain.strategy.repository.IStrategyRepository;
import cn.nju.edu.infrastructure.persistent.repository.StrategyRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 项目名称：big-market
 * 类名称：StrategyRepositoryTest
 * 作者：tkj
 * 日期：2024/11/18
 * 描述：
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRepositoryTest {

    @Resource
    private IStrategyRepository strategyRepository;

    @Test
    public void test_queryRuleTreeVOByTreeId(){
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId("tree_lock");
        log.info("测试结果：{}", JSON.toJSONString(ruleTreeVO.toString()));

    }
}
