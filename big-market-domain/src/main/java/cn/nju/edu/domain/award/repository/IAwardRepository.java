package cn.nju.edu.domain.award.repository;

import cn.nju.edu.domain.award.model.aggregate.UserAwardRecordAggregate;

/**
 * 项目名称：big-market
 * 类名称：IAwardRepository
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */
public interface IAwardRepository {

    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);
}
