package modelo;

import java.util.Date;

/**
 * Representa la sesión activa de un jugador en el sistema.
 * <p>
 * Almacena el apodo del jugador, el estado de la sesión,
 * la fecha de creación y un token de identificación de sesión.
 * </p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class Sesion {

    /** Apodo del jugador propietario de esta sesión. */
    private String apodo;

    /** Estado actual de la sesión ({@code true} si está activa). */
    private boolean activa;

    /** Fecha y hora en que se creó la sesión. */
    private Date fechaCreacion;

    /** Token único que identifica la sesión. */
    private String token;

    /**
     * Constructor vacío requerido para instanciación por reflexión.
     */
    public Sesion() {}

    /**
     * Constructor completo para crear una sesión con todos sus atributos.
     *
     * @param apodo         apodo del jugador
     * @param activa        {@code true} si la sesión está activa
     * @param fechaCreacion fecha y hora de creación
     * @param token         token único de la sesión
     */
    public Sesion(String apodo, boolean activa, Date fechaCreacion, String token) {
        this.apodo = apodo;
        this.activa = activa;
        this.fechaCreacion = fechaCreacion;
        this.token = token;
    }

    /** @return apodo del jugador */
    public String getApodo() { return apodo; }

    /** @param apodo nuevo apodo */
    public void setApodo(String apodo) { this.apodo = apodo; }

    /** @return {@code true} si la sesión está activa */
    public boolean isActiva() { return activa; }

    /** @param activa nuevo estado de la sesión */
    public void setActiva(boolean activa) { this.activa = activa; }

    /** @return fecha de creación de la sesión */
    public Date getFechaCreacion() { return fechaCreacion; }

    /** @param fechaCreacion nueva fecha de creación */
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    /** @return token de la sesión */
    public String getToken() { return token; }

    /** @param token nuevo token */
    public void setToken(String token) { this.token = token; }
}
