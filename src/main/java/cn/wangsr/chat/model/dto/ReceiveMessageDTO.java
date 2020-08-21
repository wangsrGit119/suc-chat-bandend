package cn.wangsr.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wjl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMessageDTO<T> implements Serializable {
    private Long userId;
    private Long targetId;
    private Integer targetType;
    private T data;
}
