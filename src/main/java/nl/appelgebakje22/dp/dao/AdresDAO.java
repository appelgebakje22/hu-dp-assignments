package nl.appelgebakje22.dp.dao;

import nl.appelgebakje22.dp.domain.Adres;
import nl.appelgebakje22.dp.domain.Reiziger;

import java.util.List;

public interface AdresDAO extends IDAO<Adres> {

	Adres findByReiziger(Reiziger reiziger);
}