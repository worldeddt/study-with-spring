package aop.prototypes.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum MessageType {
    CHAT, JOIN, LEAVE
}
