package cn.nju.edu.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.nju.edu.infrastructure.persistent.po.RaffleActivityAccountDay;
import cn.nju.edu.infrastructure.persistent.po.RaffleActivityAccountMonth;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名称：big-market
 * 类名称：IRaffleActivityAccountDayDao
 * 作者：tkj
 * 日期：2024/12/11
 * 描述：
 */
@Mapper
public interface IRaffleActivityAccountDayDao {
    

    void insertActivityAccountDay(RaffleActivityAccountDay raffleActivityAccountDay);


    int updateActivityAccountDaySubtractionQuota(RaffleActivityAccountDay build);

    @DBRouter
    RaffleActivityAccountDay queryActivityAccountDayByUserId(RaffleActivityAccountDay raffleActivityAccountDayReq);

    @DBRouter
    Integer queryRaffleActivityAccountDayPartakeCount(RaffleActivityAccountDay raffleActivityAccountDay);

    void addAccountQuota(RaffleActivityAccountDay raffleActivityAccountDay);
}
