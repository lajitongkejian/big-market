package cn.nju.edu.trigger.api;

import cn.nju.edu.trigger.api.dto.RaffleAwardListRequestDTO;
import cn.nju.edu.trigger.api.dto.RaffleAwardListResponseDTO;
import cn.nju.edu.trigger.api.dto.RaffleRequestDTO;
import cn.nju.edu.trigger.api.dto.RaffleResponseDTO;
import cn.nju.edu.types.model.Response;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：IRaffleService
 * 作者：tkj
 * 日期：2024/11/21
 * 描述：
 */
public interface IRaffleService {

    /**
     * 策略装配接口
     * @param strategyId
     * @return
     */

    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询抽奖奖品列表配置
     *
     * @param requestDTO 抽奖奖品列表查询请求参数
     * @return 奖品列表数据
     */

    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO requestDTO);
}
