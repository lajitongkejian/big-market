package cn.nju.edu.domain.activity.service;

import cn.nju.edu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import cn.nju.edu.domain.activity.model.entity.UserRaffleOrderEntity;

/**
 * 项目名称：big-market
 * 类名称：IRaffleActivityPartakeService
 * 作者：tkj
 * 日期：2024/12/12
 * 描述：抽奖次数领取活动参与，对用户账户进行发放抽奖次数操作
 */
public interface IRaffleActivityPartakeService {
    /**
     * 创建抽奖单；用户参与抽奖活动，扣减活动账户库存，产生抽奖单。如存在未被使用的抽奖单则直接返回已存在的抽奖单。
     *
     * @param partakeRaffleActivityEntity 参与抽奖活动实体对象
     * @return 用户抽奖订单实体对象
     */

    UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity);
}
