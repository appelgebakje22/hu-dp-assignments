package nl.appelgebakje22.dp.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.appelgebakje22.dp.dao.AdresDAO;
import nl.appelgebakje22.dp.dao.OVChipkaartDAO;
import nl.appelgebakje22.dp.dao.ReizigerDAO;
import nl.appelgebakje22.dp.domain.Adres;
import nl.appelgebakje22.dp.domain.OVChipkaart;
import nl.appelgebakje22.dp.domain.Reiziger;

public final class ReizigerDAOImpl extends AbstractDAOImpl<Reiziger> implements ReizigerDAO {

    private final AdresDAO adao;
    private final OVChipkaartDAO odao;

    public ReizigerDAOImpl(Connection conn) {
        super(conn, Reiziger.class);
        this.adao = new AdresDAOImpl(conn);
        this.odao = new OVChipkaartDAOImpl(conn);
    }

    @Override
    public boolean save(Reiziger entity) {
        try (PreparedStatement stmt = this.conn.prepareStatement(this.createInsertQuery())) {
            this.mapData(stmt, new Object[]{
                    entity.getId(),
                    entity.getVoorletters(),
                    entity.getTussenvoegsel(),
                    entity.getAchternaam(),
                    entity.getGeboortedatum()
            });
            if (stmt.executeUpdate() == 1) {
                List<OVChipkaart> currentCards = this.odao.findByReiziger(entity);
                if (!currentCards.stream().allMatch(ov -> {
                    if (entity.getOvList().stream().anyMatch(myOv -> myOv.getKaart_nummer() == ov.getKaart_nummer())) {
                        return this.odao.update(ov);
                    } else {
                        return this.odao.delete(ov);
                    }
                })) {
                    return false;
                }
                if (!entity.getOvList().stream().allMatch(ov ->
                        currentCards.stream().anyMatch(myOv -> myOv.getKaart_nummer() == ov.getKaart_nummer()) || this.odao.save(ov)
                )) {
                    return false;
                }
                Adres adres = entity.getAdres();
                return this.adao.findById(adres.getId()) != null ? this.adao.update(adres) : this.adao.save(adres);
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Reiziger entity) {
        try (PreparedStatement stmt = this.conn.prepareStatement(this.createUpdateQuery())) {
            stmt.setString(1, entity.getVoorletters());
            stmt.setString(2, entity.getTussenvoegsel());
            stmt.setString(3, entity.getAchternaam());
            stmt.setDate(4, entity.getGeboortedatum());
            stmt.setInt(5, entity.getId());
            if (stmt.executeUpdate() == 1) {
                if (!this.odao.findByReiziger(entity).stream().allMatch(ov -> {
                    if (entity.getOvList().stream().anyMatch(myOv -> myOv.getKaart_nummer() == ov.getKaart_nummer())) {
                        return odao.update(ov) || odao.save(ov);
                    } else {
                        return this.odao.delete(ov);
                    }
                })) {
                    return false;
                }
                Adres adres = entity.getAdres();
                return this.adao.findById(adres.getId()) != null ? this.adao.update(adres) : adao.save(adres);
            }
            return false;
        } catch (SQLException e) {
            System.err.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger entity) {
        if (!this.adao.delete(entity.getAdres())) {
            return false;
        }
        if (!entity.getOvList().stream().allMatch(this.odao::delete)) {
            return false;
        }
        try (PreparedStatement stmt = this.conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?")) {
            stmt.setInt(1, entity.getId());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println(e.toString());
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
            System.err.println(e.toString());
            return Collections.emptyList();
        }
    }

    @Override
    protected Reiziger mapEntity(ResultSet set) throws SQLException {
        Reiziger result = new Reiziger();
        result.setId(set.getInt("reiziger_id"));
        result.setVoorletters(set.getString("voorletters"));
        result.setTussenvoegsel(set.getString("tussenvoegsel"));
        result.setAchternaam(set.getString("achternaam"));
        result.setGeboortedatum(set.getDate("geboortedatum"));
        result.setAdres(this.adao.findByReiziger(result));
        result.setOvList(this.odao.findByReiziger(result));
        return result;
    }
}