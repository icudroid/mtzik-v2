package fr.k2i.adbeback.core.business.country;

import java.io.Serializable;

import javax.persistence.*;


import fr.k2i.adbeback.core.business.BaseObject;
import lombok.Data;


@Data
@Entity
@Table(name = "country")
public class Country extends BaseObject implements Serializable{
	
	private static final long serialVersionUID = 2428113425860286720L;
    @Id
    @SequenceGenerator(name = "Country_Gen", sequenceName = "Country_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Country_Gen")
    private Long id;
    @Column(nullable=false,unique=true)
    private String code;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Country other = (Country) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", code=" + code + "]";
	}

}
