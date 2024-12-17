package cn.nju.edu.domain.award.service;

import cn.nju.edu.domain.award.model.entity.UserAwardRecordEntity;

/**
 * 项目名称：big-market
 * 类名称：IAwardService
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：中奖记录操作
 */
public interface IAwardService {
    void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);
}
