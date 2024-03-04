package com.keyboard.builder_test.service.impl;

import com.keyboard.builder_test.enums.OrderEvents;
import com.keyboard.builder_test.enums.OrderStatus;
import com.keyboard.builder_test.entity.request.OrderRequestEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author jinyongbin
 * @version 1.0
 * @since 2024/3/2
 */
@Component("OrderStateListener")
@Slf4j
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {

    @OnTransition(source = "INIT", target = "PAYED")
    public boolean pay(Message<OrderEvents> message) {
        //TODO PAY ENVENT
        OrderRequestEntity order = (OrderRequestEntity) message.getHeaders().get("order");
        order.setStatus(OrderStatus.PAYED);
        log.info("do pay event success");
        return true;
    }

    @OnTransition(source = "PAYED", target = "SHIPPED")
    public boolean shipping(Message<OrderEvents> message) {
        //TODO SHIPPING ENVENT
        OrderRequestEntity order = (OrderRequestEntity) message.getHeaders().get("order");
        order.setStatus(OrderStatus.SHIPPED);
        log.info("do shipping event success");
        return true;
    }

    @OnTransition(source = "SHIPPED", target = "RECEIVED")
    public boolean receive(Message<OrderEvents> message) {
        //TODO RECEIVE ENVENT
        OrderRequestEntity order = (OrderRequestEntity) message.getHeaders().get("order");
        order.setStatus(OrderStatus.RECEIVED);
        log.info("do receive event success");
        return true;
    }

}
