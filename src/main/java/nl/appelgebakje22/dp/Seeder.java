package nl.appelgebakje22.dp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

final class Seeder {

    public static void resetDatabase(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("TRUNCATE TABLE ov_chipkaart_product, product, ov_chipkaart, reiziger, adres");
            stmt.execute("-- ------------------------------------------------------------------------\n" +
                    "-- Data & Persistency - Casus 'ov-chipkaart'\n" +
                    "--\n" +
                    "-- (c) 2020 Hogeschool Utrecht\n" +
                    "-- Tijmen Muller (tijmen.muller@hu.nl)\n" +
                    "-- ------------------------------------------------------------------------\n" +
                    "\n" +
                    "-- Vul alle tabellen met minimaal 5 records\n" +
                    "\n" +
                    "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum)\n" +
                    "VALUES (1, 'G', 'van', 'Rijn', TO_DATE('17-09-2002', 'dd-mm-yyyy')),\n" +
                    "       (2, 'B', 'van', 'Rijn', TO_DATE('22-10-2002', 'dd-mm-yyyy')),\n" +
                    "       (3, 'H', NULL, 'Lubben', TO_DATE('11-08-1998', 'dd-mm-yyyy')),\n" +
                    "       (4, 'F', NULL, 'Memari', TO_DATE('03-12-2002', 'dd-mm-yyyy')),\n" +
                    "       (5, 'G', NULL, 'Piccardo', TO_DATE('03-12-2002', 'dd-mm-yyyy'));\n" +
                    "\n" +
                    "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id)\n" +
                    "VALUES (35283, TO_DATE('31-05-2018', 'dd-mm-yyyy'), 2, 25.50, 2),\n" +
                    "       (46392, TO_DATE('31-05-2017', 'dd-mm-yyyy'), 2, 5.50, 2),\n" +
                    "       (57401, TO_DATE('31-05-2015', 'dd-mm-yyyy'), 2, 0.0, 2),\n" +
                    "       (68514, TO_DATE('31-03-2020', 'dd-mm-yyyy'), 1, 2.50, 3),\n" +
                    "       (79625, TO_DATE('31-01-2020', 'dd-mm-yyyy'), 1, 25.50, 4),\n" +
                    "       (90537, TO_DATE('31-12-2019', 'dd-mm-yyyy'), 2, 20.0, 5),\n" +
                    "       (18326, TO_DATE('31-12-2017', 'dd-mm-yyyy'), 2, 0.0, 5);\n" +
                    "\n" +
                    "INSERT INTO adres (adres_id, postcode, straat, huisnummer, woonplaats, reiziger_id)\n" +
                    "VALUES (1, '3511LX', 'Visschersplein ', '37', 'Utrecht', 1),\n" +
                    "       (2, '3521AL', 'Jaarbeursplein', '6A', 'Utrecht', 2),\n" +
                    "       (3, '6707AA', 'Stadsbrink', '375', 'Wageningen', 3),\n" +
                    "       (4, '3817CH', 'Arnhemseweg', '4', 'Amersfoort', 4),\n" +
                    "       (5, '3572WP', 'Vermeulenstraat ', '22', 'Utrecht', 5);\n" +
                    "\n" +
                    "INSERT INTO product (product_nummer, naam, beschrijving, prijs)\n" +
                    "VALUES (1, 'Dagkaart 2e klas', 'Een hele dag onbeperkt reizen met de trein.', 50.60),\n" +
                    "       (2, 'Dagkaart fiets', 'Uw fiets mee in de trein, 1 dag geldig in Nederland.', 6.20),\n" +
                    "       (3, 'Dal Voordeel 40%', '40% korting buiten de spits en in het weekeind.', 50.0),\n" +
                    "       (4, 'Amsterdam Travel Ticket', 'Onbeperkt reizen door Amsterdam.', 26.0),\n" +
                    "       (5, 'Railrunner', 'Voordelig reizen voor kinderen.', 2.50),\n" +
                    "       (6, 'Studentenreisproduct', 'Gratis of met korting reizen als je studeert', 0.0);\n" +
                    "\n" +
                    "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update)\n" +
                    "VALUES (35283, 3, 'actief', TO_DATE('31-05-2017', 'dd-mm-yyyy')),\n" +
                    "       (35283, 1, 'gekocht', TO_DATE('05-04-2018', 'dd-mm-yyyy')),\n" +
                    "       (35283, 2, 'gekocht', TO_DATE('05-04-2018', 'dd-mm-yyyy')),\n" +
                    "       (46392, 3, 'verlopen', TO_DATE('31-05-2017', 'dd-mm-yyyy')),\n" +
                    "       (68514, 6, 'actief', TO_DATE('01-04-2018', 'dd-mm-yyyy')),\n" +
                    "       (79625, 6, 'actief', TO_DATE('01-02-2018', 'dd-mm-yyyy')),\n" +
                    "       (90537, 3, 'actief', TO_DATE('01-02-2018', 'dd-mm-yyyy')),\n" +
                    "       (90537, 2, 'gekocht', TO_DATE('14-04-2018', 'dd-mm-yyyy'));\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}