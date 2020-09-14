package nl.appelgebakje22.dp.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.appelgebakje22.dp.lib.SQLColumn;
import nl.appelgebakje22.dp.lib.SQLTable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@SQLTable("adres")
public class Adres {

	@SQLColumn("adres_id")
	private int id;
	private String postcode, huisnummer, straat, woonplaats;
	private int reiziger_id;

	@Override
	public String toString() {
		return String.format(
				"Adres {#%s %s-%s}",
				this.id,
				this.postcode,
				this.huisnummer
		);
	}
}