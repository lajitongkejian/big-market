package cn.nju.edu.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import cn.nju.edu.infrastructure.persistent.po.UserAwardRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目名称：big-market
 * 类名称：IUserAwardRecordDao
 * 作者：tkj
 * 日期：2024/12/11
 * 描述：
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserAwardRecordDao {
    void insert(UserAwardRecord userAwardRecord);
}
