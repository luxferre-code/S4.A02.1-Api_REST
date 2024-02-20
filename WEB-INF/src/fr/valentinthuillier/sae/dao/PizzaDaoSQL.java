package fr.valentinthuillier.sae.dao;

import java.sql.Connection;

import fr.valentinthuillier.sae.DS;
import fr.valentinthuillier.sae.dto.Pizza;

public class PizzaDaoSQL implements IDao<Pizza> {

    private static final IngredientDaoSQL ingredientDao = new IngredientDaoSQL();

    public PizzaDaoSQL() {
        // Do nothing
    }

    @Override
    public Pizza findById(int id) {
        Pizza pizza = null;
        try(Connection con = DS.getConnection()) {

            

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return pizza;
    }

    @Override
    public Pizza[] findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public boolean save(Pizza object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public boolean update(Pizza object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean remove(Pizza object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }


    
}
