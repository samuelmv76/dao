// AlumnoService.java
package service;

import dao.AlumnoDAO;
import entities.Alumno;
import dto.AlumnoDTO;
import java.util.List;
import java.util.stream.Collectors;

public class AlumnoService {
    private AlumnoDAO alumnoDAO;
    
    public AlumnoService(AlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }
    
    public void crearAlumno(Alumno alumno) {
        alumnoDAO.crearAlumno(alumno);
    }
    
    public AlumnoDTO obtenerAlumnoPorId(String id) {
        Alumno alumno = alumnoDAO.obtenerAlumnoPorId(id);
        if (alumno != null) {
            return convertirAAlumnoDTO(alumno);
        }
        return null;
    }
    
    public List<AlumnoDTO> obtenerTodosLosAlumnos() {
        List<Alumno> alumnos = alumnoDAO.obtenerTodosLosAlumnos();
        return alumnos.stream()
                     .map(this::convertirAAlumnoDTO)
                     .collect(Collectors.toList());
    }
    
    public void actualizarAlumno(Alumno alumno) {
        alumnoDAO.actualizarAlumno(alumno);
    }
    
    public void eliminarAlumno(String id) {
        alumnoDAO.eliminarAlumno(id);
    }
    
    private AlumnoDTO convertirAAlumnoDTO(Alumno alumno) {
        String nombreCompleto = alumno.getNombreAlumno() + " " + alumno.getApellidosAlumno();
        return new AlumnoDTO(
            alumno.getCodAlumno(),
            nombreCompleto,
            alumno.getFechaNacimiento(),
            String.valueOf(alumno.getGrupo())
        );
    }
}