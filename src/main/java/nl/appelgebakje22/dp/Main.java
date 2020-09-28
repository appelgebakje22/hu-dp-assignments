package nl.appelgebakje22.dp;

import nl.appelgebakje22.dp.dao.AdresDAO;
import nl.appelgebakje22.dp.dao.OVChipkaartDAO;
import nl.appelgebakje22.dp.dao.ProductDAO;
import nl.appelgebakje22.dp.dao.ReizigerDAO;
import nl.appelgebakje22.dp.domain.Adres;
import nl.appelgebakje22.dp.domain.OVChipkaart;
import nl.appelgebakje22.dp.domain.Product;
import nl.appelgebakje22.dp.domain.Reiziger;
import nl.appelgebakje22.dp.impl.AdresDAOImpl;
import nl.appelgebakje22.dp.impl.OVChipkaartDAOImpl;
import nl.appelgebakje22.dp.impl.ProductDAOImpl;
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
    private final ProductDAO pdao;

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
        this.pdao = new ProductDAOImpl(this.conn);
        this.doTests();
        try {
            this.conn.close();
        } catch (SQLException ignored) {
        }
    }

    private void doTests() {
        OVChipkaart ov = this.odao.findById(35283);
        System.out.println("OV 35283 heeft " + ov.getProductList().size() + " producten");
        Product p = ov.getProductList().get(0).resolve().get();
        this.pdao.delete(p);
        ov = this.odao.findById(35283);
        System.out.println("OV 35283 heeft " + ov.getProductList().size() + " producten");
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
            ProductDAO pdao = new ProductDAOImpl(this.conn);
            pdao.delete(pdao.findById(TEST_ID));
        });
        Helper.doSilently(() -> {
            OVChipkaartDAO odao = new OVChipkaartDAOImpl(this.conn);
            odao.delete(odao.findById(TEST_ID));
        });
    }
}