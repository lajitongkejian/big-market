package cn.nju.edu.domain.rebate.service;

import cn.nju.edu.domain.rebate.model.entity.BehaviorEntity;
import cn.nju.edu.domain.rebate.model.entity.BehaviorRebateOrderEntity;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：IBehaviorRebateService
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：行为返利服务接口
 */
public interface IBehaviorRebateService {

    List<String> createOrder(BehaviorEntity behaviorEntity);

    List<BehaviorRebateOrderEntity> queryOrderByOutBusinessNo(String businessNo,String userId);
}
