package fr.k2i.adbeback.webapp.bean.datatable.query;

import lombok.*;
import lombok.experimental.Builder;

/**
 * User: dimitri
 * Date: 22/01/15
 * Time: 14:00
 * Goal:
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataTableSearch {
    private String value;
    private Boolean regex;
}
