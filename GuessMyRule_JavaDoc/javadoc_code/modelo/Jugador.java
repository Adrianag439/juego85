package modelo;

import java.util.Date;

/**
 * Entidad que representa a un jugador registrado en el sistema.
 * <p>
 * Almacena el identificador único (UUID), el apodo público,
 * el estado de sesión y la fecha en que el jugador fue registrado.
 * </p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class Jugador {

    /** Identificador único del jugador generado como UUID. */
    private String id;

    /** Apodo público del jugador (máximo 10 caracteres). */
    private String apodo;

    /** Indica si el jugador tiene una sesión activa en curso. */
    private boolean sesionActiva;

    /** Fecha y hora en que el jugador fue registrado. */
    private Date fechaRegistro;

    /**
     * Constructor vacío requerido para instanciación por reflexión.
     */
    public Jugador() {}

    /**
     * Constructor completo para crear un jugador con todos sus atributos.
     *
     * @param id            identificador UUID único
     * @param apodo         nombre público del jugador
     * @param sesionActiva  {@code true} si la sesión está activa
     * @param fechaRegistro fecha y hora de registro
     */
    public Jugador(String id, String apodo, boolean sesionActiva, Date fechaRegistro) {
        this.id = id;
        this.apodo = apodo;
        this.sesionActiva = sesionActiva;
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Retorna el identificador UUID del jugador.
     * @return id como {@code String}
     */
    public String getId() { return id; }

    /**
     * Establece el identificador UUID del jugador.
     * @param id nuevo identificador
     */
    public void setId(String id) { this.id = id; }

    /**
     * Retorna el apodo del jugador.
     * @return apodo como {@code String}
     */
    public String getApodo() { return apodo; }

    /**
     * Establece el apodo del jugador.
     * @param apodo nuevo apodo (máx. 10 caracteres)
     */
    public void setApodo(String apodo) { this.apodo = apodo; }

    /**
     * Indica si el jugador tiene una sesión activa.
     * @return {@code true} si la sesión está activa
     */
    public boolean isSesionActiva() { return sesionActiva; }

    /**
     * Activa o desactiva la sesión del jugador.
     * @param sesionActiva {@code true} para activar la sesión
     */
    public void setSesionActiva(boolean sesionActiva) { this.sesionActiva = sesionActiva; }

    /**
     * Retorna la fecha en que el jugador fue registrado.
     * @return fecha de registro
     */
    public Date getFechaRegistro() { return fechaRegistro; }

    /**
     * Establece la fecha de registro del jugador.
     * @param fechaRegistro nueva fecha de registro
     */
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
