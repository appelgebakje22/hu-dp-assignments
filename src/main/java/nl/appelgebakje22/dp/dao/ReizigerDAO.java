package nl.appelgebakje22.dp.dao;

import nl.appelgebakje22.dp.domain.Reiziger;

import java.util.List;

public interface ReizigerDAO extends IDAO<Reiziger> {

	List<Reiziger> findByGbdatum(String datum);
}