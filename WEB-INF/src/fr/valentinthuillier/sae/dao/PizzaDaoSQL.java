package fr.valentinthuillier.sae.dao;

import fr.valentinthuillier.sae.DS;
import fr.valentinthuillier.sae.dto.Pizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * PizzaDaoSQL Class - Cette classe permet de manipuler les objets de type Pizza dans la base de données.
 *
 * @author Valentin THUILLIER
 * @see IDao
 * @see Pizza
 * @see IngredientDaoSQL
 * @see ComposeDaoSQL
 * @see Ingredient
 * @see Compose
 * @see DS
 */
public class PizzaDaoSQL implements IDao<Pizza> {

    private static final IngredientDaoSQL ingredientDao = new IngredientDaoSQL();
    private static final ComposeDaoSQL composeDao = new ComposeDaoSQL();

    public PizzaDaoSQL() {
        // Do nothing
    }

    @Override
    public Pizza findById(int id) {
        Pizza pizza = null;
        try(Connection con = DS.getConnection()) {

            PreparedStatement ps = con.prepareStatement("SELECT nom, pate, prixBase FROM pizza WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                pizza = new Pizza(id, rs.getString("nom"), ingredientDao.findById(rs.getInt("pate")), rs.getDouble("prixBase"), composeDao.findById(id));
            }
            ps.close();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("id : " + id + " pizza : " + pizza);
        return pizza;
    }

    @Override
    public Pizza[] findAll() {
        List<Pizza> pizzas = new ArrayList<>();
        try(Connection con = DS.getConnection()) {

            PreparedStatement ps = con.prepareStatement("SELECT id, nom, pate, prixBase FROM pizza");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                pizzas.add(new Pizza(rs.getInt("id"), rs.getString("nom"), ingredientDao.findById(rs.getInt("pate")), rs.getDouble("prixBase"), composeDao.findById(rs.getInt("id"))));
            ps.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return pizzas.toArray(new Pizza[0]);
    }

    @Override
    public boolean save(Pizza object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps;
            if(object.getId() <= 0) {
                ps = con.prepareStatement("INSERT INTO pizza(nom, pate, prixBase) VALUES(?, ?, ?)");
                ps.setString(1, object.getNom());
                ps.setInt(2, object.getPate().getId());
                ps.setDouble(3, object.getPrix());
            } else {
                ps = con.prepareStatement("INSERT INTO pizza(id, nom, pate, prixBase) VALUES(?, ?, ?, ?)");
                ps.setInt(1, object.getId());
                ps.setString(2, object.getNom());
                ps.setInt(3, object.getPate().getId());
                ps.setDouble(4, object.getPrix());
            }
            if(ps.executeUpdate() <= 0) return false;
            ps.close();
            try { return composeDao.save(object.getIngredients()); }
            catch(Exception e) { return false; }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Pizza object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE pizza SET nom = ?, pate = ?, prixBase = ? WHERE id = ?");
            ps.setString(1, object.getNom());
            ps.setInt(2, object.getPate().getId());
            ps.setDouble(3, object.getPrix());
            ps.setInt(4, object.getId());
            ps.executeUpdate();
            return composeDao.update(object.getIngredients());
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean remove(Pizza object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM pizza WHERE id = ?");
            ps.setInt(1, object.getId());
            ps.executeUpdate();
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


}
