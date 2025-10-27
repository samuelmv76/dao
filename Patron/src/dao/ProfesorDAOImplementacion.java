package dao;

import entities.Profesor;
import entities.Modulo;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAOImplementacion implements ProfesorDAO {
    private static ProfesorDAOImplementacion instancia;
    
    private ProfesorDAOImplementacion() {}
    
    public static ProfesorDAOImplementacion getInstancia() {
        if (instancia == null) {
            instancia = new ProfesorDAOImplementacion();
        }
        return instancia;
    }
    
    @Override
    public Profesor obtenerProfesorPorId(String id) {
        String sql = "SELECT cod_profesor, nombre_profesor, ciudad FROM profesor WHERE cod_profesor = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Profesor profesor = new Profesor();
                    profesor.setCodProfesor(resultSet.getString("cod_profesor"));
                    profesor.setNombreProfesor(resultSet.getString("nombre_profesor"));
                    // No tenemos apellidos en la BD, usamos ciudad o nombre completo
                    profesor.setApellidosProfesor(resultSet.getString("ciudad"));
                    return profesor;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener profesor por ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Profesor> obtenerTodosLosProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        String sql = "SELECT cod_profesor, nombre_profesor, ciudad FROM profesor ORDER BY cod_profesor";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Profesor profesor = new Profesor();
                profesor.setCodProfesor(resultSet.getString("cod_profesor"));
                profesor.setNombreProfesor(resultSet.getString("nombre_profesor"));
                profesor.setApellidosProfesor(resultSet.getString("ciudad")); // Usamos ciudad como apellido
                profesores.add(profesor);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los profesores: " + e.getMessage());
        }
        return profesores;
    }
    
    @Override
    public List<String> obtenerModulosPorProfesor(String codProfesor) {
        List<String> modulos = new ArrayList<>();
        String sql = "SELECT nombre_modulo FROM modulo WHERE cod_profesor = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, codProfesor);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    modulos.add(resultSet.getString("nombre_modulo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener módulos por profesor: " + e.getMessage());
        }
        return modulos;
    }
    
    public List<Modulo> obtenerModulosCompletosPorProfesor(String codProfesor) {
        List<Modulo> modulos = new ArrayList<>();
        String sql = "SELECT cod_modulo, nombre_modulo, curso FROM modulo WHERE cod_profesor = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, codProfesor);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Modulo modulo = new Modulo();
                    modulo.setCodModulo(resultSet.getString("cod_modulo"));
                    modulo.setNombreModulo(resultSet.getString("nombre_modulo"));
                    modulo.setCurso(resultSet.getString("curso"));
                    // Las horas_semanales no están en las columnas listadas
                    modulos.add(modulo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener módulos completos por profesor: " + e.getMessage());
        }
        return modulos;
    }
    
    public Profesor obtenerProfesorPorModulo(String codModulo) {
        String sql = "SELECT p.cod_profesor, p.nombre_profesor, p.ciudad " +
                    "FROM profesor p " +
                    "INNER JOIN modulo m ON p.cod_profesor = m.cod_profesor " +
                    "WHERE m.cod_modulo = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, codModulo);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Profesor profesor = new Profesor();
                    profesor.setCodProfesor(resultSet.getString("cod_profesor"));
                    profesor.setNombreProfesor(resultSet.getString("nombre_profesor"));
                    profesor.setApellidosProfesor(resultSet.getString("ciudad"));
                    return profesor;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener profesor por módulo: " + e.getMessage());
        }
        return null;
    }
    
    public List<Object[]> obtenerAlumnosYProfesorPorModulo(String codModulo) {
        List<Object[]> resultados = new ArrayList<>();
        
        String sql = "SELECT " +
                    "a.cod_alumno, a.nombre_alumno, a.apellidos_alumno, " +
                    "p.cod_profesor, p.nombre_profesor, p.ciudad, " +
                    "m.nombre_modulo, am.nota " +
                    "FROM alumno a " +
                    "INNER JOIN alumno_modulo am ON a.cod_alumno = am.cod_alumno " +
                    "INNER JOIN modulo m ON am.cod_modulo = m.cod_modulo " +
                    "INNER JOIN profesor p ON m.cod_profesor = p.cod_profesor " +
                    "WHERE m.cod_modulo = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, codModulo);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Object[] fila = {
                        resultSet.getString("cod_alumno"),
                        resultSet.getString("nombre_alumno"),
                        resultSet.getString("apellidos_alumno"),
                        resultSet.getString("cod_profesor"),
                        resultSet.getString("nombre_profesor"),
                        resultSet.getString("ciudad"), // Usamos ciudad como apellido
                        resultSet.getString("nombre_modulo"),
                        resultSet.getString("nota")
                    };
                    resultados.add(fila);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos y profesor por módulo: " + e.getMessage());
        }
        return resultados;
    }
    
    // Método para mostrar distribución de módulos con nombres correctos
    public void mostrarDistribucionModulos() {
        String sql = "SELECT p.cod_profesor, p.nombre_profesor, p.ciudad, " +
                    "COUNT(m.cod_modulo) as num_modulos, " +
                    "GROUP_CONCAT(m.nombre_modulo) as modulos " +
                    "FROM profesor p " +
                    "LEFT JOIN modulo m ON p.cod_profesor = m.cod_profesor " +
                    "GROUP BY p.cod_profesor, p.nombre_profesor, p.ciudad " +
                    "ORDER BY p.cod_profesor";
        
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            System.out.println("\n📊 DISTRIBUCIÓN DE MÓDULOS POR PROFESOR:");
            System.out.println("==========================================");
            
            while (resultSet.next()) {
                System.out.printf("👨‍🏫 %s (%s) - Ciudad: %s%n",
                    resultSet.getString("nombre_profesor"),
                    resultSet.getString("cod_profesor"),
                    resultSet.getString("ciudad"));
                
                System.out.printf("   📚 Módulos: %d%n", resultSet.getInt("num_modulos"));
                
                String modulos = resultSet.getString("modulos");
                if (modulos != null) {
                    String[] listaModulos = modulos.split(",");
                    for (String modulo : listaModulos) {
                        System.out.println("      • " + modulo.trim());
                    }
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener distribución de módulos: " + e.getMessage());
        }
    }
}