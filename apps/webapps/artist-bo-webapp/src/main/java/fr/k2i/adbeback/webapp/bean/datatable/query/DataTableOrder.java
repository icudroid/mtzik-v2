package fr.k2i.adbeback.webapp.bean.datatable.query;

import lombok.*;
import lombok.experimental.Builder;
import org.springframework.data.domain.Sort;

/**
 * User: dimitri
 * Date: 22/01/15
 * Time: 13:59
 * Goal:
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataTableOrder {
    private Integer column;
    private String dir;

    public Sort.Direction toDirection(){
        return Sort.Direction.valueOf(dir.toUpperCase());
    }
}
