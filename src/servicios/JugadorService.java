package servicios;
import modelo.Jugador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

public class JugadorService {
    
    private ConexionBD conexionBD;
    
    public JugadorService() {
        conexionBD = new ConexionBD();
    }
    
    // Crear jugador
    public String create(Jugador jugador) throws Exception {
        Connection con = conexionBD.getConexion();
        
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
    
    // Buscar jugador por apodo
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
        
        return null;
    }
    
    // Actualizar estado de sesión del jugador
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