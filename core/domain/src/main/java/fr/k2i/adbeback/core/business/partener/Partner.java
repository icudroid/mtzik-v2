package fr.k2i.adbeback.core.business.partener;

import java.io.Serializable;

import javax.persistence.*;


import fr.k2i.adbeback.core.business.BaseObject;
import lombok.Data;

@Data
@Entity
@Table(name = "partner")
public class Partner extends BaseObject implements Serializable {

	private static final long serialVersionUID = -8741225076812664415L;

    @Id
    @SequenceGenerator(name = "Partner_Gen", sequenceName = "Partner_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Partner_Gen")
    private Long id;

	private String name;
	private String webSite;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((webSite == null) ? 0 : webSite.hashCode());
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
		Partner other = (Partner) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (webSite == null) {
			if (other.webSite != null)
				return false;
		} else if (!webSite.equals(other.webSite))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Partner [id=" + id + ", name=" + name + ", webSite=" + webSite
				+ "]";
	}

}
