package modelo;

/**
 * Núcleo lógico de una partida del juego Guess My Rule.
 * <p>
 * Gestiona el estado completo de una partida activa: vidas restantes,
 * puntuación, intentos fallidos, tiempo y los cálculos matemáticos
 * de ambos niveles (Fácil y Difícil).
 * </p>
 *
 * <p><b>Regla Nivel Fácil:</b> resultado = n! / 2</p>
 * <p><b>Regla Nivel Difícil:</b> resultado = n!/2 + n!/(n+1)</p>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class Partida {

    /** Identificador único de la partida. */
    private int id;

    /** Apodo del jugador que juega esta partida. */
    private String apodoJugador;

    /** Identificador del nivel seleccionado (1=Fácil, 2=Difícil). */
    private int idNivel;

    /** Vidas o intentos restantes en la partida actual. */
    private int vidasRestantes;

    /** Contador de respuestas correctas consecutivas. */
    private int puntuacion;

    /** Acumulado de respuestas incorrectas en la partida. */
    private int intentosFallidos;

    /** Indica si la partida sigue activa ({@code false} cuando termina). */
    private boolean activa;

    /**
     * Segundos restantes del cronómetro.
     * Solo aplica al Nivel Difícil (inicia en 180 = 3 minutos).
     */
    private int segundosRestantes = 180;

    /**
     * Constructor vacío. Inicializa la partida con valores por defecto.
     */
    public Partida() {}

    /**
     * Constructor completo para crear una partida con todos sus atributos.
     *
     * @param id              identificador de la partida
     * @param apodoJugador    apodo del jugador
     * @param idNivel         nivel seleccionado
     * @param vidasRestantes  vidas iniciales
     * @param puntuacion      puntuación inicial
     * @param intentosFallidos intentos fallidos acumulados
     * @param activa          {@code true} si la partida está en curso
     */
    public Partida(int id, String apodoJugador, int idNivel, int vidasRestantes,
                   int puntuacion, int intentosFallidos, boolean activa) {
        this.id = id;
        this.apodoJugador = apodoJugador;
        this.idNivel = idNivel;
        this.vidasRestantes = vidasRestantes;
        this.puntuacion = puntuacion;
        this.intentosFallidos = intentosFallidos;
        this.activa = activa;
    }

    // ══════════════════════════════════════════════════════════
    // LÓGICA COMPARTIDA
    // ══════════════════════════════════════════════════════════

    /**
     * Calcula el factorial de un número entero positivo de forma iterativa.
     * <p>
     * Es la operación base para ambas reglas del juego.
     * </p>
     *
     * @param num número entero no negativo
     * @return n! como {@code long}
     */
    public long calcularFactorial(int num) {
        long factorial = 1;
        for (int i = 1; i <= num; i++) {
            factorial *= i;
        }
        return factorial;
    }

    // ══════════════════════════════════════════════════════════
    // LÓGICA NIVEL FÁCIL
    // ══════════════════════════════════════════════════════════

    /**
     * Calcula el resultado correcto para el Nivel Fácil.
     * <p>
     * Fórmula: {@code n! / 2.0}
     * </p>
     *
     * @param num número ingresado por el jugador
     * @return resultado correcto como {@code double}
     */
    public double calcularResultadoCorrecto(int num) {
        return calcularFactorial(num) / 2.0;
    }

    // ══════════════════════════════════════════════════════════
    // LÓGICA NIVEL DIFÍCIL
    // ══════════════════════════════════════════════════════════

    /**
     * Calcula el resultado correcto para el Nivel Difícil.
     * <p>
     * Fórmula: {@code (n! / 2) + (n! / (n+1))}
     * </p>
     *
     * @param n número ingresado por el jugador
     * @return resultado correcto como {@code double}
     */
    public double calcularResultadoDificil(int n) {
        long factorial = calcularFactorial(n);
        return (factorial / 2.0) + (factorial / (double)(n + 1));
    }

    // ══════════════════════════════════════════════════════════
    // LÓGICA DE VERIFICACIÓN
    // ══════════════════════════════════════════════════════════

    /**
     * Verifica si la respuesta del jugador es correcta (Nivel Fácil).
     * <p>
     * Si es correcta incrementa {@code puntuacion}.
     * Si es incorrecta incrementa {@code intentosFallidos} y decrementa {@code vidasRestantes}.
     * </p>
     *
     * @param num              número ingresado por el jugador
     * @param respuestaUsuario resultado que el jugador escribió
     * @return {@code true} si la respuesta es correcta
     */
    public boolean verificarRespuesta(int num, double respuestaUsuario) {
        double correcto = calcularResultadoCorrecto(num);
        if (respuestaUsuario == correcto) {
            puntuacion++;
            return true;
        } else {
            intentosFallidos++;
            vidasRestantes--;
            return false;
        }
    }

    /**
     * Indica si el jugador se ha quedado sin vidas.
     *
     * @return {@code true} si {@code vidasRestantes <= 0}
     */
    public boolean sinVidas() {
        return vidasRestantes <= 0;
    }

    /**
     * Indica si el jugador puede solicitar una pista.
     * Se habilita cuando acumula 5 o más intentos fallidos.
     *
     * @return {@code true} si {@code intentosFallidos >= 5}
     */
    public boolean puedeVerPista() {
        return intentosFallidos >= 5;
    }

    /**
     * Reinicia la partida a su estado inicial.
     * Restablece vidas a 6, puntuación e intentos a 0,
     * activa a {@code true} y el cronómetro a 180 segundos.
     */
    public void reiniciar() {
        vidasRestantes = 6;
        puntuacion = 0;
        intentosFallidos = 0;
        activa = true;
        segundosRestantes = 180;
    }

    // ══════════════════════════════════════════════════════════
    // LÓGICA DEL CRONÓMETRO (Nivel Difícil)
    // ══════════════════════════════════════════════════════════

    /**
     * Descuenta un segundo del cronómetro.
     * Llamado cada segundo por el {@code Timeline} de JavaFX.
     * No baja de 0.
     */
    public void descontarSegundo() {
        if (segundosRestantes > 0) {
            segundosRestantes--;
        }
    }

    /**
     * Indica si el tiempo del cronómetro se agotó.
     *
     * @return {@code true} si {@code segundosRestantes == 0}
     */
    public boolean tiempoAgotado() {
        return segundosRestantes <= 0;
    }

    /**
     * Indica si el tiempo está en zona de advertencia.
     * El cronómetro cambia a color naranja al llegar a este umbral.
     *
     * @return {@code true} si {@code segundosRestantes <= 30}
     */
    public boolean tiempoEnAdvertencia() {
        return segundosRestantes <= 30;
    }

    /**
     * Indica si el tiempo está en zona de peligro crítico.
     * El cronómetro cambia a color rojo al llegar a este umbral.
     *
     * @return {@code true} si {@code segundosRestantes <= 10}
     */
    public boolean tiempoEnPeligro() {
        return segundosRestantes <= 10;
    }

    /**
     * Retorna los segundos restantes en el cronómetro.
     *
     * @return segundos restantes
     */
    public int getSegundosRestantes() {
        return segundosRestantes;
    }

    /**
     * Reinicia el cronómetro a 180 segundos (3 minutos).
     */
    public void reiniciarTiempo() {
        segundosRestantes = 180;
    }

    // ══════════════════════════════════════════════════════════
    // GETTERS Y SETTERS
    // ══════════════════════════════════════════════════════════

    /** @return id de la partida */
    public int getId() { return id; }

    /** @param id nuevo id de la partida */
    public void setId(int id) { this.id = id; }

    /** @return apodo del jugador */
    public String getApodoJugador() { return apodoJugador; }

    /** @param apodoJugador nuevo apodo */
    public void setApodoJugador(String apodoJugador) { this.apodoJugador = apodoJugador; }

    /** @return id del nivel */
    public int getIdNivel() { return idNivel; }

    /** @param idNivel nuevo id de nivel */
    public void setIdNivel(int idNivel) { this.idNivel = idNivel; }

    /** @return vidas restantes */
    public int getVidasRestantes() { return vidasRestantes; }

    /** @param vidasRestantes nueva cantidad de vidas */
    public void setVidasRestantes(int vidasRestantes) { this.vidasRestantes = vidasRestantes; }

    /** @return puntuación actual */
    public int getPuntuacion() { return puntuacion; }

    /** @param puntuacion nueva puntuación */
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    /** @return intentos fallidos acumulados */
    public int getIntentosFallidos() { return intentosFallidos; }

    /** @param intentosFallidos nuevo valor de intentos fallidos */
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    /** @return {@code true} si la partida está activa */
    public boolean isActiva() { return activa; }

    /** @param activa {@code true} para mantener la partida en curso */
    public void setActiva(boolean activa) { this.activa = activa; }
}
