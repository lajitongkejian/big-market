package cn.nju.edu.trigger.job;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.nju.edu.domain.task.model.entity.TaskEntity;
import cn.nju.edu.domain.task.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 项目名称：big-market
 * 类名称：SendMessageTaskJob
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */
@Slf4j
@Component
public class SendMessageTaskJob {

    @Resource
    private ITaskService taskService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private IDBRouterStrategy routerStrategy;


    @Scheduled(cron = "0/5 * * * * ?")
    public void exec(){

        try{
            int dbCount = routerStrategy.dbCount();
            for (int i = 1; i <= dbCount; i++) {
                //每个分库用线程池同时操作
                int finalI = i;
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            routerStrategy.setDBKey(finalI);
                            routerStrategy.setTBKey(0);
                            List<TaskEntity> list = taskService.queryNoSendMessageTaskList();
                            if(list.isEmpty()) return;
                            for (TaskEntity taskEntity : list) {
                                threadPoolExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            taskService.sendMessage(taskEntity);
                                            taskService.updateTaskSendMessageCompleted(taskEntity.getUserId(),taskEntity.getMessageId());
                                        }catch (Exception e){
                                            log.error("定时任务，发送MQ消息失败,userId:{},topic:{}",taskEntity.getUserId(),taskEntity.getTopic());
                                            taskService.updateTaskSendMessageFailed(taskEntity.getUserId(),taskEntity.getMessageId());
                                        }
                                    }
                                });
                            }
                        }finally {
                            routerStrategy.clear();
                        }
                    }
                });

            }


        }catch (Exception e){
            log.error("定时任务，扫描任务表发送消息失败",e);
        }finally {
            routerStrategy.clear();
        }
    }
}
