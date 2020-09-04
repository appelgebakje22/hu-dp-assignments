package nl.appelgebakje22.dp.p1;

import nl.appelgebakje22.dp.Helper;

import java.sql.*;
import java.util.Properties;

public class DP1 {

	public static void main(String[] args) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ignored) {
			throw new RuntimeException("Couldn't initialize PostgreSQL Driver ");
		}
		Properties dbProps = Helper.getDatabaseProps();
		try (Connection conn = DriverManager.getConnection("jdbc:postgresql://" + dbProps.getProperty("host") + "/ovchiphu", dbProps.getProperty("username"), dbProps.getProperty("password"))) {
			Statement s = conn.createStatement();
			s.execute("SELECT * FROM reiziger");
			ResultSet set = s.getResultSet();
			printResultSet(set);
			set.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static void printResultSet(ResultSet set) throws SQLException {
		System.out.println("Alle reizigers:");
		while (set.next()) {
			String builder = "\t#" + set.getObject("reiziger_id") + ':' +
					' ' + set.getObject("voorletters") + '.' +
					' ' + set.getObject("tussenvoegsel") +
					' ' + set.getObject("achternaam") +
					" (" + set.getObject("geboortedatum") + ')';
			System.out.println(builder);
		}
	}
}