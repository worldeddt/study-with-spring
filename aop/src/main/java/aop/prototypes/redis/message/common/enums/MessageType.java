package aop.prototypes.redis.message.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MessageType {
    CHAT, JOIN, LEAVE
}
