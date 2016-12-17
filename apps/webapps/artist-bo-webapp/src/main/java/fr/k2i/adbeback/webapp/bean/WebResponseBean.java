package fr.k2i.adbeback.webapp.bean;

import lombok.Data;
import lombok.experimental.Builder;

import java.io.Serializable;

/**
 * User: dimitri
 * Date: 14/01/15
 * Time: 10:54
 * Goal:
 */
@Data
@Builder
public class WebResponseBean<T> implements Serializable{
    private Integer resultCode;
    private String errorMessage;
    private T data;


    public static <T> WebResponseBean OK_WITH_DATA(T data){
        return WebResponseBean.builder().resultCode(0).data(data).build();
    }

    public static <T> WebResponseBean OK(){
        return WebResponseBean.builder().resultCode(0).build();
    }

    public static <T> WebResponseBean FAILED(Integer errorCode,String errorMessage){
        return WebResponseBean.builder().resultCode(errorCode).errorMessage(errorMessage).build();
    }

}
