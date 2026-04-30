package servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión JDBC a la base de datos MySQL del juego.
 * <p>
 * Carga el driver automáticamente en el constructor e implementa
 * reconexión automática si la conexión fue cerrada o es nula.
 * Los parámetros de conexión están definidos como constantes estáticas.
 * </p>
 *
 * <p><b>Base de datos:</b> juego_db en localhost:3306</p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class ConexionBD {

    /** Conexión JDBC activa con la base de datos. */
    private Connection conexion;

    /** URL de conexión al servidor MySQL. */
    private static final String URL = "jdbc:mysql://localhost:3306/juego_db";

    /** Usuario de la base de datos. */
    private static final String USUARIO = "root";

    /** Contraseña del usuario de la base de datos. */
    private static final String CONTRASENA = "root1234";

    /**
     * Constructor. Carga el driver MySQL y abre la conexión inicial.
     * <p>
     * Si ocurre un error al cargar el driver o al conectar,
     * imprime el stack trace y {@code conexion} quedará como {@code null}.
     * </p>
     */
    public ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna la conexión activa con la base de datos.
     * <p>
     * Si la conexión está cerrada o es {@code null}, intenta reabrirla
     * antes de retornar. Esto garantiza que siempre se entregue
     * una conexión utilizable.
     * </p>
     *
     * @return conexión JDBC activa
     */
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

    /**
     * Cierra la conexión con la base de datos liberando los recursos.
     * <p>
     * Solo cierra si la conexión existe y está abierta.
     * </p>
     */
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
