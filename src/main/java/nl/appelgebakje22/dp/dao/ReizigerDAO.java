package nl.appelgebakje22.dp.dao;

import java.util.List;
import nl.appelgebakje22.dp.domain.Reiziger;

public interface ReizigerDAO extends IDAO<Reiziger> {

	List<Reiziger> findByGbdatum(String datum);
}