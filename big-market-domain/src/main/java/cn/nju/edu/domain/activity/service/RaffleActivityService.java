package cn.nju.edu.domain.activity.service;

import cn.nju.edu.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * 项目名称：big-market
 * 类名称：RaffleActivityService
 * 作者：tkj
 * 日期：2024/12/1
 * 描述：
 */
@Service
public class RaffleActivityService extends AbstractRaffleActivity {

    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

}

