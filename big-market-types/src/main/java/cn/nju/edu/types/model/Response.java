package cn.nju.edu.types.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 项目名称：big-market
 * 类名称：Response
 * 作者：tkj
 * 日期：2024/11/21
 * 描述：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response<T> implements Serializable {
    private String code;
    private String info;
    private T data;
}
