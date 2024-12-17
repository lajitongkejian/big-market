package cn.nju.edu.domain.award.model.aggregate;

import cn.nju.edu.domain.award.model.entity.TaskEntity;
import cn.nju.edu.domain.award.model.entity.UserAwardRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：UserAwardRecordAggregate
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：中奖记录写入的一次事务对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRecordAggregate {
    private UserAwardRecordEntity userAwardRecordEntity;
    private TaskEntity taskEntity;
}
