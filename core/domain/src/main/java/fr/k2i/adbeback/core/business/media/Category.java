package fr.k2i.adbeback.core.business.media;

import fr.k2i.adbeback.core.business.BaseObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "genre")
@DiscriminatorColumn(name = "classe", discriminatorType = DiscriminatorType.STRING)
public abstract class Category extends BaseObject implements Serializable {

	private static final long serialVersionUID = 7015318296306104846L;

    @Id
    @SequenceGenerator(name = "Category_Gen", sequenceName = "Category_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Category_Gen")
    protected Long id;
	protected String genre;
	protected String codeGenre; // Pour les traductions


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeGenre == null) ? 0 : codeGenre.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
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
		Category other = (Category) obj;
		if (codeGenre == null) {
			if (other.codeGenre != null)
				return false;
		} else if (!codeGenre.equals(other.codeGenre))
			return false;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		return true;
	}
}
