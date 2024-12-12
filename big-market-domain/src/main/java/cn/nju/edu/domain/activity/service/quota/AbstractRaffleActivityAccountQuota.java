package cn.nju.edu.domain.activity.service.quota;

import cn.nju.edu.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import cn.nju.edu.domain.activity.model.entity.*;
import cn.nju.edu.domain.activity.repository.IActivityRepository;
import cn.nju.edu.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.nju.edu.domain.activity.service.IRaffleActivitySkuStockService;
import cn.nju.edu.domain.activity.service.quota.rule.IActionChain;
import cn.nju.edu.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 项目名称：big-market
 * 类名称：AbstractRaffleActivity
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：定义了发布抽奖次数（模拟商品下单处理）的流程模板
 */
@Slf4j
public abstract class AbstractRaffleActivityAccountQuota extends RaffleActivityAccountQuotaSupport implements IRaffleActivityAccountQuotaService, IRaffleActivitySkuStockService {

    public AbstractRaffleActivityAccountQuota(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        super(activityRepository, defaultActivityChainFactory);
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        // 1. 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        // 2. 查询活动信息
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        // 3. 查询次数信息（用户在活动上可参与的次数）
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }

    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        //1.参数校验，验证skuRechargeEntity参数是否为空
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if(null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        //2.查询基础信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        //3.活动规则校验
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        boolean success = actionChain.action(activityCountEntity,activitySkuEntity,activityEntity);
        //这里暂时先不对success做判断处理
        //4.生成订单聚合类
        CreateQuotaOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity,activityCountEntity,activitySkuEntity,activityEntity);
        //5.保存订单
        doSave(createOrderAggregate);
        //6.返回单号
        return createOrderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract void doSave(CreateQuotaOrderAggregate createOrderAggregate);


    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivityCountEntity activityCountEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity);


}

