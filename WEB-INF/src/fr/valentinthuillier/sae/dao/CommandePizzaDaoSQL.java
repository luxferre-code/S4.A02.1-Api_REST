package fr.valentinthuillier.sae.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import fr.valentinthuillier.sae.DS;
import fr.valentinthuillier.sae.dto.CommandePizza;
import fr.valentinthuillier.sae.dto.Pizza;

public class CommandePizzaDaoSQL implements IDao<CommandePizza> {

    @Override
    public CommandePizza findById(int id) {
        CommandePizza commandePizza = null;
        try (Connection con = DS.getConnection()) {
            
            PreparedStatement ps = con.prepareStatement("SELECT pizza FROM commande_pizza WHERE commande = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            List<Pizza> pizzas = new ArrayList<>();
            IDao<Pizza> pizzaDao = new PizzaDaoSQL();
            
            while (rs.next()) {
                pizzas.add(pizzaDao.findById(rs.getInt("pizza")));
            }

            commandePizza = new CommandePizza(id, pizzas.toArray(new Pizza[0]));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return commandePizza;
    }

    @Override
    public CommandePizza[] findAll() {
        List<CommandePizza> commandePizzas = new ArrayList<>();
        try (Connection con = DS.getConnection()) {

            PreparedStatement ps = con.prepareStatement("SELECT commande FROM commande_pizza");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                commandePizzas.add(findById(rs.getInt("commande")));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return commandePizzas.toArray(new CommandePizza[0]);
    }

    @Override
    public boolean save(CommandePizza object) {
        try (Connection con = DS.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO commande_pizza (commande, pizza) VALUES (?, ?)");
            for (Pizza pizza : object.getPizzas()) {
                ps.setInt(1, object.getId_commande());
                ps.setInt(2, pizza.getId());
                ps.addBatch();
            }
            return ps.executeBatch().length > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(CommandePizza object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public boolean remove(CommandePizza object) {
        try (Connection con = DS.getConnection()) {
            
            PreparedStatement ps = con.prepareStatement("DELETE FROM commande_pizza WHERE commande = ?");
            ps.setInt(1, object.getId_commande());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
}
