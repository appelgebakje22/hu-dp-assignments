package nl.appelgebakje22.dp.dao;

import java.util.List;
import nl.appelgebakje22.dp.domain.OVChipkaart;
import nl.appelgebakje22.dp.domain.Product;

public interface ProductDAO extends IDAO<Product> {

	List<Product> findByOVChipkaart(OVChipkaart entity);
}