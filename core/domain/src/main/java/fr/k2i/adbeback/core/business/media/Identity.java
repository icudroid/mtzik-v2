package fr.k2i.adbeback.core.business.media;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.converter.LocalDatePersistenceConverter;
import fr.k2i.adbeback.core.business.user.Address;
import fr.k2i.adbeback.core.business.user.AgeGroup;
import fr.k2i.adbeback.core.business.user.Sex;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "person")
@DiscriminatorColumn(name = "classe", discriminatorType = DiscriminatorType.STRING)
public abstract class Identity extends BaseObject implements Serializable {
	private static final long serialVersionUID = 4741756135041947874L;

    @Id
    @SequenceGenerator(name = "Person_Gen", sequenceName = "Person_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Person_Gen")
	protected Long id;

	protected String firstName; // required
	protected String lastName; // required
	protected String website;
	protected Integer version;
    protected String photo;
    protected String biography;

    protected String phone;
    protected String mobile;

    //social information
    protected String twitter;
    protected String facebook;
    protected String googlePlus;


    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate birthday;

    @Embedded
    private Address address = new Address();

	public Identity(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}


    @Transient
    public abstract String getFullName();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((website == null) ? 0 : website.hashCode());
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
		Identity other = (Identity) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (website == null) {
			if (other.website != null)
				return false;
		} else if (!website.equals(other.website))
			return false;
		return true;
	}

	public Identity() {
	}

}
