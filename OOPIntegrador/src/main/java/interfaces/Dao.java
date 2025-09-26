/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;

/**
 *
 * @author idra2
 * @param <T>
 */
public interface Dao<T> {
    
    T Get(long id);
   
    List<T> getAll();
    
    void save(T t);
   
    boolean update(T oldOne, T newOne);
    
    void delete(T t);
}
