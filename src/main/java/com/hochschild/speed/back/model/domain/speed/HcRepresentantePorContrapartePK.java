package com.hochschild.speed.back.model.domain.speed;

import lombok.Data;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class HcRepresentantePorContrapartePK implements Serializable {

	private static final long serialVersionUID = 780634102524620036L;

	@ManyToOne
	@JoinColumn(name = "id_contraparte", nullable = false)
	private Cliente contraparte;

	@ManyToOne
	@JoinColumn(name = "id_representate", nullable = false)
	private Cliente representante;
}
