package nl.appelgebakje22.dp.domain;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.appelgebakje22.dp.lib.SQLTable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@SQLTable("ov_chipkaart")
public class OVChipkaart {

	private int kaart_nummer;
	private Date geldig_tot;
	private int klasse;
	private float saldo;
	private int reizigers_id;
}