package fr.k2i.adbeback.webapp.bean;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * User: dimitri
 * Date: 30/12/14
 * Time: 14:58
 * Goal:
 */
@Data
@Builder
public class ResponseBean {
    private Integer errCode;
    private String msgerr;

    public static ResponseBean OK() {
        return  ResponseBean.builder().errCode(0).build();
    }

    public static ResponseBean FAILED(String message) {
        return ResponseBean.builder().errCode(-1).msgerr(message).build();
    }
}
