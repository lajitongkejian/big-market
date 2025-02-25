package cn.nju.edu.test.infrastructure.activity;

import cn.nju.edu.domain.activity.model.entity.ActivityOrderEntity;
import cn.nju.edu.domain.activity.model.entity.ActivityShopCartEntity;
import cn.nju.edu.domain.activity.model.entity.SkuRechargeEntity;
import cn.nju.edu.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.nju.edu.domain.activity.service.armory.IActivityArmory;
import cn.nju.edu.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * 项目名称：big-market
 * 类名称：RaffleOrderTest
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleOrderTest {

    @Resource
    private IRaffleActivityAccountQuotaService raffleOrder;

    @Resource
    private IActivityArmory activityArmory;

    @Before
    public void setUp() throws Exception {
        log.info("装配活动：{}",activityArmory.assembleActivitySku(9011L));
    }

    @Test
    public void test_createRaffleActivityOrder() {
        ActivityShopCartEntity activityShopCartEntity = new ActivityShopCartEntity();
        activityShopCartEntity.setUserId("tkj");
        activityShopCartEntity.setSku(9011L);
        ActivityOrderEntity raffleActivityOrder = raffleOrder.createRaffleActivityOrder(activityShopCartEntity);
        log.info("测试结果：{}", JSON.toJSONString(raffleActivityOrder));
    }

    @Test
    public void test_createSkuRechargeOrder_duplicate() {
        SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
        skuRechargeEntity.setUserId("xiaofuge");
        skuRechargeEntity.setSku(9011L);
        // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
        skuRechargeEntity.setOutBusinessNo("700091009112");
        String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
        log.info("测试结果：{}", orderId);
    }

    @Test
    public void test_createSkuRechargeOrder() throws InterruptedException{
        for (int i = 0; i < 20; i++) {
            try {
                SkuRechargeEntity skuRechargeEntity = new SkuRechargeEntity();
                skuRechargeEntity.setUserId("xiaofuge");
                skuRechargeEntity.setSku(9011L);
                // outBusinessNo 作为幂等仿重使用，同一个业务单号2次使用会抛出索引冲突 Duplicate entry '700091009111' for key 'uq_out_business_no' 确保唯一性。
                skuRechargeEntity.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
                String orderId = raffleOrder.createSkuRechargeOrder(skuRechargeEntity);
                log.info("测试结果：{}", orderId);
            } catch (AppException e) {
                log.warn(e.getInfo());
            }
        }

        new CountDownLatch(1).await();

    }



}

