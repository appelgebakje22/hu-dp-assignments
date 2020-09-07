package nl.appelgebakje22.dp.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.appelgebakje22.dp.dao.AdresDAO;
import nl.appelgebakje22.dp.dao.ReizigerDAO;
import nl.appelgebakje22.dp.domain.Adres;
import nl.appelgebakje22.dp.domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class AdresDAOImpl implements AdresDAO {

	private final Connection conn;
	@SuppressWarnings("unused") @Setter
	private ReizigerDAO rdao;

	@Override
	public boolean save(Adres entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO adres VALUES (?,?,?,?,?,?)")) {
			stmt.setInt(1, entity.getId());
			stmt.setString(2, entity.getPostcode());
			stmt.setString(3, entity.getHuisnummer());
			stmt.setString(4, entity.getStraat());
			stmt.setString(5, entity.getWoonplaats());
			stmt.setInt(6, entity.getReiziger_id());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean update(Adres entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement(
				"UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?"
		)) {
			stmt.setString(1, entity.getPostcode());
			stmt.setString(2, entity.getHuisnummer());
			stmt.setString(3, entity.getStraat());
			stmt.setString(4, entity.getWoonplaats());
			stmt.setInt(5, entity.getReiziger_id());
			stmt.setInt(6, entity.getId());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public boolean delete(Adres entity) {
		try (PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM adres WHERE adres_id = ?")) {
			stmt.setInt(1, entity.getId());
			return stmt.executeUpdate() == 1;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public Adres findById(int id) {
		try (PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM adres WHERE adres_id = ?")) {
			stmt.setInt(1, id);
			ResultSet set = stmt.executeQuery();
			return set.next() ? mapEntity(set) : null;
		} catch (SQLException e) {
			return null;
		}
	}

	@Override
	public Adres findByReiziger(Reiziger reiziger) {
		try (PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ? LIMIT 1")) {
			stmt.setInt(1, reiziger.getId());
			ResultSet set = stmt.executeQuery();
			return set.next() ? mapEntity(set) : null;
		} catch (SQLException e) {
			return null;
		}
	}

	@Override
	public List<Adres> findAll() {
		try (Statement stmt = this.conn.createStatement()) {
			ResultSet set = stmt.executeQuery("SELECT * FROM adres");
			ArrayList<Adres> result = new ArrayList<>();
			while (set.next()) {
				result.add(mapEntity(set));
			}
			return result;
		} catch (SQLException e) {
			return Collections.emptyList();
		}
	}

	private static Adres mapEntity(ResultSet set) throws SQLException {
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