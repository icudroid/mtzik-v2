package fr.k2i.adbeback.core.business.media;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@DiscriminatorValue("Video")
public class Video extends Media {
	private static final long serialVersionUID = 305593059441866272L;

    @ManyToMany(targetEntity = Actor.class, cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    @JoinTable(name = "video_actor", joinColumns = @JoinColumn(name = "VIDEO_ID"), inverseJoinColumns = @JoinColumn(name = "ACTOR_ID"))
	private List<Actor> actors;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actors == null) ? 0 : actors.hashCode());
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
		Video other = (Video) obj;
		if (actors == null) {
			if (other.actors != null)
				return false;
		} else if (!actors.equals(other.actors))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Video [actors=" + actors + ", id=" + id + ", title=" + title
				+ ", productors=" + productors + ", genres=" + categories
				+ ", description=" + description + ", duration=" + duration
				+ ", jacket=" + jacket + ", releaseDate=" + releaseDate + "]";
	}

}
