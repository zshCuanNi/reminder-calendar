package cc.kinami.template.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorInfoEnum {
    COMMON_ERROR(40000, "就是错了"),
    RHQ_HUNGRY(40001, "我饿了");

    private final Integer errCode;
    private final String errMsg;
}
