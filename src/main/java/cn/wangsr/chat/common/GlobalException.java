package cn.wangsr.chat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wjl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    /**
     * 异常状态码
     */
    private Integer code;
    /**
     * 异常message
     */
    private String message;

}
