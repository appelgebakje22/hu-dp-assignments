package nl.appelgebakje22.dp.domain;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Reiziger {

	private int id;
	private String voorletters, tussenvoegsel, achternaam;
	private Date geboortedatum;
}