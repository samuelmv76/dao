package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/instituto?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";      
    private static final String PASSWORD = "root";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver cargado correctamente");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver de MySQL no encontrado", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void probarConexion() {
        System.out.println("🔍 Probando conexión a MySQL...");
        try (Connection conn = getConnection()) {
            System.out.println("✅ CONEXIÓN EXITOSA con MySQL!");
            System.out.println("   Base de datos: " + conn.getCatalog());
        } catch (SQLException e) {
            System.err.println("❌ ERROR de conexión: " + e.getMessage());
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }
}