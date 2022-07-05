package com.wk.rabbitmq.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description : router key
 * @Author : wukong
 */
@AllArgsConstructor
@Getter
public enum RouterKeyEnum {
    DELAY_ROUTER_KEY("delayQueue.msg", "延迟消息"),
    DIRECT_ROUTER_KEY("directQueue.msg", "直连消息"),
    FANOUT_ROUTER_KEY("fanoutQueue.msg", "广播消息"),
    TOPIC_ROUTER_KEY("topic.#", "topic消息"),
    ;

    private String val;
    private String desc;
}
