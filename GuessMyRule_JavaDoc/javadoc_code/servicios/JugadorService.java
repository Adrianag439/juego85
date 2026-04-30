package servicios;

import modelo.Jugador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

/**
 * Capa de acceso a datos (DAO) para la entidad {@link modelo.Jugador}.
 * <p>
 * Provee operaciones CRUD sobre la tabla {@code jugador} en MySQL
 * usando {@code PreparedStatement} parametrizados para prevenir
 * inyección SQL.
 * </p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 * @see ConexionBD
 */
public class JugadorService {

    /** Instancia de conexión a la base de datos. */
    private ConexionBD conexionBD;

    /**
     * Constructor. Crea una nueva instancia de {@link ConexionBD}
     * lista para operar.
     */
    public JugadorService() {
        conexionBD = new ConexionBD();
    }

    /**
     * Inserta un nuevo jugador en la base de datos.
     * <p>
     * Genera automáticamente un UUID y lo asigna al objeto
     * antes de persistir. La fecha de registro debe estar
     * configurada antes de llamar a este método.
     * </p>
     *
     * @param jugador objeto {@link Jugador} con apodo, sesionActiva y fechaRegistro ya configurados
     * @return mensaje de confirmación: "✔ Jugador guardado correctamente" o "✘ Error al guardar jugador"
     * @throws Exception si ocurre un error de conexión o SQL
     */
    public String create(Jugador jugador) throws Exception {
        Connection con = conexionBD.getConexion();

        // Genera un UUID único para el nuevo jugador
        String id = UUID.randomUUID().toString();
        jugador.setId(id);

        String SQL = "INSERT INTO jugador (id, apodo, sesion_activa, fecha_registro) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(SQL);
        ps.setString(1, id);
        ps.setString(2, jugador.getApodo());
        ps.setBoolean(3, jugador.isSesionActiva());
        ps.setTimestamp(4, new java.sql.Timestamp(jugador.getFechaRegistro().getTime()));

        int filas = ps.executeUpdate();

        if (filas > 0) {
            return "✔ Jugador guardado correctamente";
        }

        return "✘ Error al guardar jugador";
    }

    /**
     * Busca un jugador en la base de datos por su apodo.
     *
     * @param apodo apodo único del jugador a buscar
     * @return objeto {@link Jugador} si existe, {@code null} si no se encontró
     * @throws Exception si ocurre un error de conexión o SQL
     */
    public Jugador readone(String apodo) throws Exception {
        Connection con = conexionBD.getConexion();

        String SQL = "SELECT id, apodo, sesion_activa, fecha_registro FROM jugador WHERE apodo = ?";

        PreparedStatement ps = con.prepareStatement(SQL);
        ps.setString(1, apodo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Jugador jugador = new Jugador();
            jugador.setId(rs.getString("id"));
            jugador.setApodo(rs.getString("apodo"));
            jugador.setSesionActiva(rs.getBoolean("sesion_activa"));
            jugador.setFechaRegistro(new Date(rs.getTimestamp("fecha_registro").getTime()));
            return jugador;
        }

        // No se encontró ningún jugador con ese apodo
        return null;
    }

    /**
     * Actualiza el estado de sesión de un jugador en la base de datos.
     *
     * @param jugadorId identificador UUID del jugador a actualizar
     * @param activa    {@code true} para activar la sesión, {@code false} para cerrarla
     * @return mensaje de confirmación: "✔ Estado de sesión actualizado" o "✘ Error al actualizar estado"
     * @throws Exception si ocurre un error de conexión o SQL
     */
    public String updateSesionActiva(String jugadorId, boolean activa) throws Exception {
        Connection con = conexionBD.getConexion();

        String SQL = "UPDATE jugador SET sesion_activa = ? WHERE id = ?";

        PreparedStatement ps = con.prepareStatement(SQL);
        ps.setBoolean(1, activa);
        ps.setString(2, jugadorId);

        int filas = ps.executeUpdate();

        if (filas > 0) {
            return "✔ Estado de sesión actualizado";
        }

        return "✘ Error al actualizar estado";
    }
}
