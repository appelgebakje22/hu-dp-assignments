package nl.appelgebakje22.dp.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nl.appelgebakje22.dp.dao.OVChipkaartDAO;
import nl.appelgebakje22.dp.domain.OVChipkaart;
import nl.appelgebakje22.dp.domain.Reiziger;

public final class OVChipkaartDAOImpl extends AbstractDAOImpl<OVChipkaart> implements OVChipkaartDAO {

	public OVChipkaartDAOImpl(Connection conn) {
		super(conn, OVChipkaart.class);
	}

	@Override
	public boolean save(OVChipkaart entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(createInsertQuery())) {
			this.mapData(stmt, new Object[]{
					entity.getKaart_nummer(),
					entity.getGeldig_tot(),
					entity.getKlasse(),
					entity.getSaldo(),
					entity.getReiziger_id()
			});
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return false;
		}
	}

	@Override
	public boolean update(OVChipkaart entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(this.createUpdateQuery())) {
			stmt.setInt(1, entity.getKaart_nummer());
			stmt.setDate(2, entity.getGeldig_tot());
			stmt.setInt(3, entity.getKlasse());
			stmt.setFloat(4, entity.getSaldo());
			stmt.setInt(5, entity.getReiziger_id());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return false;
		}
	}

	@Override
	public boolean delete(OVChipkaart entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(String.format("DELETE FROM ov_chipkaart WHERE %s = ?", this.columns[0]))) {
			stmt.setInt(1, entity.getKaart_nummer());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return false;
		}
	}

	@Override
	public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
		try (PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?")) {
			stmt.setInt(1, reiziger.getId());
			ResultSet set = stmt.executeQuery();
			ArrayList<OVChipkaart> result = new ArrayList<>();
			while (set.next()) {
				OVChipkaart entity = this.mapEntity(set);
				entity.setReiziger(reiziger);
				result.add(entity);
			}
			return result;
		} catch (SQLException e) {
			System.err.println(e.toString());
			return null;
		}
	}

	@Override
	protected OVChipkaart mapEntity(ResultSet set) throws SQLException {
		OVChipkaart result = new OVChipkaart();
		result.setKaart_nummer(set.getInt(this.columns[0]));
		result.setGeldig_tot(set.getDate(this.columns[1]));
		result.setKlasse(set.getInt(this.columns[2]));
		result.setSaldo(set.getFloat(this.columns[3]));
		result.setReiziger_id(set.getInt(this.columns[4]));
		return result;
	}
}