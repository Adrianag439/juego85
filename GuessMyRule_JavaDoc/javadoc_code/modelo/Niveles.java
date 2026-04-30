package modelo;

/**
 * Clase abstracta que define la estructura común de todos los niveles del juego.
 * <p>
 * Cada nivel concreto debe heredar de esta clase e implementar
 * su propia lógica de juego. Define atributos base como vidas,
 * puntuación y si el nivel tiene temporizador.
 * </p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public abstract class Niveles {

    /** Identificador numérico del nivel. */
    protected int id;

    /** Nombre descriptivo del nivel (ej. "Fácil", "Difícil"). */
    protected String nombre;

    /** Cantidad de vidas o intentos disponibles en este nivel. */
    protected int vidas;

    /** Puntos base que se otorgan al completar el nivel correctamente. */
    protected int puntuacionBase;

    /** Indica si el nivel tiene un límite de tiempo (cronómetro). */
    protected boolean tieneTemporizador;

    /**
     * Constructor vacío requerido para instanciación por herencia.
     */
    public Niveles() {}

    /**
     * Constructor completo para inicializar un nivel con todos sus atributos.
     *
     * @param id                 identificador del nivel
     * @param nombre             nombre descriptivo
     * @param vidas              cantidad de vidas disponibles
     * @param puntuacionBase     puntos base por completar el nivel
     * @param tieneTemporizador  {@code true} si el nivel usa cronómetro
     */
    public Niveles(int id, String nombre, int vidas, int puntuacionBase, boolean tieneTemporizador) {
        this.id = id;
        this.nombre = nombre;
        this.vidas = vidas;
        this.puntuacionBase = puntuacionBase;
        this.tieneTemporizador = tieneTemporizador;
    }

    /** @return identificador del nivel */
    public int getId() { return id; }

    /** @param id nuevo identificador */
    public void setId(int id) { this.id = id; }

    /** @return nombre del nivel */
    public String getNombre() { return nombre; }

    /** @param nombre nuevo nombre */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** @return número de vidas del nivel */
    public int getVidas() { return vidas; }

    /** @param vidas nueva cantidad de vidas */
    public void setVidas(int vidas) { this.vidas = vidas; }

    /** @return puntuación base del nivel */
    public int getPuntuacionBase() { return puntuacionBase; }

    /** @param puntuacionBase nueva puntuación base */
    public void setPuntuacionBase(int puntuacionBase) { this.puntuacionBase = puntuacionBase; }

    /** @return {@code true} si el nivel tiene temporizador */
    public boolean isTieneTemporizador() { return tieneTemporizador; }

    /** @param tieneTemporizador {@code true} para activar el temporizador */
    public void setTieneTemporizador(boolean tieneTemporizador) { this.tieneTemporizador = tieneTemporizador; }
}
