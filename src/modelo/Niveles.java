package modelo;

public abstract class Niveles {
    protected int id;
    protected String nombre;
    protected int vidas;
    protected int puntuacionBase;
    protected boolean tieneTemporizador;

    public Niveles() {}

    public Niveles(int id, String nombre, int vidas, int puntuacionBase, boolean tieneTemporizador) {
        this.id = id;
        this.nombre = nombre;
        this.vidas = vidas;
        this.puntuacionBase = puntuacionBase;
        this.tieneTemporizador = tieneTemporizador;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getVidas() { return vidas; }
    public void setVidas(int vidas) { this.vidas = vidas; }
    public int getPuntuacionBase() { return puntuacionBase; }
    public void setPuntuacionBase(int puntuacionBase) { this.puntuacionBase = puntuacionBase; }
    public boolean isTieneTemporizador() { return tieneTemporizador; }
    public void setTieneTemporizador(boolean tieneTemporizador) { this.tieneTemporizador = tieneTemporizador; }

    // ── Subclase NIVEL FÁCIL ──────────────────────────────────
    public static class NivelFacil extends Niveles {
        public NivelFacil() {
            super(1, "Fácil", 5, 10, false);
        }
    }

    // ── Subclase NIVEL DIFÍCIL ────────────────────────────────
    public static class NivelDificil extends Niveles {
        public NivelDificil() {
            super(2, "Difícil", 3, 20, true);
        }
    }
}