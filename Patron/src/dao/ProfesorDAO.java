// ProfesorDAO.java
package dao;

import entities.Profesor;
import java.util.List;

public interface ProfesorDAO {
    Profesor obtenerProfesorPorId(String id);
    List<Profesor> obtenerTodosLosProfesores();
    List<String> obtenerModulosPorProfesor(String codProfesor);
}