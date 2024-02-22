package fr.valentinthuillier.sae.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fr.valentinthuillier.sae.DS;
import fr.valentinthuillier.sae.dto.Commande;

public class CommandeDaoSQL implements IDao<Commande> {

    @Override
    public Commande findById(int id) {
        Commande commande = null;

        try (Connection con = DS.getConnection()) {
            
            PreparedStatement ps = con.prepareStatement("SELECT nom, date FROM commande WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                commande = new Commande(id, rs.getString("nom"), new CommandePizzaDaoSQL().findById(id), rs.getDate("date").toLocalDate());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Erreur ici !");
            e.printStackTrace();
        }
        System.out.println(commande);
        return commande;
    }

    @Override
    public Commande[] findAll() {
        List<Commande> commandes = new ArrayList<>();
        try (Connection con = DS.getConnection()) {

            PreparedStatement ps = con.prepareStatement("SELECT id FROM commande");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Commande cmd = findById(rs.getInt("id"));
                commandes.add(cmd);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return commandes.toArray(new Commande[0]);
    }

    @Override
    public boolean save(Commande object) {
        try (Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO commande (id, nom, date) VALUES (?, ?, ?)");
            ps.setInt(1, object.getId());
            ps.setString(2, object.getNom());
            ps.setDate(3, Date.valueOf(object.getDate().toString()));
            return ps.executeUpdate() == 1 && new CommandePizzaDaoSQL().save(object.getPizzas());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Commande object) {
        try (Connection con = DS.getConnection()) {
            
            PreparedStatement ps = con.prepareStatement("UPDATE commande SET nom = ?, date = ? WHERE id = ?");
            ps.setString(1, object.getNom());
            ps.setDate(2, Date.valueOf(object.getDate().toString()));
            ps.setInt(3, object.getId());
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean remove(Commande object) {
        try (Connection con = DS.getConnection()) {
            
            PreparedStatement ps = con.prepareStatement("DELETE FROM commande WHERE id = ?");
            ps.setInt(1, object.getId());
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    
}
