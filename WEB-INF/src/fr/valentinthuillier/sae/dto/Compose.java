package fr.valentinthuillier.sae.dto;

import java.util.Arrays;

/**
 * Compose Class - Cette classe permet de manipuler les objets de type Compose.
 *
 * @author Valentin THUILLIER
 * @see Ingredient
 * @see Pizza
 */
public class Compose {

    private int id_pizza;
    private Ingredient[] ingredients;

    public Compose() {
        // Do nothing
    }

    public Compose(int pizza, Ingredient[] ingredients) {
        this.id_pizza = pizza;
        this.ingredients = ingredients;
    }

    public Compose(Pizza pizza, Ingredient[] ingredients) {
        this.id_pizza = pizza.getId();
        this.ingredients = ingredients;
    }

    public int getId_pizza() {
        return id_pizza;
    }

    public void setId_pizza(int id_pizza) {
        this.id_pizza = id_pizza;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id_pizza;
        result = prime * result + Arrays.hashCode(ingredients);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Compose other = (Compose) obj;
        if(id_pizza != other.id_pizza)
            return false;
        return Arrays.equals(ingredients, other.ingredients);
    }

    @Override
    public String toString() {
        return "Compose [id_pizza=" + id_pizza + ", ingredients=" + Arrays.toString(ingredients) + "]";
    }


}
