package nl.appelgebakje22.dp.domain;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Adres {

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