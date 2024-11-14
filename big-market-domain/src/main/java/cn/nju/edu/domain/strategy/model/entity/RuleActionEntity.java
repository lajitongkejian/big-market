package cn.nju.edu.domain.strategy.model.entity;

import cn.nju.edu.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import lombok.*;

/**
 * 项目名称：big-market
 * 类名称：RuleActionEntity
 * 作者：tkj
 * 日期：2024/11/12
 * 描述：
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();

    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();

    private String ruleModel;

    private T data;



    static public class RaffleEntity{

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @EqualsAndHashCode(callSuper = true)
    static public class RaffleBeforeEntity extends RaffleEntity{
        /**
         * 抽奖策略ID
         */
        private Long strategyId;
        /**
         * 权重值key
         */
        private String ruleWeightValueKey;

        /**
         * 抽奖奖品ID - 内部流转使用
         */
        private Integer awardId;
    }
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    @EqualsAndHashCode(callSuper = true)
    static public class RaffleAfterEntity extends RaffleEntity{

    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    @EqualsAndHashCode(callSuper = true)
    static public class RaffleProceedEntity extends RaffleEntity{


    }
}
