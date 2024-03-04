package com.keyboard.builder_test.service.impl;

import com.keyboard.builder_test.enums.OrderEvents;
import com.keyboard.builder_test.enums.OrderStatus;
import com.keyboard.builder_test.entity.request.OrderRequestEntity;
import lombok.SneakyThrows;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jinyongbin
 * @version 1.0
 * @since 2024/3/2
 */
@Component("OrderProcessor")
public class OrderProcessor {
    @Resource
    private StateMachine<OrderStatus, OrderEvents> orderstateMachine;

    @Resource
    private StateMachinePersister<OrderStatus, OrderEvents, OrderRequestEntity> persist;

    public boolean process(OrderRequestEntity order, OrderEvents events) {
        Message<OrderEvents> message = MessageBuilder.withPayload(events)
                .setHeader("order", order).build();
        return sendEvent(message);
    }

    @SneakyThrows
    private boolean sendEvent(Message<OrderEvents> message) {
        OrderRequestEntity order = (OrderRequestEntity) message.getHeaders().get("order");
        persist.restore(orderstateMachine, order);
        return orderstateMachine.sendEvent(message);
    }
}
