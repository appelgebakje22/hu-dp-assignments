package nl.appelgebakje22.dp;

import nl.appelgebakje22.dp.dao.AdresDAO;
import nl.appelgebakje22.dp.dao.ReizigerDAO;
import nl.appelgebakje22.dp.domain.Adres;
import nl.appelgebakje22.dp.domain.Reiziger;
import nl.appelgebakje22.dp.impl.AdresDAOImpl;
import nl.appelgebakje22.dp.impl.ReizigerDAOImpl;
import nl.appelgebakje22.dp.lib.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

	private static final int TEST_ID = 77;

	public static void main(String[] args) {
		new Main();
	}

	private final Connection conn;

	public Main() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ignored) {
			throw new RuntimeException("Couldn't initialize PostgreSQL Driver ");
		}
		try {
			Properties dbProps = Helper.getDatabaseProps();
			this.conn = DriverManager.getConnection("jdbc:postgresql://" + dbProps.getProperty("host") + "/ovchiphu", dbProps.getProperty("username"), dbProps.getProperty("password"));
		} catch (SQLException e) {
			RuntimeException e2 = new RuntimeException();
			e2.setStackTrace(e.getStackTrace());
			throw e2;
		}

		this.testReizigerDAO();
		this.testAdresDAO();

		this.cleanup();
		try {
			this.conn.close();
		} catch (SQLException ignored) {
		}
	}

	private void testReizigerDAO() {
		System.out.println("\n---------- Test ReizigerDAO -------------");
		ReizigerDAO rdao = new ReizigerDAOImpl(this.conn);

		// Haal alle reizigers op uit de database
		List<Reiziger> reizigers = rdao.findAll();
		System.out.println("[Test]ReizigerDAO.findAll() geeft de volgende reizigers:");
		reizigers.forEach(System.out::println);
		System.out.println();

		//Maak een nieuwe reiziger aan en persisteer deze in de database
		Adres adres = new Adres(TEST_ID, "1234AB", "1", "Straat", "Plaats", TEST_ID);
		String gbdatum = "1981-03-14";
		Reiziger sietske = new Reiziger(TEST_ID, "S", "", "Boers", java.sql.Date.valueOf(gbdatum), adres, new ArrayList<>());
		if (rdao.save(sietske)) {
			System.out.print("[Test]Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
			reizigers = rdao.findAll();
			System.out.println(reizigers.size() + " reizigers\n");
		} else {
			System.err.println("[Test]Fout tijdens ReizigerDAO.save()");
		}

		//Update een bestaande reiziger en persisteer deze in de database
		sietske.setVoorletters("S2");
		if (rdao.update(sietske)) {
			System.out.println("[Test]Update was succesvol");
		} else {
			System.err.println("[Test]Fout tijdens ReizigerDAO.update()");
		}

		//Verwijder een bestaande reiziger uit de database
		if (rdao.delete(sietske)) {
			System.out.println("[Test]Delete was succesvol");
		} else {
			System.err.println("[Test]Fout tijdens ReizigerDAO.delete()");
		}
	}

	private void testAdresDAO() {
		System.out.println("\n---------- Test AdresDAO -------------");
		AdresDAO adao = new AdresDAOImpl(this.conn);

		// Haal alle adressen op uit de database
		List<Adres> adressen = adao.findAll();
		System.out.println("[Test]AdresDAO.findAll() geeft de volgende adressen:");
		adressen.forEach(System.out::println);
		System.out.println();

		//Maak een nieuw adres in de database en persisteer deze in de database
		Adres adres = new Adres(TEST_ID, "1234AB", "1", "Straat", "Plaats", TEST_ID);
		System.out.print("[Test]Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
		adao.save(adres);
		adressen = adao.findAll();
		System.out.print(adressen.size() + " adressen\n");
	}

	private void cleanup() {
		Helper.doSilently(() -> {
			ReizigerDAO rdao = new ReizigerDAOImpl(this.conn);
			rdao.delete(rdao.findById(TEST_ID));
		});
		Helper.doSilently(() -> {
			AdresDAO adao = new AdresDAOImpl(this.conn);
			adao.delete(adao.findById(TEST_ID));

		});
	}
}