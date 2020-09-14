package nl.appelgebakje22.dp.domain;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.appelgebakje22.dp.lib.SQLColumn;
import nl.appelgebakje22.dp.lib.SQLIgnore;
import nl.appelgebakje22.dp.lib.SQLTable;

@NoArgsConstructor
@AllArgsConstructor()
@Getter
@Setter
@EqualsAndHashCode
@SQLTable("reiziger")
public class Reiziger {

	@SQLColumn("reiziger_id") private int id;
	private String voorletters, tussenvoegsel, achternaam;
	private Date geboortedatum;
	@SQLIgnore private Adres adres;
	@SQLIgnore private List<OVChipkaart> ovList;

	@Override
	public String toString() {
		return String.format(
				"Reiziger {#%s %s. %s %s, geb %s, %s} met OV-chipkaarten: %s",
				this.id,
				this.voorletters,
				this.tussenvoegsel,
				this.achternaam,
				this.geboortedatum,
				this.adres,
				Arrays.toString(this.ovList.toArray())
		);
	}
}