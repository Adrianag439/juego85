package modelo;

public class Partida {
    private int id;
    private String apodoJugador;
    private int idNivel;
    private int vidasRestantes;
    private int puntuacion;
    private int intentosFallidos;
    private boolean activa;
    private int segundosRestantes = 180;

    public Partida() {}

    public Partida(int id, String apodoJugador, int idNivel, int vidasRestantes, int puntuacion, int intentosFallidos, boolean activa) {
        this.id = id;
        this.apodoJugador = apodoJugador;
        this.idNivel = idNivel;
        this.vidasRestantes = vidasRestantes;
        this.puntuacion = puntuacion;
        this.intentosFallidos = intentosFallidos;
        this.activa = activa;
    }

    // ── Logica de FACTORIAL ─────────────────────────────────────
    public long calcularFactorial(int num) {
        if (num < 0) return 0;
        long factorial = 1;
        for (int i = 1; i <= num; i++) {
            factorial *= i;
        }
        return factorial;
    }

    // ── Resultado correcto es el FACTORIAL (sin dividir) ────────
    public long calcularResultadoCorrecto(int num) {
        return calcularFactorial(num);
    }

    // ── Logica nivel DIFICIL (si aún la necesitas) ──────────────
    public long calcularResultadoDificil(int n) {
        return calcularFactorial(n);
    }

    // ── Logica de verificacion ────────────────────────────────
    public boolean verificarRespuesta(int num, double respuestaUsuario) {
        long correcto = calcularResultadoCorrecto(num);
        if (respuestaUsuario == correcto) {
            puntuacion++;
            return true;
        } else {
            intentosFallidos++;
            vidasRestantes--;
            return false;
        }
    }

    public boolean sinVidas() {
        return vidasRestantes <= 0;
    }

    // umbral en 5 fallos
    public boolean puedeVerPista() {
        return intentosFallidos >= 5;
    }

    public void reiniciar() {
        vidasRestantes = 6;
        puntuacion = 0;
        intentosFallidos = 0;
        activa = true;
        segundosRestantes = 180;
    }

    // ── Logica del tiempo ─────────────────────────────────────
    public void descontarSegundo() {
        if (segundosRestantes > 0) {
            segundosRestantes--;
        }
    }

    public boolean tiempoAgotado() {
        return segundosRestantes <= 0;
    }

    public boolean tiempoEnAdvertencia() {
        return segundosRestantes <= 30;
    }

    public boolean tiempoEnPeligro() {
        return segundosRestantes <= 10;
    }

    public int getSegundosRestantes() {
        return segundosRestantes;
    }

    // ✅ MÉTODO AGREGADO (este faltaba)
    public void setSegundosRestantes(int segundosRestantes) {
        this.segundosRestantes = segundosRestantes;
    }

    public void reiniciarTiempo() {
        segundosRestantes = 180;
    }

    // ── Getters y Setters ─────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getApodoJugador() { return apodoJugador; }
    public void setApodoJugador(String apodoJugador) { this.apodoJugador = apodoJugador; }
    
    public int getIdNivel() { return idNivel; }
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }
    
    public int getVidasRestantes() { return vidasRestantes; }
    public void setVidasRestantes(int vidasRestantes) { this.vidasRestantes = vidasRestantes; }
    
    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    
    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }
    
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}