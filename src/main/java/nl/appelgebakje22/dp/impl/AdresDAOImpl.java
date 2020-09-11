package nl.appelgebakje22.dp.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import nl.appelgebakje22.dp.dao.AdresDAO;
import nl.appelgebakje22.dp.domain.Adres;
import nl.appelgebakje22.dp.domain.Reiziger;

public final class AdresDAOImpl extends AbstractDAOImpl<Adres> implements AdresDAO {

	public AdresDAOImpl(Connection conn) {
		super(conn, Adres.class);
	}

	@Override
	public boolean save(Adres entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(createInsertQuery())) {
			this.mapData(stmt, new Object[]{
					entity.getId(),
					entity.getPostcode(),
					entity.getHuisnummer(),
					entity.getStraat(),
					entity.getWoonplaats(),
					entity.getReiziger_id()
			});
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return false;
		}
	}

	@Override
	public boolean update(Adres entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(createUpdateQuery())) {
			stmt.setString(1, entity.getPostcode());
			stmt.setString(2, entity.getHuisnummer());
			stmt.setString(3, entity.getStraat());
			stmt.setString(4, entity.getWoonplaats());
			stmt.setInt(5, entity.getReiziger_id());
			stmt.setInt(6, entity.getId());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return false;
		}
	}

	@Override
	public boolean delete(Adres entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM adres WHERE adres_id = ?")) {
			stmt.setInt(1, entity.getId());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return false;
		}
	}

	@Override
	public Adres findByReiziger(Reiziger reiziger) {
		try (PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ? LIMIT 1")) {
			stmt.setInt(1, reiziger.getId());
			ResultSet set = stmt.executeQuery();
			return set.next() ? mapEntity(set) : null;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return null;
		}
	}

	@Override
	protected Adres mapEntity(ResultSet set) throws SQLException {
		Adres result = new Adres();
		result.setId(set.getInt("adres_id"));
		result.setPostcode(set.getString("postcode"));
		result.setHuisnummer(set.getString("huisnummer"));
		result.setStraat(set.getString("straat"));
		result.setWoonplaats(set.getString("woonplaats"));
		result.setReiziger_id(set.getInt("reiziger_id"));
		return result;
	}
}