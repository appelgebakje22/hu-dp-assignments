package nl.appelgebakje22.dp;

import nl.appelgebakje22.dp.dao.ReizigerDAO;
import nl.appelgebakje22.dp.domain.Reiziger;
import nl.appelgebakje22.dp.impl.ReizigerDAOImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ignored) {
			throw new RuntimeException("Couldn't initialize PostgreSQL Driver ");
		}
		Properties dbProps = Helper.getDatabaseProps();
		try (Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbProps.getProperty("host") + "/ovchiphu", dbProps.getProperty("username"), dbProps.getProperty("password"))) {
			ReizigerDAO dao = new ReizigerDAOImpl(conn);
			testReizigerDAO(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
		System.out.println("\n---------- Test ReizigerDAO -------------");

		// Haal alle reizigers op uit de database
		List<Reiziger> reizigers = rdao.findAll();
		System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
		for (Reiziger r : reizigers) {
			System.out.println(r);
		}
		System.out.println();

		// Maak een nieuwe reiziger aan en persisteer deze in de database
		String gbdatum = "1981-03-14";
		Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
		System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
		rdao.save(sietske);
		reizigers = rdao.findAll();
		System.out.println(reizigers.size() + " reizigers\n");

		//Update een bestaande reiziger en persisteer deze in de database
		sietske.setVoorletters("S2");
		rdao.update(sietske);
		System.out.println("[Test]Update was succesvol");

		//Verwijder een bestaande reiziger uit de database
		rdao.delete(sietske);
		System.out.println("[Test]Delete was succesvol");
	}
}
