package modelo;
import java.util.Date;

public class Jugador {
    private String id;
    private String apodo;
    private boolean sesionActiva;
    private Date fechaRegistro;

    public Jugador() {}

    public Jugador(String id, String apodo, boolean sesionActiva, Date fechaRegistro) {
        this.id = id;
        this.apodo = apodo;
        this.sesionActiva = sesionActiva;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getApodo() { return apodo; }
    public void setApodo(String apodo) { this.apodo = apodo; }
    public boolean isSesionActiva() { return sesionActiva; }
    public void setSesionActiva(boolean sesionActiva) { this.sesionActiva = sesionActiva; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}