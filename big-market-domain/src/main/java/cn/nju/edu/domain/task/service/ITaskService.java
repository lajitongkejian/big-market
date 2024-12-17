package cn.nju.edu.domain.task.service;

import cn.nju.edu.domain.task.model.entity.TaskEntity;

import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：ITaskService
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */
public interface ITaskService {
    List<TaskEntity> queryNoSendMessageTaskList();

    void sendMessage(TaskEntity task);

    void updateTaskSendMessageCompleted(String userId,String messageId);

    void updateTaskSendMessageFailed(String userId,String messageId);
}
