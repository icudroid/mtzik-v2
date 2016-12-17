package fr.k2i.adbeback.webapp.bean.datatable.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User: dimitri
 * Date: 22/01/15
 * Time: 14:06
 * Goal:
 */
@Data
public  class DataTableResponse<T> implements Serializable {

    private Integer draw;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List<T> data;
    private String error;

}
