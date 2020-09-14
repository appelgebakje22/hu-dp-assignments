package nl.appelgebakje22.dp;

import nl.appelgebakje22.dp.dao.AdresDAO;
import nl.appelgebakje22.dp.dao.OVChipkaartDAO;
import nl.appelgebakje22.dp.dao.ReizigerDAO;
import nl.appelgebakje22.dp.domain.Adres;
import nl.appelgebakje22.dp.domain.OVChipkaart;
import nl.appelgebakje22.dp.domain.Reiziger;
import nl.appelgebakje22.dp.impl.AdresDAOImpl;
import nl.appelgebakje22.dp.impl.OVChipkaartDAOImpl;
import nl.appelgebakje22.dp.impl.ReizigerDAOImpl;
import nl.appelgebakje22.dp.lib.Helper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Main {

    private static final int TEST_ID = 77;

    public static void main(String[] args) {
        new Main();
    }

    private final Connection conn;
    private final ReizigerDAO rdao;
    private final AdresDAO adao;
    private final OVChipkaartDAO odao;

    public Main() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {
            throw new RuntimeException("Couldn't initialize PostgreSQL Driver ");
        }
        try {
            Properties dbProps = Helper.getDatabaseProps();
            this.conn = DriverManager.getConnection("jdbc:postgresql://" + dbProps.getProperty("host") + "/ovchiphu", dbProps.getProperty("username"), dbProps.getProperty("password"));
            this.conn.setAutoCommit(true);
        } catch (SQLException e) {
            RuntimeException e2 = new RuntimeException();
            e2.setStackTrace(e.getStackTrace());
            throw e2;
        }
        Seeder.resetDatabase(this.conn);
        this.rdao = new ReizigerDAOImpl(this.conn);
        this.adao = new AdresDAOImpl(this.conn);
        this.odao = new OVChipkaartDAOImpl(this.conn);
        this.doTests();
        try {
            this.conn.close();
        } catch (SQLException ignored) {
        }
    }

    private void doTests() {
        System.out.println("Initial state");
        this.printSizes();

        Adres adres = new Adres(TEST_ID, "1234AB", "11", "Straat", "Nieuwegein", TEST_ID);
        Reiziger reiziger = new Reiziger(TEST_ID, "DA", "van den", "Ham", Date.valueOf("1995-12-03"), adres, new ArrayList<>());

        if (this.rdao.save(reiziger)) {
            System.out.println("[Reiziger]Save success!");
            this.printSizes();

        } else {
            System.err.println("[Reiziger]Save errored!");
            return;
        }

        if (this.rdao.delete(reiziger)) {
            System.out.println("[Reiziger]Delete success!");
            this.printSizes();
        } else {
            System.err.println("[Reiziger]Delete errored!");
            return;
        }

        OVChipkaart ov = new OVChipkaart(TEST_ID, Date.valueOf("2020-01-01"), 1, 1.0F, TEST_ID);
        reiziger.getOvList().add(ov);
        if (this.rdao.save(reiziger)) {
            System.out.println("[Reiziger - OVChip]Save success!");
            this.printSizes();

        } else {
            System.err.println("[Reiziger - OVChip]Save errored!");
            return;
        }

        reiziger.getOvList().remove(ov);
        if (this.rdao.update(reiziger)) {
            System.out.println("[Reiziger - OVChip]Delete success!");
            this.printSizes();
        } else {
            System.err.println("[Reiziger - OVChip]Delete errored!");
            return;
        }
    }

    private void printSizes() {
        System.out.println("\tNumber of reizigers: " + this.rdao.findAll().size());
        System.out.println("\tNumber of adressen: " + this.adao.findAll().size());
        System.out.println("\tNumber of ovChipkaarten: " + this.odao.findAll().size());
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
        Helper.doSilently(() -> {
            OVChipkaartDAO odao = new OVChipkaartDAOImpl(this.conn);
            odao.delete(odao.findById(TEST_ID));
        });
    }
}