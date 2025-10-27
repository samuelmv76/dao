package service;

import dao.ProfesorDAO;
import dao.ProfesorDAOImplementacion;
import entities.Profesor;
import entities.Modulo;
import dto.ProfesorDTO;
import java.util.List;
import java.util.stream.Collectors;

public class ProfesorService {
    private ProfesorDAO profesorDAO;
    
    public ProfesorService(ProfesorDAO profesorDAO) {
        this.profesorDAO = profesorDAO;
    }
    
    // Métodos básicos
    public ProfesorDTO obtenerProfesorPorId(String id) {
        Profesor profesor = profesorDAO.obtenerProfesorPorId(id);
        if (profesor != null) {
            List<String> modulos = profesorDAO.obtenerModulosPorProfesor(id);
            return convertirAProfesorDTO(profesor, modulos);
        }
        return null;
    }
    
    public List<ProfesorDTO> obtenerTodosLosProfesores() {
        List<Profesor> profesores = profesorDAO.obtenerTodosLosProfesores();
        return profesores.stream()
                        .map(profesor -> {
                            List<String> modulos = profesorDAO.obtenerModulosPorProfesor(profesor.getCodProfesor());
                            return convertirAProfesorDTO(profesor, modulos);
                        })
                        .collect(Collectors.toList());
    }
    
    // Métodos adicionales que faltaban
    public Profesor obtenerProfesorPorModulo(String codModulo) {
        // Cast a la implementación concreta para acceder a métodos adicionales
        if (profesorDAO instanceof ProfesorDAOImplementacion) {
            ProfesorDAOImplementacion profesorDAOImpl = (ProfesorDAOImplementacion) profesorDAO;
            return profesorDAOImpl.obtenerProfesorPorModulo(codModulo);
        }
        return null;
    }
    
    public List<Object[]> obtenerAlumnosYProfesorPorModulo(String codModulo) {
        if (profesorDAO instanceof ProfesorDAOImplementacion) {
            ProfesorDAOImplementacion profesorDAOImpl = (ProfesorDAOImplementacion) profesorDAO;
            return profesorDAOImpl.obtenerAlumnosYProfesorPorModulo(codModulo);
        }
        return List.of(); // Retorna lista vacía si no se puede castear
    }
    
    public List<Modulo> obtenerModulosCompletosPorProfesor(String codProfesor) {
        if (profesorDAO instanceof ProfesorDAOImplementacion) {
            ProfesorDAOImplementacion profesorDAOImpl = (ProfesorDAOImplementacion) profesorDAO;
            return profesorDAOImpl.obtenerModulosCompletosPorProfesor(codProfesor);
        }
        return List.of();
    }
    
    // Método para obtener profesor con información completa (sin DTO especial)
    public ProfesorDTO obtenerProfesorCompletoPorId(String id) {
        Profesor profesor = profesorDAO.obtenerProfesorPorId(id);
        if (profesor != null) {
            List<Modulo> modulosCompletos = obtenerModulosCompletosPorProfesor(id);
            // Convertir módulos completos a lista de nombres para el DTO estándar
            List<String> nombresModulos = modulosCompletos.stream()
                    .map(Modulo::getNombreModulo)
                    .collect(Collectors.toList());
            return convertirAProfesorDTO(profesor, nombresModulos);
        }
        return null;
    }
    
    // Método auxiliar para calcular horas totales
    public int obtenerHorasTotalesProfesor(String codProfesor) {
        List<Modulo> modulos = obtenerModulosCompletosPorProfesor(codProfesor);
        return modulos.stream()
                .mapToInt(Modulo::getHorasSemanales)
                .sum();
    }
    
    private ProfesorDTO convertirAProfesorDTO(Profesor profesor, List<String> modulos) {
        String nombreCompleto = profesor.getNombreProfesor() + " " + profesor.getApellidosProfesor();
        return new ProfesorDTO(
            profesor.getCodProfesor(),
            nombreCompleto,
            modulos
        );
    }
}