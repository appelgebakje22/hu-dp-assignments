package nl.appelgebakje22.dp.impl;

import lombok.AllArgsConstructor;
import nl.appelgebakje22.dp.dao.ReizigerDAO;
import nl.appelgebakje22.dp.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class ReizigerDAOImpl implements ReizigerDAO {

	private final Connection conn;

	@Override
	public boolean save(Reiziger entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO reiziger VALUES (?,?,?,?,?)")) {
			stmt.setInt(1, entity.getId());
			stmt.setString(2, entity.getVoorletters());
			stmt.setString(3, entity.getTussenvoegsel());
			stmt.setString(4, entity.getAchternaam());
			stmt.setDate(5, entity.getGeboortedatum());
			return stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(Reiziger entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(
				"UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?"
		)) {
			stmt.setString(1, entity.getVoorletters());
			stmt.setString(2, entity.getTussenvoegsel());
			stmt.setString(3, entity.getAchternaam());
			stmt.setDate(4, entity.getGeboortedatum());
			stmt.setInt(5, entity.getId());
			return !stmt.execute(); //Update returns false
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(Reiziger entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?")) {
			stmt.setInt(1, entity.getId());
			return !stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Reiziger> findByGbdatum(String datum) {
		try (PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum = ?")) {
			stmt.setDate(1, Date.valueOf(datum));
			ResultSet set = stmt.executeQuery();
			ArrayList<Reiziger> result = new ArrayList<>();
			while (set.next()) {
				result.add(mapEntity(set));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public Reiziger findById(int id) {
		try (PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM reiziger WHERE reizigers_id = ?")) {
			stmt.setInt(1, id);
			ResultSet set = stmt.executeQuery();
			return set.first() ? mapEntity(set) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Reiziger> findAll() {
		try (Statement stmt = this.conn.createStatement()) {
			ResultSet set = stmt.executeQuery("SELECT * FROM reiziger");
			ArrayList<Reiziger> result = new ArrayList<>();
			while (set.next()) {
				result.add(mapEntity(set));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private static Reiziger mapEntity(ResultSet set) throws SQLException {
		Reiziger result = new Reiziger();
		result.setId(set.getInt("reiziger_id"));
		result.setVoorletters(set.getString("voorletters"));
		result.setTussenvoegsel(set.getString("tussenvoegsel"));
		result.setAchternaam(set.getString("achternaam"));
		result.setGeboortedatum(set.getDate("geboortedatum"));
		return result;
	}
}