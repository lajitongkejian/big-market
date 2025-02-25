package cn.nju.edu.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.nju.edu.infrastructure.persistent.po.RaffleActivityAccountMonth;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名称：big-market
 * 类名称：IRaffleActivityAccountMonthDao
 * 作者：tkj
 * 日期：2024/12/11
 * 描述：
 */
@Mapper
public interface IRaffleActivityAccountMonthDao {
    int updateActivityAccountMonthSubtractionQuota(RaffleActivityAccountMonth build);

    void insertActivityAccountMonth(RaffleActivityAccountMonth raffleActivityAccountMonth);

    @DBRouter
    RaffleActivityAccountMonth queryActivityAccountMonthByUserId(RaffleActivityAccountMonth raffleActivityAccountMonthReq);

    void addAccountQuota(RaffleActivityAccountMonth raffleActivityAccountMonth);
}
