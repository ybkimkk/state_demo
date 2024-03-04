package com.keyboard.builder_test.config;


import com.keyboard.builder_test.entity.request.OrderRequestEntity;
import com.keyboard.builder_test.enums.OrderEvents;
import com.keyboard.builder_test.enums.OrderStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;

/**
 * @author jinyongbin
 * @version 1.0
 * @since 2024/3/2
 */
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.INIT)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStatus.INIT)  //order source state
                .target(OrderStatus.PAYED) //order target state
                .event(OrderEvents.PAY) //order trigger event
                .and()
                .withExternal()
                .source(OrderStatus.PAYED)
                .target(OrderStatus.SHIPPED)
                .event(OrderEvents.SHIPPING)
                .and()
                .withExternal()
                .source(OrderStatus.SHIPPED)
                .target(OrderStatus.RECEIVED)
                .event(OrderEvents.RECEIVE);
    }

    @Bean
    public DefaultStateMachinePersister<OrderStatus, OrderEvents, OrderRequestEntity> persister() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<OrderStatus, OrderEvents, OrderRequestEntity>() {
            @Override
            public void write(StateMachineContext<OrderStatus, OrderEvents> stateMachineContext, OrderRequestEntity order) throws Exception {

            }

            @Override
            public StateMachineContext<OrderStatus, OrderEvents> read(OrderRequestEntity order) throws Exception {
                return new DefaultStateMachineContext<>(order.getStatus(), null, null, null);
            }
        });
    }
}
