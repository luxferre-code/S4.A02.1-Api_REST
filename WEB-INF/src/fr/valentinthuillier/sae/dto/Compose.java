package fr.valentinthuillier.sae.dto;

public class Compose {

    private Ingredient[] ingredients;

    public Compose() {
        // Do nothing
    }

    public Compose(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public String toString() {
        return "Compose [ingredients=" + ingredients + "]";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Compose compose = (Compose) o;

        return ingredients != null ? ingredients.equals(compose.ingredients) : compose.ingredients == null;
    }

    public int hashCode() {
        return ingredients != null ? ingredients.hashCode() : 0;
    }
    
}
