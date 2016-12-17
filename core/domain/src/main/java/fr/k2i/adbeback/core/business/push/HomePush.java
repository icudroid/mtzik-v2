package fr.k2i.adbeback.core.business.push;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.IMetaData;
import fr.k2i.adbeback.core.business.converter.LocalDatePersistenceConverter;
import fr.k2i.adbeback.core.business.media.Media;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "home_push")
@Data
public class HomePush extends BaseObject implements Serializable {

	private static final long serialVersionUID = 6711696472191272787L;

    @Id
    @SequenceGenerator(name = "HomePush_Gen", sequenceName = "HomePush_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HomePush_Gen")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media medias;

    //@Temporal(TemporalType.DATE)
    @Convert(converter = LocalDatePersistenceConverter.class)
	private LocalDate startDate;

    //@Temporal(TemporalType.DATE)
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate endDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HomePush)) return false;

        HomePush homePush = (HomePush) o;

        if (endDate != null ? !endDate.equals(homePush.endDate) : homePush.endDate != null) return false;
        if (startDate != null ? !startDate.equals(homePush.startDate) : homePush.startDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HomePush{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
