package fr.valentinthuillier.sae.dao;

/**
 * IDao Interface - Cette interface permet de définir les méthodes permettant de manipuler les objets de la base de données.
 * @param <T> Type de l'objet à manipuler
 * @version 1.0
 * @author Valentin THUILLIER
 */
public interface IDao<T extends Object> {

    public T findById(int id);

    public T[] findAll();

    public boolean save(T object);

    public boolean update(T object);

    public boolean remove(T object);

}
