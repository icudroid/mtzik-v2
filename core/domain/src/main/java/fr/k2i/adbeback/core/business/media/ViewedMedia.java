package fr.k2i.adbeback.core.business.media;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.converter.LocalDateTimePersistenceConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "viewed_media")
public class ViewedMedia extends BaseObject implements Serializable {
	private static final long serialVersionUID = -925714084631112751L;
    @Id
    @SequenceGenerator(name = "ViewedMedia_Gen", sequenceName = "ViewedMedia_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ViewedMedia_Gen")
	private Long id;

    @ManyToMany(targetEntity = Media.class, cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    @JoinTable(name = "view_media", joinColumns = @JoinColumn(name = "VIEW_ID"), inverseJoinColumns = @JoinColumn(name = "MEDIA_ID"))
	private List<Media> medias;

    //@Temporal(TemporalType.TIMESTAMP)
	@Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime viewed;

    @Column(name = "is_win_view")
	private Boolean won;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((medias == null) ? 0 : medias.hashCode());
		result = prime * result + ((viewed == null) ? 0 : viewed.hashCode());
		result = prime * result + ((won == null) ? 0 : won.hashCode());
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
		ViewedMedia other = (ViewedMedia) obj;
		if (medias == null) {
			if (other.medias != null)
				return false;
		} else if (!medias.equals(other.medias))
			return false;
		if (viewed == null) {
			if (other.viewed != null)
				return false;
		} else if (!viewed.equals(other.viewed))
			return false;
		if (won == null) {
			if (other.won != null)
				return false;
		} else if (!won.equals(other.won))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ViewedMedia [id=" + id + ", medias=" + medias + ", viewed="
				+ viewed + ", won=" + won + "]";
	}

}
