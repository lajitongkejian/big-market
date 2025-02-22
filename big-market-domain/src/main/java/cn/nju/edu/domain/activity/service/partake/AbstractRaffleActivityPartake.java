package cn.nju.edu.domain.activity.service.partake;

import cn.nju.edu.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import cn.nju.edu.domain.activity.model.entity.ActivityEntity;
import cn.nju.edu.domain.activity.model.entity.PartakeRaffleActivityEntity;
import cn.nju.edu.domain.activity.model.entity.UserRaffleOrderEntity;
import cn.nju.edu.domain.activity.model.vo.ActivityStateVO;
import cn.nju.edu.domain.activity.repository.IActivityRepository;
import cn.nju.edu.domain.activity.service.IRaffleActivityPartakeService;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.exception.AppException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 项目名称：big-market
 * 类名称：AbstractRaffleActivityPartake
 * 作者：tkj
 * 日期：2024/12/12
 * 描述：
 */
@Slf4j
public abstract class AbstractRaffleActivityPartake implements IRaffleActivityPartakeService {

    protected final IActivityRepository activityRepository;

    public AbstractRaffleActivityPartake(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public UserRaffleOrderEntity createOrder(PartakeRaffleActivityEntity partakeRaffleActivityEntity) {
        //1.基础信息
        String userId = partakeRaffleActivityEntity.getUserId();
        Long activityId = partakeRaffleActivityEntity.getActivityId();
        Date currentDate = new Date();
        //2.活动查询
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activityId);
        //判断活动是否开启
        if(!ActivityStateVO.open.getCode().equals(activityEntity.getState().getCode())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(),ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        //判断活动日期
        if(activityEntity.getBeginDateTime().after(currentDate) || activityEntity.getEndDateTime().before(currentDate)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(),ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        //3.查询未被使用的领取次数订单
        UserRaffleOrderEntity noUsedUserRaffleOrderEntity = activityRepository.queryNoUsedRaffleOrder(partakeRaffleActivityEntity);
        if(null != noUsedUserRaffleOrderEntity) {
            log.info("已存在未使用的抽奖次数领取订单userId:{},activityId:{},userRaffleOrderEntity:{}", userId, activityId, JSON.toJSONString(noUsedUserRaffleOrderEntity));
            return noUsedUserRaffleOrderEntity;
        }
        //4.账户额度过滤
        CreatePartakeOrderAggregate createPartakeOrderAggregate = doFilterAccount(userId,activityId,currentDate);

        //5.创建订单
        UserRaffleOrderEntity userRaffleOrderEntity = buildUserRaffleOrder(userId,activityId,currentDate);

        //6.填充聚合对象
        createPartakeOrderAggregate.setUserRaffleOrderEntity(userRaffleOrderEntity);

        //7.持久化聚合对象
        activityRepository.saveCreatePartakeOrderAggregate(createPartakeOrderAggregate);

        return userRaffleOrderEntity;


    }

    @Override
    public UserRaffleOrderEntity createOrder(String userId, Long activityId) {
        return createOrder(PartakeRaffleActivityEntity.builder()
                .activityId(activityId)
                .userId(userId)
                .build());
    }

    protected abstract CreatePartakeOrderAggregate doFilterAccount(String userId, Long activityId, Date currentDate);

    protected abstract UserRaffleOrderEntity buildUserRaffleOrder(String userId,Long activityId,Date currentDate);


}
