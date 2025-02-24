package cn.nju.edu.domain.rebate.model.aggregate;

import cn.nju.edu.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import cn.nju.edu.domain.rebate.model.entity.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：BehaviorRebateAggregate
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：行为返利聚合对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BehaviorRebateAggregate {
    private String userId;
    private BehaviorRebateOrderEntity behaviorRebateOrder;
    private TaskEntity taskEntity;
}
