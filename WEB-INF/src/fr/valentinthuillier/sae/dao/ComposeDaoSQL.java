package fr.valentinthuillier.sae.dao;

import fr.valentinthuillier.sae.DS;
import fr.valentinthuillier.sae.dto.Compose;
import fr.valentinthuillier.sae.dto.Ingredient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * ComposeDaoSQL Class - Cette classe permet de manipuler les objets de type Compose dans la base de données.
 *
 * @author Valentin THUILLIER
 * @see IDao
 * @see Compose
 * @see Ingredient
 * @see DS
 * @see IngredientDaoSQL
 */
public class ComposeDaoSQL implements IDao<Compose> {

    private static final IngredientDaoSQL ingredientDao = new IngredientDaoSQL();

    @Override
    public Compose findById(int id_pizza) {
        Compose compose = null;
        List<Ingredient> ingredients = new ArrayList<>();
        try(Connection con = DS.getConnection()) {

            PreparedStatement ps = con.prepareStatement("SELECT ingredient FROM compose WHERE pizza = ?");
            ps.setInt(1, id_pizza);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                ingredients.add(ingredientDao.findById(rs.getInt("ingredient")));
            compose = new Compose(id_pizza, ingredients.toArray(new Ingredient[0]));
            ps.close();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return compose;
    }

    public boolean removeSpecifique(int pizza, int ingredient) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM compose WHERE pizza = ? AND ingredient = ?");
            ps.setInt(1, pizza);
            ps.setInt(2, ingredient);
            ps.executeUpdate();
            return ps.getUpdateCount() > 0;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Compose[] findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public boolean save(Compose object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO compose(pizza, ingredient) VALUES(?, ?)");
            for(Ingredient ingredient : object.getIngredients()) {
                ps.setInt(1, object.getId_pizza());
                ps.setInt(2, ingredient.getId());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Compose object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM compose WHERE pizza = ?");
            ps.setInt(1, object.getId_pizza());
            ps.executeUpdate();
            return save(object);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean remove(Compose object) {
        try(Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM compose WHERE pizza = ?");
            ps.setInt(1, object.getId_pizza());
            ps.executeUpdate();
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
