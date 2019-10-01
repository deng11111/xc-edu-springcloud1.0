package com.xuecheng.learning.service;

import com.google.common.base.Preconditions;
import com.xuecheng.framework.domain.task.XcTask;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2018/5/29.
 */
@Service
public class TaskService {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    //发送消息
    public void publish(XcTask task, String exchange, String routeKey, CorrelationData correlationData) {
        Preconditions.checkNotNull(task);
        Preconditions.checkNotNull(exchange);
        Preconditions.checkNotNull(routeKey);
        rabbitTemplate.convertAndSend(exchange, routeKey, task, correlationData);
    }


}
