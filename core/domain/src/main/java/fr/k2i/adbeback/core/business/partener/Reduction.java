package fr.k2i.adbeback.core.business.partener;

import java.io.Serializable;

import javax.persistence.*;


import fr.k2i.adbeback.core.business.BaseObject;
import lombok.Data;

@Data
@Entity
@Table(name = "reduction")
public class Reduction extends BaseObject implements Serializable {
	private static final long serialVersionUID = 6039138990472415617L;

    @Id
    @SequenceGenerator(name = "Reduction_Gen", sequenceName = "Reduction_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Reduction_Gen")
    private Long id;

	private Double value;
	private Double percentageValue;
	private String reductionCode;
	private String description;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "PARTENER_ID")
    private Partner partner;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((partner == null) ? 0 : partner.hashCode());
		result = prime * result
				+ ((percentageValue == null) ? 0 : percentageValue.hashCode());
		result = prime * result
				+ ((reductionCode == null) ? 0 : reductionCode.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Reduction other = (Reduction) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (partner == null) {
			if (other.partner != null)
				return false;
		} else if (!partner.equals(other.partner))
			return false;
		if (percentageValue == null) {
			if (other.percentageValue != null)
				return false;
		} else if (!percentageValue.equals(other.percentageValue))
			return false;
		if (reductionCode == null) {
			if (other.reductionCode != null)
				return false;
		} else if (!reductionCode.equals(other.reductionCode))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reduction [id=" + id + ", value=" + value
				+ ", percentageValue=" + percentageValue + ", reductionCode="
				+ reductionCode + ", description=" + description
				+ ", partner=" + partner + "]";
	}

}
