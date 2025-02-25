package cn.nju.edu.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称：big-market
 * 类名称：RaffaleAwardListResponseDTO
 * 作者：tkj
 * 日期：2024/11/21
 * 描述：奖品列表返回结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleAwardListResponseDTO {

    // 奖品ID
    private Integer awardId;
    // 奖品标题
    private String awardTitle;
    // 奖品副标题【抽奖1次后解锁】
    private String awardSubtitle;
    // 排序编号
    private Integer sort;
    // 奖品次数规则，抽奖n次后解锁，未配置则没有
    private Integer awardRuleLockCount;
    // 奖品是否解锁
    private Boolean isAwardUnlock;
    // 等待解锁次数 = n次解锁-已经抽奖次数
    private Integer waitUnlockCount;

}
