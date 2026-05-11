package modelo;
import java.util.Date;

public class Sesion {
    private String apodo;
    private boolean activa;
    private Date fechaCreacion;
    private String token;

    public Sesion() {}

    public Sesion(String apodo, boolean activa, Date fechaCreacion, String token) {
        this.apodo = apodo;
        this.activa = activa;
        this.fechaCreacion = fechaCreacion;
        this.token = token;
    }

    // Getters y Setters
    public String getApodo() { return apodo; }
    public void setApodo(String apodo) { this.apodo = apodo; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}