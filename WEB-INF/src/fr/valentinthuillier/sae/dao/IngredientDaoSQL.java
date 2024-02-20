package fr.valentinthuillier.sae.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fr.valentinthuillier.sae.DS;
import fr.valentinthuillier.sae.dto.Ingredient;

/**
 * IngredientDao Class - Cette classe permet de manipuler les objets de type Ingredient dans la base de données.
 * @see IDao
 * @see Ingredient
 * @see DS
 * @author Valentin THUILLIER
 */
public class IngredientDaoSQL implements IDao<Ingredient> {
    
    public IngredientDaoSQL() {
        // Do nothing
    }

    @Override
    public Ingredient findById(int id) {
        Ingredient ingredient = null;
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT nom, prix FROM ingredient WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                ingredient = new Ingredient(id, rs.getString("nom"), rs.getDouble("prix"));
            ps.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return ingredient;
    }

    @Override
    public Ingredient[] findAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT id, nom, prix FROM ingredient");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                ingredients.add(new Ingredient(rs.getInt("id"), rs.getString("nom"), rs.getDouble("prix")));
            ps.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return ingredients.toArray(new Ingredient[0]);
    }

    @Override
    public boolean save(Ingredient object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO ingredient(id, nom, prix) VALUES(?, ?, ?)");
            ps.setInt(1, object.getId());
            ps.setString(2, object.getNom());
            ps.setDouble(3, object.getPrix());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Ingredient object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE ingredient SET nom = ?, prix = ? WHERE id = ?");
            ps.setString(1, object.getNom());
            ps.setDouble(2, object.getPrix());
            ps.setInt(3, object.getId());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean remove(Ingredient object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM ingredient WHERE id = ?");
            ps.setInt(1, object.getId());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
}
