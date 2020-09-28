package nl.appelgebakje22.dp.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nl.appelgebakje22.dp.dao.OVChipkaartDAO;
import nl.appelgebakje22.dp.dao.ProductDAO;
import nl.appelgebakje22.dp.domain.OVChipkaart;
import nl.appelgebakje22.dp.domain.Product;
import nl.appelgebakje22.dp.lib.LazyOptional;

public class ProductDAOImpl extends AbstractDAOImpl<Product> implements ProductDAO {

	private final LazyOptional<OVChipkaartDAO> odao;

	public ProductDAOImpl(Connection conn) {
		super(conn, Product.class);
		this.odao = LazyOptional.of(() -> new OVChipkaartDAOImpl(conn));
	}

	@Override
	public boolean save(Product entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(this.createInsertQuery())) {
			this.mapData(stmt, new Object[]{
					entity.getProduct_nummer(),
					entity.getNaam(),
					entity.getBeschrijving(),
					entity.getPrijs()
			});
			return stmt.executeUpdate() == 1 && syncOvIds(entity);
		} catch (SQLException e) {
			System.err.println(e.toString());
			return false;
		}
	}

	@Override
	public boolean update(Product entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(this.createUpdateQuery())) {
			stmt.setInt(1, entity.getProduct_nummer());
			stmt.setString(2, entity.getNaam());
			stmt.setString(3, entity.getBeschrijving());
			stmt.setFloat(4, entity.getPrijs());
			return stmt.executeUpdate() == 1 && syncOvIds(entity);
		} catch (SQLException e) {
			System.err.println(e.toString());
		}
		return false;
	}

	private boolean syncOvIds(Product entity) throws SQLException {
		//Sync local ids
		for (int id : entity.getOvIdList()) {
			PreparedStatement stmt2 = this.conn.prepareStatement("SELECT * FROM ov_chipkaart_product WHERE product_nummer = ? AND kaart_nummer = ?");
			if (stmt2.executeQuery().first()) {
				//Record already exists, skipping!
				continue;
			}
			stmt2.close();
			PreparedStatement stmt3 = this.conn.prepareStatement("INSERT INTO ov_chipkaart_product VALUES (?, ?, ?, ?)");
			stmt3.setInt(1, id);
			stmt3.setInt(2, entity.getProduct_nummer());
			stmt3.setString(3, "actief");
			stmt3.setDate(4, Date.valueOf(LocalDate.now()));
			if (stmt3.executeUpdate() != 1) {
				return false;
			}
			stmt3.close();
		}
		//Delete remote ids
		PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM ov_chipkaart_product WHERE product_nummer = ?");
		stmt.setInt(1, entity.getProduct_nummer());
		ResultSet set = stmt.executeQuery();
		while (set.next()) {
			int remoteId = set.getInt(1);
			if (entity.getOvIdList().contains(remoteId)) {
				//Record exists locally, skipping!
				continue;
			}
			PreparedStatement stmt2 = this.conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ? AND product_nummer = ?");
			stmt2.setInt(1, remoteId);
			stmt2.setInt(2, entity.getProduct_nummer());
			if (stmt2.executeUpdate() != 1) {
				return false;
			}
			stmt2.close();
		}
		return true;
	}

	@Override
	public boolean delete(Product entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?")) {
			stmt.setInt(1, entity.getProduct_nummer());
			if (stmt.executeUpdate() == 1) {
				try (PreparedStatement stmt2 = this.conn.prepareStatement(String.format("DELETE FROM product WHERE %s = ?", this.columns[0]))) {
					stmt2.setInt(1, entity.getProduct_nummer());
					return stmt2.executeUpdate() == 1;
				} catch (SQLException e) {
					System.err.println(e.toString());
				}
			}
		} catch (SQLException e) {
			System.err.println(e.toString());
		}
		return false;
	}

	@Override
	public List<Product> findByOVChipkaart(OVChipkaart entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(
				"SELECT product.* FROM product INNER JOIN ov_chipkaart_product ON ov_chipkaart_product.product_nummer = product.product_nummer WHERE ov_chipkaart_product.kaart_nummer = ?"
		)) {
			stmt.setInt(1, entity.getKaart_nummer());
			ResultSet set = stmt.executeQuery();
			ArrayList<Product> result = new ArrayList<>();
			while (set.next()) {
				result.add(mapEntity(set));
			}
			return result;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return null;
		}
	}

	@Override
	protected Product mapEntity(ResultSet set) throws SQLException {
		Product result = new Product();
		result.setProduct_nummer(set.getInt(1));
		result.setNaam(set.getString(2));
		result.setBeschrijving(set.getString(3));
		result.setPrijs(set.getFloat(4));
		return mapOvs(result);
	}

	private Product mapOvs(Product entity) throws SQLException {
		PreparedStatement stmt = this.conn.prepareStatement(
				"SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?"
		);
		stmt.setInt(1, entity.getProduct_nummer());
		List<Integer> resultList = new ArrayList<>();
		ResultSet set = stmt.executeQuery();
		while (set.next()) {
			resultList.add(set.getInt(1));
		}
		set.close();
		stmt.close();
		entity.setOvIdList(resultList);
		entity.setOvList(new ArrayList(resultList.stream().map(
				this.odao.resolve().get()::findById
		).collect(Collectors.toList())));
		return entity;
	}
}