package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.LearningService;
import com.xuecheng.learning.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by mrt on 2018/5/30.
 */
@Component
public class ChooseCourseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    LearningService learningService;
    @Autowired
    TaskService taskService;
    /**
     * 接收选课任务
     */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE})
    public void receiveChoosecourseTask(XcTask task,Message message, Channel channel) throws IOException {
        LOGGER.info("receiveChoosecourseTask...{}",task.getId());
        //接收到 的消息id
        String id = task.getId();
        //响应的消息id
        CorrelationData correlationData = new CorrelationData(id);
        //添加选课
        try {
            String requestBody = task.getRequestBody();
            Map map = JSON.parseObject(requestBody, Map.class);
            String userId = (String) map.get("userId");
            String courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            Date startTime = null;
            Date endTime = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            if(map.get("startTime")!=null){
                startTime =dateFormat.parse((String) map.get("startTime"));
            }
            if(map.get("endTime")!=null){
                endTime =dateFormat.parse((String) map.get("endTime"));
            }


            ResponseResult addcourse = learningService.addcourse(userId, courseId, valid,startTime, endTime);
            Preconditions.checkNotNull(addcourse);
            if(addcourse.isSuccess()){
                //发送响应消息
                taskService.publish(task, RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,RabbitMQConfig.xc_learning_finishaddchoosecourse_key,correlationData);
                LOGGER.info("RECEIVE msg id is {} of queue {} process success",id,RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("RECEIVE msg id is {} of queue {} process fail",id,RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE);
        }

    }
}
