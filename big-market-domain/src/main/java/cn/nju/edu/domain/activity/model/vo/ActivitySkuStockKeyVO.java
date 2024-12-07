package cn.nju.edu.domain.activity.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：ActivitySkuStockKeyVO
 * 作者：tkj
 * 日期：2024/12/5
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivitySkuStockKeyVO {
    private Long sku;
    private Long activityId;
}
