package cn.nju.edu.domain.task.service;

import cn.nju.edu.domain.task.model.entity.TaskEntity;
import cn.nju.edu.domain.task.repository.ITaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 项目名称：big-market
 * 类名称：TaskService
 * 作者：tkj
 * 日期：2024/12/17
 * 描述：
 */
@Service
public class TaskService implements ITaskService {

    @Resource
    private ITaskRepository taskRepository;

    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public void sendMessage(TaskEntity task) {
        taskRepository.sendMessage(task);
    }

    @Override
    public void updateTaskSendMessageCompleted(String userId, String messageId) {

        taskRepository.updateTaskSendMessageCompleted(userId,messageId);
    }

    @Override
    public void updateTaskSendMessageFailed(String userId, String messageId) {
        taskRepository.updateTaskSendMessageFailed(userId,messageId);
    }
}
