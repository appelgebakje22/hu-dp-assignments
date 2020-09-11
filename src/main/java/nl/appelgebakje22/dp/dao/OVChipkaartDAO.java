package nl.appelgebakje22.dp.dao;

import java.util.List;
import nl.appelgebakje22.dp.domain.OVChipkaart;
import nl.appelgebakje22.dp.domain.Reiziger;

public interface OVChipkaartDAO extends IDAO<OVChipkaart> {

	List<OVChipkaart> findByReiziger(Reiziger reiziger);
}