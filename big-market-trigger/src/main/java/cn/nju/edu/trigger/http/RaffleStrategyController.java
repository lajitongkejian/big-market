package cn.nju.edu.trigger.http;

import cn.nju.edu.domain.activity.service.IRaffleActivityAccountQuotaService;
import cn.nju.edu.domain.strategy.model.entity.RaffleAwardEntity;
import cn.nju.edu.domain.strategy.model.entity.RaffleFactorEntity;
import cn.nju.edu.domain.strategy.model.entity.StrategyAwardEntity;
import cn.nju.edu.domain.strategy.service.IRaffleRule;
import cn.nju.edu.domain.strategy.service.IRaffleStrategy;
import cn.nju.edu.domain.strategy.service.armory.IStrategyArmory;
import cn.nju.edu.domain.strategy.service.rule.IRaffleAward;
import cn.nju.edu.trigger.api.IRaffleStrategyService;
import cn.nju.edu.trigger.api.dto.RaffleAwardListRequestDTO;
import cn.nju.edu.trigger.api.dto.RaffleAwardListResponseDTO;
import cn.nju.edu.trigger.api.dto.RaffleStrategyRequestDTO;
import cn.nju.edu.trigger.api.dto.RaffleStrategyResponseDTO;
import cn.nju.edu.types.enums.ResponseCode;
import cn.nju.edu.types.model.Response;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：big-market
 * 类名称：IRaffleController
 * 作者：tkj
 * 日期：2024/11/21
 * 描述：
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/strategy")
public class RaffleStrategyController implements IRaffleStrategyService {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private IRaffleAward  raffleAward;

    @Resource
    private IRaffleRule raffleRule;

    @Resource
    private IRaffleActivityAccountQuotaService accountQuotaService;


    @RequestMapping(value = "strategy_armory",method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyArmory(Long strategyId) {
        try{
            log.info("策略装配开始，strategyId:{}",strategyId);
            boolean armory = strategyArmory.assembleLotteryStrategy2(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armory)
                    .build();
            log.info("策略装配完成，strategyId:{},response:{}",strategyId, JSON.toJSONString(response));
            return response;
        }catch (Exception e){
            log.error("策略装配失败，strategyId：{},message:{}",strategyId,e.getMessage());
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }


    @RequestMapping(value = "query_raffle_award_list",method = RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO requestDTO) {
        try{
            log.info("查询奖品列表开始，userId:{} activityId:{}",requestDTO.getUserId(),requestDTO.getActivityId());
            //1.参数校验
            if(StringUtils.isBlank(requestDTO.getUserId()) || null == requestDTO.getActivityId()){
                return Response.<List<RaffleAwardListResponseDTO>>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }
            List<StrategyAwardEntity> list = raffleAward.queryStrategyAwardByActivityId(requestDTO.getActivityId());
            //2.获取ruleModel
            String[] treeIds = list.stream().map(StrategyAwardEntity::getRuleModels)
                    .filter(ruleModel -> ruleModel!=null && !ruleModel.isEmpty())
                    .toArray(String[]::new);
            //3.查询规则配置
            Map<String,Integer> ruleLockCountMap = raffleRule.queryAwardRuleLockCount(treeIds);
            //4.查询抽奖次数，用户已经参与了多少次抽奖
            Integer dayPartakeCount = accountQuotaService.queryRaffleActivityAccountDayPartakeCount(requestDTO.getActivityId(),requestDTO.getUserId());
            //5.遍历填充数据
            List<RaffleAwardListResponseDTO> res = new ArrayList<>();
            for (StrategyAwardEntity strategyAwardEntity : list) {
                Integer awardRuleLockCount = ruleLockCountMap.get(strategyAwardEntity.getRuleModels());
                RaffleAwardListResponseDTO responseDTO = RaffleAwardListResponseDTO.builder()
                                  .awardId(strategyAwardEntity.getAwardId())
                                  .awardTitle(strategyAwardEntity.getAwardTitle())
                                  .awardSubtitle(strategyAwardEntity.getAwardSubtitle())
                                  .sort(strategyAwardEntity.getSort())
                                  .awardRuleLockCount(awardRuleLockCount)
                                  .isAwardUnlock(null == awardRuleLockCount || dayPartakeCount > awardRuleLockCount)
                        .waitUnlockCount(null == awardRuleLockCount || awardRuleLockCount <= dayPartakeCount ? 0:awardRuleLockCount-dayPartakeCount)
                                  .build();
                res.add(responseDTO);

            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .data(res)
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();
            log.info("策略装配完成，userId:{} activityId:{} response:{}",requestDTO.getUserId(),requestDTO.getActivityId(),JSON.toJSONString(response));
            return response;
        }catch (Exception e){
            log.error("查询奖品列表失败，userId:{} activityId:{}",requestDTO.getUserId(),requestDTO.getActivityId());
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }


    @RequestMapping(value = "random_raffle",method = RequestMethod.POST)
    @Override
    public Response<RaffleStrategyResponseDTO> randomRaffle(@RequestBody RaffleStrategyRequestDTO requestDTO) {
        try {
            log.info("随机抽奖开始，strategyId:{}",requestDTO.getStrategyId());
            RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                    .userId("system")
                    .strategyId(requestDTO.getStrategyId())
                    .build();
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
            RaffleStrategyResponseDTO raffleResponseDTO = RaffleStrategyResponseDTO.builder()
                    .awardId(raffleAwardEntity.getAwardId())
                    .awardIndex(raffleAwardEntity.getSort())
                    .build();
            Response<RaffleStrategyResponseDTO> response = Response.<RaffleStrategyResponseDTO>builder()
                    .data(raffleResponseDTO)
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();
            log.info("随机抽奖完成，strategyId:{},response:{}",requestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;
        }catch (Exception e){
            log.error("随机抽奖失败，strategyId:{}",requestDTO.getStrategyId(),e);
            return Response.<RaffleStrategyResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
