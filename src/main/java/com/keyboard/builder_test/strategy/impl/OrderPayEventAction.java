package com.keyboard.builder_test.strategy.impl;

import com.keyboard.builder_test.strategy.OrderEventAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jinyongbin
 * @version 1.0
 * @since 2024/3/4
 */
@Component
@Slf4j
public class OrderPayEventAction implements OrderEventAction {
    @Override
    public boolean process() {
        //TODO PAY ENVENT
        log.info("do pay event success");
        return true;
    }
}
