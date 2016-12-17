package fr.k2i.adbeback.core.business.media;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Video")
public class VideoCategory extends Category {

	@Override
	public String toString() {
		return "VideoGenre [id=" + id + ", genre=" + genre + ", codeGenre="
				+ codeGenre + "]";
	}

	private static final long serialVersionUID = 7156290999522090595L;

}
