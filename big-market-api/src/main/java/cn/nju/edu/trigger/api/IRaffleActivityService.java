package cn.nju.edu.trigger.api;

import cn.nju.edu.trigger.api.dto.ActivityDrawRequestDTO;
import cn.nju.edu.trigger.api.dto.ActivityDrawResponseDTO;
import cn.nju.edu.trigger.api.dto.UserActivityAccountRequestDTO;
import cn.nju.edu.trigger.api.dto.UserActivityAccountResponseDTO;
import cn.nju.edu.types.model.Response;

/**
 * 项目名称：big-market
 * 类名称：IRaffleActivityService
 * 作者：tkj
 * 日期：2025/2/22
 * 描述：
 */
public interface IRaffleActivityService {
    /**
     * 活动装配，数据预热缓存
     * @param activityId 活动ID
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     * @param request 请求对象
     * @return 返回结果
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    /**
     * 日历签到返利接口
     *
     * @param userId 用户ID
     * @return 签到结果
     */
    public Response<Boolean> calendarSignRebate(String userId);

    /**
     * 日历签到返利接口是否返利
     *
     * @param userId 用户ID
     * @return 签到结果
     */
    public Response<Boolean> isCalendarSignRebate(String userId);


    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO request);

}
