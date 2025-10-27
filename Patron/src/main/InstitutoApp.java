package main;

import dao.AlumnoDAOImplementacion;
import dao.ProfesorDAOImplementacion;
import service.AlumnoService;
import service.ProfesorService;
import entities.Alumno;
import entities.Profesor;
import entities.Modulo;
import dto.AlumnoDTO;
import dto.ProfesorDTO;
import config.DatabaseConfig;

import java.util.Date;
import java.util.List;

public class InstitutoApp {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTIÓN DE INSTITUTO DAM ===\n");
        
        // Probar conexión
        System.out.println("🔍 Probando conexión...");
        DatabaseConfig.probarConexion();
        System.out.println();
        
        try {
            // Inicializar servicios
            AlumnoDAOImplementacion alumnoDAO = AlumnoDAOImplementacion.getInstancia();
            ProfesorDAOImplementacion profesorDAO = ProfesorDAOImplementacion.getInstancia();
            
            AlumnoService alumnoService = new AlumnoService(alumnoDAO);
            ProfesorService profesorService = new ProfesorService(profesorDAO);
            
            // Mostrar distribución de módulos primero
            profesorDAO.mostrarDistribucionModulos();
            
            // Pruebas con Alumnos
            probarOperacionesAlumnos(alumnoService);
            
            // Pruebas con Profesores  
            probarOperacionesProfesores(profesorService);
            
            // Pruebas con Módulos
            probarOperacionesModulos(profesorService);
            
        } catch (Exception e) {
            System.err.println("❌ Error en la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void probarOperacionesAlumnos(AlumnoService alumnoService) {
        System.out.println("=== PRUEBAS CON ALUMNOS DAM ===");
        
        try {
            // Obtener alumno específico
            System.out.println("1. Obteniendo alumno 01...");
            AlumnoDTO alumnoDTO = alumnoService.obtenerAlumnoPorId("01");
            if (alumnoDTO != null) {
                System.out.println("✅ Alumno obtenido: " + alumnoDTO);
            }
            
            // Obtener todos los alumnos
            System.out.println("\n2. Obteniendo todos los alumnos...");
            List<AlumnoDTO> todosAlumnos = alumnoService.obtenerTodosLosAlumnos();
            System.out.println("✅ Total de alumnos DAM: " + todosAlumnos.size());
            
            // Mostrar primeros 5 alumnos
            System.out.println("\n📋 Primeros 5 alumnos:");
            todosAlumnos.stream().limit(5).forEach(System.out::println);
            
        } catch (Exception e) {
            System.err.println("❌ Error en operaciones de alumnos: " + e.getMessage());
        }
    }
    
    private static void probarOperacionesProfesores(ProfesorService profesorService) {
        System.out.println("\n=== PRUEBAS CON PROFESORES DAM ===");
        
        try {
            // Obtener todos los profesores
            System.out.println("1. Obteniendo todos los profesores...");
            List<ProfesorDTO> todosProfesores = profesorService.obtenerTodosLosProfesores();
            System.out.println("✅ Total de profesores: " + todosProfesores.size());
            
            if (!todosProfesores.isEmpty()) {
                // Usar el primer profesor
                String primerProfesorId = todosProfesores.get(0).getCodProfesor();
                
                // Obtener profesor específico
                System.out.println("\n2. Obteniendo profesor " + primerProfesorId + "...");
                ProfesorDTO profesorDTO = profesorService.obtenerProfesorPorId(primerProfesorId);
                if (profesorDTO != null) {
                    System.out.println("✅ Profesor obtenido: " + profesorDTO);
                }
                
                // Obtener información completa del profesor
                System.out.println("\n3. Información completa del profesor " + primerProfesorId + ":");
                ProfesorDTO profesorCompleto = profesorService.obtenerProfesorCompletoPorId(primerProfesorId);
                if (profesorCompleto != null) {
                    System.out.println("   👨‍🏫 " + profesorCompleto.getNombreCompleto());
                    System.out.println("   📚 Módulos: " + profesorCompleto.getModulosImpartidos());
                    
                    // Calcular horas totales
                    int horasTotales = profesorService.obtenerHorasTotalesProfesor(primerProfesorId);
                    System.out.println("   ⏰ Horas totales semanales: " + horasTotales);
                    
                    // Mostrar módulos completos
                    List<Modulo> modulosCompletos = profesorService.obtenerModulosCompletosPorProfesor(primerProfesorId);
                    if (!modulosCompletos.isEmpty()) {
                        System.out.println("   📖 Detalles de módulos:");
                        modulosCompletos.forEach(modulo -> 
                            System.out.println("      - " + modulo.getNombreModulo() + 
                                             " (" + modulo.getHorasSemanales() + "h) - " + 
                                             modulo.getCurso()));
                    }
                }
                
                // Mostrar todos los profesores
                System.out.println("\n4. Lista completa de profesores:");
                todosProfesores.forEach(System.out::println);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en operaciones de profesores: " + e.getMessage());
        }
    }
    
    private static void probarOperacionesModulos(ProfesorService profesorService) {
        System.out.println("\n=== PRUEBAS CON MÓDULOS DAM ===");
        
        try {
            // Probar con el módulo "Proyecto DAM" (0492)
            String moduloProyecto = "0492";
            
            System.out.println("1. Obteniendo profesor del módulo " + moduloProyecto + "...");
            Profesor profesorModulo = profesorService.obtenerProfesorPorModulo(moduloProyecto);
            if (profesorModulo != null) {
                System.out.println("✅ Profesor del módulo: " + 
                                 profesorModulo.getNombreProfesor() + " " + 
                                 profesorModulo.getApellidosProfesor());
            } else {
                System.out.println("❌ No se encontró profesor para el módulo " + moduloProyecto);
            }
            
            // Obtener alumnos y profesor por módulo
            System.out.println("\n2. Obteniendo alumnos del módulo " + moduloProyecto + "...");
            List<Object[]> resultados = profesorService.obtenerAlumnosYProfesorPorModulo(moduloProyecto);
            if (!resultados.isEmpty()) {
                System.out.println("✅ Alumnos matriculados en Proyecto DAM:");
                for (Object[] fila : resultados) {
                    System.out.printf("   👨‍🎓 %s %s - 👨‍🏫 %s %s%n", 
                        fila[1], fila[2], fila[4], fila[5]);
                }
                System.out.println("   📊 Total: " + resultados.size() + " alumnos");
            } else {
                System.out.println("ℹ️  No se encontraron alumnos para el módulo " + moduloProyecto);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en operaciones de módulos: " + e.getMessage());
        }
    }
}