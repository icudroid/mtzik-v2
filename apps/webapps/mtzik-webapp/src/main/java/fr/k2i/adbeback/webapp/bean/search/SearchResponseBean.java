package fr.k2i.adbeback.webapp.bean.search;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User: dimitri
 * Date: 05/03/14
 * Time: 16:27
 * Goal:
 */
@Data
public class SearchResponseBean implements Serializable{
    private List<SmallPersonBean> artists;
    private List<SmallPersonBean> labels;
    private List<SmallMediaBean> musics;
}





