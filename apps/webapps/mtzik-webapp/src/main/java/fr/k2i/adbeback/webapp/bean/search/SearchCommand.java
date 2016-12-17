package fr.k2i.adbeback.webapp.bean.search;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 11/01/14
 * Time: 21:26
 * To change this template use File | Settings | File Templates.
 */

@Data
public class SearchCommand implements Serializable{
    private String req;
    private long genreId;
    private int top = 50;
}
