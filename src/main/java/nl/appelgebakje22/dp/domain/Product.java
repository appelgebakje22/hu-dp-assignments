package nl.appelgebakje22.dp.domain;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.appelgebakje22.dp.lib.LazyOptional;
import nl.appelgebakje22.dp.lib.SQLIgnore;
import nl.appelgebakje22.dp.lib.SQLTable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@SQLTable("product")
public class Product {

	private int product_nummer;
	private String naam;
	private String beschrijving;
	private float prijs;
	@SQLIgnore private List<Integer> ovIdList;
	@SQLIgnore private List<LazyOptional<OVChipkaart>> ovList;

	@Override
	public String toString() {
		return "Product{" +
				"product_nummer=" + product_nummer +
				", naam='" + naam + '\'' +
				", beschrijving='" + beschrijving + '\'' +
				", prijs=" + prijs +
				'}';
	}
}