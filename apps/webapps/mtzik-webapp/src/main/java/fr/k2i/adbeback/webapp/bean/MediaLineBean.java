package fr.k2i.adbeback.webapp.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class MediaLineBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7127790773994895099L;
	private String title;
	private Integer adNeeded;
	private Long idMedia;
    private String jacket;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMedia == null) ? 0 : idMedia.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediaLineBean other = (MediaLineBean) obj;
		if (idMedia == null) {
			if (other.idMedia != null)
				return false;
		} else if (!idMedia.equals(other.idMedia))
			return false;
		return true;
	}
	
}
