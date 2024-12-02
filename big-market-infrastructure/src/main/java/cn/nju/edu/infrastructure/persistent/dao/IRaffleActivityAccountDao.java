package cn.nju.edu.infrastructure.persistent.dao;

import cn.nju.edu.infrastructure.persistent.po.RaffleActivityAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名称：big-market
 * 类名称：IRaffleActivityAccountDao
 * 作者：tkj
 * 日期：2024/11/30
 * 描述：
 */
@Mapper
public interface IRaffleActivityAccountDao {
    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

    void insert(RaffleActivityAccount raffleActivityAccount);
}