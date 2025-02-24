package cn.nju.edu.domain.rebate.model.entity;

import cn.nju.edu.domain.rebate.model.vo.BehaviorTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：BehaviorEntity
 * 作者：tkj
 * 日期：2025/2/24
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BehaviorEntity {
    private String userId;

    private BehaviorTypeVO behaviorType;

    private String outBusinessNo;
}
