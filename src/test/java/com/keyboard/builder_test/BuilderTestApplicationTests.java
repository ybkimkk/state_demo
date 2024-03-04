package com.keyboard.builder_test;

import com.alibaba.fastjson2.JSON;
import com.keyboard.builder_test.enums.OrderEvents;
import com.keyboard.builder_test.enums.OrderStatus;
import com.keyboard.builder_test.entity.common.R;
import com.keyboard.builder_test.entity.request.OrderRequestEntity;
import com.keyboard.builder_test.service.impl.OrderProcessor;
import com.keyboard.builder_test.strategy.OrderEventAction;
import com.keyboard.builder_test.strategy.impl.OrderPayEventAction;
import com.keyboard.builder_test.strategy.impl.OrderReceiveEventAction;
import com.keyboard.builder_test.strategy.impl.OrderShippingEventAction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
@Slf4j
class BuilderTestApplicationTests {

    @Autowired
    private OrderProcessor orderProcessor;

    //order strategy event
    @Autowired
    private OrderPayEventAction orderPayEventAction;

    @Autowired
    private OrderShippingEventAction orderShippingEventAction;

    @Autowired
    private OrderReceiveEventAction orderReceiveEventAction;



    @Test
    void normalTest() {
        OrderRequestEntity order = initRequest();
        if (OrderStatus.INIT.equals(order.getStatus()) && OrderEvents.PAY.equals(order.getEvent())) {
            //TODO PAY EVENT
            log.info(JSON.toJSONString(R.ok("pay action: request success!")));
        } else if (OrderStatus.PAYED.equals(order.getStatus()) && OrderEvents.SHIPPING.equals(order.getEvent())) {
            //TODO SHIPPING EVENT
            log.info(JSON.toJSONString(R.ok("shipping action: request success!")));
        } else if (OrderStatus.SHIPPED.equals(order.getStatus()) && OrderEvents.RECEIVE.equals(order.getEvent())) {
            //TODO RECEIVE EVENT
            log.info(JSON.toJSONString(R.ok("receive action: request success!")));
        } else {
            //TODO ERROR ACTION
            log.error(JSON.toJSONString(R.error("event has error")));
        }
    }

    @Test
    void stateMachineTest() {
        OrderRequestEntity order = initRequest();
        boolean process = orderProcessor.process(order, OrderEvents.PAY);
        if (process) {
            log.info(JSON.toJSONString(R.ok("request success!")));
        }
    }


    @Test
    void StrategyPatternTest() {
        Map<OrderEvents, OrderEventAction> orderStrategy = getOrderStrategy();
        OrderRequestEntity order = initRequest();
        OrderEventAction orderEventAction = orderStrategy.get(order.getEvent());
        if (Objects.isNull(orderEventAction)) {
            log.error("request fail!");
            return;
        }

        boolean process = orderEventAction.process();
        if (process) {
            log.info(JSON.toJSONString(R.ok("request success!")));
        }

    }

    private Map<OrderEvents, OrderEventAction> getOrderStrategy() {
        Map<OrderEvents, OrderEventAction> map = new HashMap<>();
        map.put(OrderEvents.PAY, orderPayEventAction);
        map.put(OrderEvents.SHIPPING, orderShippingEventAction);
        map.put(OrderEvents.RECEIVE, orderReceiveEventAction);
        return map;
    }


    private OrderRequestEntity initRequest() {
        OrderRequestEntity order = new OrderRequestEntity();
        order.setId(1L);
        order.setStatus(OrderStatus.INIT);
        order.setEvent(OrderEvents.PAY);
        return order;
    }

}
