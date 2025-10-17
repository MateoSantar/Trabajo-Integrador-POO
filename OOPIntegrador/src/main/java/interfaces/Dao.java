package interfaces;

import java.util.List;

/**
 * Interfaz genérica DAO (Data Access Object) para operaciones CRUD.
 * <p>
 * Define los métodos básicos de persistencia que deben implementar todas las
 * clases DAO del proyecto. Se utiliza con tipos genéricos para que pueda
 * reutilizarse con cualquier entidad del dominio.
 * </p>
 *
 * <p>CRUD significa:</p>
 * <ul>
 *   <li><b>C</b>reate – Guardar un nuevo objeto.</li>
 *   <li><b>R</b>ead – Obtener uno o varios objetos.</li>
 *   <li><b>U</b>pdate – Actualizar un objeto existente.</li>
 *   <li><b>D</b>elete – Eliminar un objeto existente.</li>
 * </ul>
 *
 * @param <T> el tipo de entidad con el que trabaja el DAO
 * @author
 *   Mateo Santarsiero
 * @version 1.0
 */
public interface Dao<T> {

    /**
     * Obtiene un objeto por su identificador único.
     *
     * @param id identificador único
     * @return el objeto encontrado o {@code null} si no existe
     */
    T Get(long id);

    /**
     * Devuelve una lista con todos los objetos almacenados.
     *
     * @return lista de objetos
     */
    List<T> getAll();

    /**
     * Guarda un nuevo objeto en la fuente de datos.
     *
     * @param t objeto a guardar
     */
    void save(T t);

    /**
     * Actualiza un objeto existente en la fuente de datos.
     *
     * @param oldOne objeto existente a actualizar
     * @param newOne objeto con los nuevos valores
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario
     */
    boolean update(T oldOne, T newOne);

    /**
     * Elimina un objeto de la fuente de datos.
     *
     * @param t objeto a eliminar
     */
    void delete(T t);
}
