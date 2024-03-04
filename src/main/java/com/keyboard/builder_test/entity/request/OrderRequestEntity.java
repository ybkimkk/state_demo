package com.keyboard.builder_test.entity.request;

import com.keyboard.builder_test.enums.OrderEvents;
import com.keyboard.builder_test.enums.OrderStatus;
import lombok.Data;


/**
 * @author jinyongbin
 * @since  2024-03-02 21:00:55
 */

@Data
public class OrderRequestEntity {
    private Long id;
    private OrderStatus status;
    private OrderEvents event;
}



