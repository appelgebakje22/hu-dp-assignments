package nl.appelgebakje22.dp.domain;

import java.sql.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nl.appelgebakje22.dp.lib.LazyOptional;
import nl.appelgebakje22.dp.lib.SQLIgnore;
import nl.appelgebakje22.dp.lib.SQLTable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@SQLTable("ov_chipkaart")
public class OVChipkaart {

	private int kaart_nummer;
	private Date geldig_tot;
	private int klasse;
	private float saldo;
	private int reiziger_id;
	@SQLIgnore private Reiziger reiziger;
	@SQLIgnore private List<Integer> productIdList;
	@SQLIgnore private List<LazyOptional<Product>> productList;

	public OVChipkaart(int kaart_nummer, Date geldig_tot, int klasse, float saldo, int reiziger_id) {
		this.kaart_nummer = kaart_nummer;
		this.geldig_tot = geldig_tot;
		this.klasse = klasse;
		this.saldo = saldo;
		this.reiziger_id = reiziger_id;
	}

	@Override
	public String toString() {
		return "OVChipkaart{" +
				"kaart_nummer=" + kaart_nummer +
				", geldig_tot=" + geldig_tot +
				", klasse=" + klasse +
				", saldo=" + saldo +
				", reiziger_id=" + reiziger_id +
				'}';
	}
}