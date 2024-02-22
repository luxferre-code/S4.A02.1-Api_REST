package fr.valentinthuillier.sae.dto;

import java.util.Arrays;
import java.util.List;

public class CommandePizza {

    private int id_commande;
    private Pizza[] pizzas;

    public CommandePizza() {
        // Do nothing
    }

    public CommandePizza(int id_commande, Pizza[] pizzas) {
        this.id_commande = id_commande;
        this.pizzas = pizzas;
    }

    public int getId_commande() {
        return id_commande;
    }

    public void setId_commande(int id_commande) {
        this.id_commande = id_commande;
    }

    public Pizza[] getPizzas() {
        return pizzas;
    }

    public void setPizzas(Pizza[] pizzas) {
        this.pizzas = pizzas;
    }

    public String toString() {
        return "CommandePizza [id_commande=" + id_commande + ", pizzas=" + List.of(pizzas) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id_commande;
        result = prime * result + Arrays.hashCode(pizzas);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CommandePizza other = (CommandePizza) obj;
        if (id_commande != other.id_commande)
            return false;
        if (!Arrays.equals(pizzas, other.pizzas))
            return false;
        return true;
    }

}
