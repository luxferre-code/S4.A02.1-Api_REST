package fr.valentinthuillier.sae.dto;

import java.time.LocalDate;

public class Commande {
    
    private int id;
    private String nom;
    private CommandePizza pizzas;
    private LocalDate date;

    public Commande(int id, String nom, CommandePizza pizzas, LocalDate date) {
        this.id = id;
        this.nom = nom;
        this.pizzas = pizzas;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public CommandePizza getPizzas() {
        return pizzas;
    }

    public void setPizzas(CommandePizza pizzas) {
        this.pizzas = pizzas;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String toString() {
        return "Commande [id=" + id + ", nom=" + nom + ", pizzas=" + pizzas + " date=" + date + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((pizzas == null) ? 0 : pizzas.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
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
        Commande other = (Commande) obj;
        if (id != other.id)
            return false;
        if (nom == null) {
            if (other.nom != null)
                return false;
        } else if (!nom.equals(other.nom))
            return false;
        if (pizzas == null) {
            if (other.pizzas != null)
                return false;
        } else if (!pizzas.equals(other.pizzas))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        return true;
    }

    

}
