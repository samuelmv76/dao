package dao;

import entities.Alumno;
import config.DatabaseConfig;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAOImplementacion implements AlumnoDAO {
    private static AlumnoDAOImplementacion instancia;
    
    private AlumnoDAOImplementacion() {}
    
    public static AlumnoDAOImplementacion getInstancia() {
        if (instancia == null) {
            instancia = new AlumnoDAOImplementacion();
        }
        return instancia;
    }
    
    @Override
    public void crearAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumno (cod_alumno, nombre_alumno, apellidos_alumno, fecha_nacimiento, grupo) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Generar código que no exista (empezando desde 50)
            String nuevoCodigo = generarNuevoCodigo();
            statement.setString(1, nuevoCodigo);
            statement.setString(2, alumno.getNombreAlumno());
            statement.setString(3, alumno.getApellidosAlumno());
            statement.setDate(4, new java.sql.Date(alumno.getFechaNacimiento().getTime()));
            statement.setString(5, String.valueOf(alumno.getGrupo()));
            
            int filasAfectadas = statement.executeUpdate();
            System.out.println("✅ Alumno creado. Código: " + nuevoCodigo);
            
        } catch (SQLException e) {
            System.err.println("❌ Error al crear alumno: " + e.getMessage());
        }
    }
    
    private String generarNuevoCodigo() {
        // Buscar el máximo código actual y sumar 1
        String sql = "SELECT MAX(CAST(cod_alumno AS UNSIGNED)) as max_cod FROM alumno";
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            if (resultSet.next()) {
                int maxCod = resultSet.getInt("max_cod");
                return String.format("%02d", maxCod + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error al generar nuevo código: " + e.getMessage());
        }
        return "50"; // Si falla, usar 50
    }
    
    @Override
    public Alumno obtenerAlumnoPorId(String id) {
        String sql = "SELECT cod_alumno, nombre_alumno, apellidos_alumno, fecha_nacimiento, grupo FROM alumno WHERE cod_alumno = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearResultSetAAlumno(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener alumno por ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Alumno> obtenerTodosLosAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        String sql = "SELECT cod_alumno, nombre_alumno, apellidos_alumno, fecha_nacimiento, grupo FROM alumno ORDER BY CAST(cod_alumno AS UNSIGNED)";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Alumno alumno = mapearResultSetAAlumno(resultSet);
                alumnos.add(alumno);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los alumnos: " + e.getMessage());
        }
        return alumnos;
    }
    
    @Override
    public void actualizarAlumno(Alumno alumno) {
        String sql = "UPDATE alumno SET nombre_alumno = ?, apellidos_alumno = ?, fecha_nacimiento = ?, grupo = ? WHERE cod_alumno = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, alumno.getNombreAlumno());
            statement.setString(2, alumno.getApellidosAlumno());
            statement.setDate(3, new java.sql.Date(alumno.getFechaNacimiento().getTime()));
            statement.setString(4, String.valueOf(alumno.getGrupo()));
            statement.setString(5, alumno.getCodAlumno());
            
            int filasAfectadas = statement.executeUpdate();
            System.out.println("✅ Alumno actualizado. Filas afectadas: " + filasAfectadas);
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar alumno: " + e.getMessage());
        }
    }
    
    @Override
    public void eliminarAlumno(String id) {
        String sql = "DELETE FROM alumno WHERE cod_alumno = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, id);
            
            int filasAfectadas = statement.executeUpdate();
            System.out.println("✅ Alumno eliminado. Filas afectadas: " + filasAfectadas);
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar alumno: " + e.getMessage());
        }
    }
    
    private Alumno mapearResultSetAAlumno(ResultSet resultSet) throws SQLException {
        Alumno alumno = new Alumno();
        alumno.setCodAlumno(resultSet.getString("cod_alumno"));
        alumno.setNombreAlumno(resultSet.getString("nombre_alumno"));
        alumno.setApellidosAlumno(resultSet.getString("apellidos_alumno"));
        alumno.setFechaNacimiento(resultSet.getDate("fecha_nacimiento"));
        
        String grupo = resultSet.getString("grupo");
        if (grupo != null && !grupo.isEmpty()) {
            alumno.setGrupo(grupo.charAt(0));
        }
        
        return alumno;
    }
    
    // Método adicional: Obtener módulos de un alumno
    public List<String> obtenerModulosDeAlumno(String codAlumno) {
        List<String> modulos = new ArrayList<>();
        String sql = "SELECT m.nombre_modulo, am.nota " +
                    "FROM modulo m " +
                    "INNER JOIN alumno_modulo am ON m.cod_modulo = am.cod_modulo " +
                    "WHERE am.cod_alumno = ?";
        
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, codAlumno);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String modulo = resultSet.getString("nombre_modulo");
                    String nota = resultSet.getString("nota");
                    modulos.add(modulo + (nota != null ? " (Nota: " + nota + ")" : ""));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener módulos del alumno: " + e.getMessage());
        }
        return modulos;
    }
}