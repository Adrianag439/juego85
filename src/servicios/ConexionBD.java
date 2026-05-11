package servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    private Connection conexion;
    
    // Configuración de la BD - AJUSTA ESTOS VALORES
    private static final String URL = "jdbc:mysql://localhost:3306/juego_db";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "root1234";
    
    public ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conexion;
    }
    
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}