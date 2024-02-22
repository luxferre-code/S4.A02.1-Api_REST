package fr.valentinthuillier.sae.dto;

/**
 * Pizza Class - Cette classe permet de manipuler les objets de type Pizza.
 *
 * @author Valentin THUILLIER
 * @see Ingredient
 */
public class Pizza {

    private int id;
    private String nom;
    private Ingredient pate;
    private double prix;
    private Compose ingredients;

    public Pizza() {
        // Do nothing
    }

    public Pizza(int id, String nom, Ingredient pate, double prix, Compose ingredients) {
        this.id = id;
        this.nom = nom;
        this.pate = pate;
        this.prix = prix;
        this.ingredients = ingredients;
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

    public Ingredient getPate() {
        return pate;
    }

    public void setPate(Ingredient pate) {
        this.pate = pate;
    }

    public double getPrix() {
        return prix;
    }

    public double prixFinal() {
        double prix = 0;
        for(Ingredient ingredient : ingredients.getIngredients()) {
            prix += ingredient.getPrix();
        }
        return prix + this.prix + this.pate.getPrix();
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Compose getIngredients() {
        return ingredients;
    }

    public void setIngredients(Compose ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((pate == null) ? 0 : pate.hashCode());
        long temp;
        temp = Double.doubleToLongBits(prix);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ingredients.hashCode();
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
        Pizza other = (Pizza) obj;
        if(id != other.id)
            return false;
        if(nom == null) {
            if(other.nom != null)
                return false;
        } else if(!nom.equals(other.nom))
            return false;
        if(pate == null) {
            if(other.pate != null)
                return false;
        } else if(!pate.equals(other.pate))
            return false;
        if(Double.doubleToLongBits(prix) != Double.doubleToLongBits(other.prix))
            return false;
        return ingredients.equals(other.ingredients);
    }

    @Override
    public String toString() {
        return "Pizza [id=" + id + ", nom=" + nom + ", pate=" + pate + ", prix=" + prix + ", ingredients="
                + ingredients + "]";
    }

}
