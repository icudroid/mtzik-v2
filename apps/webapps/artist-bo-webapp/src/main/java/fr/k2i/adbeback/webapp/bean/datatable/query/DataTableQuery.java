package fr.k2i.adbeback.webapp.bean.datatable.query;

import lombok.*;
import lombok.experimental.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * User: dimitri
 * Date: 22/01/15
 * Time: 13:53
 * Goal:
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataTableQuery<T> implements Serializable {
    private Integer draw;
    private Integer start;
    private Integer length;

    private T additionalForm;

    //private Map<String, String> search;
    private DataTableSearch search;

    //private List<Map<String, String>> order;

    private List<DataTableOrder> order;


/*
     public DataTableSearch getSearchObj(){
         if(search==null)return new DataTableSearch();
        return DataTableSearch.builder()
                .regex(Boolean.valueOf(search.get("regex")))
                .value(search.get("value")).build();
    }
*/


/*    public List<DataTableOrder> getOrderObj(){
        List<DataTableOrder> res = new ArrayList<>();

        if(order==null)return new ArrayList<>();

        for (Map<String, String> map :order) {
            res.add(
                    DataTableOrder.builder()
                            .column(Integer.valueOf(map.get("column")))
                            .dir(map.get("dir"))
                            .build()
            );
        }

        return res;
    }*/

}
