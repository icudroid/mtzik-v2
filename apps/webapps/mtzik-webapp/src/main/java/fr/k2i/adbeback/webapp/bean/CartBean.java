package fr.k2i.adbeback.webapp.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CartBean implements Serializable{
	private static final long serialVersionUID = -4552013561893862429L;
	private Set<MediaLineBean> lines = new HashSet<MediaLineBean>();
	private Integer minScore = 0;
	private Integer maxTime;
	private Integer nbProduct = 0;
	private String error;


    public void empty(){
        lines.clear();
        maxTime = 0;
        nbProduct = 0;
        error = null;
    }

    public List<Long> getMediaIds() {
        List<Long> mediaIds = new ArrayList<Long>();

        for (MediaLineBean line : lines) {
            mediaIds.add(line.getIdMedia());
        }

        return mediaIds;
    }
}
