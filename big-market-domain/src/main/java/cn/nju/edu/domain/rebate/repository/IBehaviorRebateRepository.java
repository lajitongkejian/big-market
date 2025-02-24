package cn.nju.edu.domain.rebate.repository;

import cn.nju.edu.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import cn.nju.edu.domain.rebate.model.vo.BehaviorTypeVO;
import cn.nju.edu.domain.rebate.model.vo.DailyBehaviorRebateVO;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：IBehaviorRebateRepository
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：
 */
public interface IBehaviorRebateRepository {
    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorType);

    void saveUserRebateRecord(String userId, List<BehaviorRebateAggregate> behaviorRebateAggregates);
}
