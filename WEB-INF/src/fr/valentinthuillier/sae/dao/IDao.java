package fr.valentinthuillier.sae.dao;

/**
 * IDao Interface - Cette interface permet de définir les méthodes permettant de manipuler les objets de la base de données.
 *
 * @param <T> Type de l'objet à manipuler
 * @author Valentin THUILLIER
 * @version 1.0
 */
public interface IDao<T extends Object> {

    T findById(int id);

    T[] findAll();

    boolean save(T object);

    boolean update(T object);

    boolean remove(T object);

}
