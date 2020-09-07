package nl.appelgebakje22.dp.domain;

import lombok.*;
import nl.appelgebakje22.dp.dao.ReizigerDAO;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor()
@Getter
@Setter
@EqualsAndHashCode
public class Reiziger {

	private int id;
	private String voorletters, tussenvoegsel, achternaam;
	private Date geboortedatum;
	private Adres adres;

	@Override
	public String toString() {
		return String.format(
				"Reiziger {#%s %s. %s %s, geb %s, %s}",
				this.id,
				this.voorletters,
				this.tussenvoegsel,
				this.achternaam,
				this.geboortedatum,
				this.adres
		);
	}
}