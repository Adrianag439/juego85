package modelo;

/**
 * Gestiona las pistas progresivas disponibles en el Nivel Fácil.
 * <p>
 * Cada partida dispone de 3 pistas ordenadas de menos a más reveladoras:
 * </p>
 * <ul>
 *   <li><b>Pista 1:</b> muestra la operación del factorial (ej. 4! = 1×2×3×4)</li>
 *   <li><b>Pista 2:</b> muestra n!/2 con el valor del factorial (ej. 24/2)</li>
 *   <li><b>Pista 3:</b> muestra la fórmula completa con el resultado final</li>
 * </ul>
 *
 * @author Equipo Guess My Rule
 * @version 1.0
 */
public class Pista {

    /** Identificador único de la pista. */
    private int id;

    /** Identificador de la partida a la que pertenece esta pista. */
    private int idPartida;

    /** Texto de la pista generada más recientemente. */
    private String texto;

    /** Número sobre el cual se generó la pista. */
    private int numero;

    /** Indica si esta pista ya fue utilizada por el jugador. */
    private boolean usada;

    /**
     * Cantidad de pistas restantes disponibles para la partida actual.
     * Inicia en 3 y se decrementa con cada uso.
     */
    private int pistasRestantes = 3;

    /**
     * Constructor vacío. Inicializa la pista con 3 usos disponibles.
     */
    public Pista() {}

    /**
     * Constructor completo para restaurar una pista desde persistencia.
     *
     * @param id        identificador de la pista
     * @param idPartida identificador de la partida asociada
     * @param texto     texto de la última pista generada
     * @param numero    número sobre el que se generó la pista
     * @param usada     {@code true} si ya fue usada
     */
    public Pista(int id, int idPartida, String texto, int numero, boolean usada) {
        this.id = id;
        this.idPartida = idPartida;
        this.texto = texto;
        this.numero = numero;
        this.usada = usada;
    }

    // ══════════════════════════════════════════════════════════
    // LÓGICA DE PISTAS
    // ══════════════════════════════════════════════════════════

    /**
     * Genera el texto de la pista correspondiente al uso actual y
     * decrementa {@code pistasRestantes} en 1.
     * <p>
     * El nivel de detalle depende de cuántas pistas quedan:
     * </p>
     * <ul>
     *   <li>3 pistas → muestra la operación de los factores del factorial</li>
     *   <li>2 pistas → muestra n!/2 con el valor concreto</li>
     *   <li>1 pista  → muestra el resultado completo de n!/2</li>
     * </ul>
     *
     * @param numero    número ingresado por el jugador
     * @param factorial valor del factorial de {@code numero}
     */
    public void generarPista(int numero, long factorial) {
        this.numero = numero;
        this.usada = true;

        switch (pistasRestantes) {
            case 3:
                // Pista 1: muestra la multiplicación que forma el factorial
                // Ejemplo para n=4: "4! = 1 x 2 x 3 x 4"
                StringBuilder operacion = new StringBuilder();
                operacion.append("💡 Pista 1: ").append(numero).append("! = ");
                for (int i = 1; i <= numero; i++) {
                    operacion.append(i);
                    if (i < numero) operacion.append(" x ");
                }
                this.texto = operacion.toString();
                break;

            case 2:
                // Pista 2: muestra n!/2 con el valor del factorial
                // Ejemplo para n=4: "4! / 2 = 24 / 2"
                this.texto = "💡 Pista 2: " + numero + "! / 2 = "
                    + factorial + " / 2";
                break;

            case 1:
                // Pista 3: muestra la fórmula completa con el resultado
                // Ejemplo para n=4: "4! / 2 = 24 / 2 = 12.0"
                this.texto = "💡 Pista 3: " + numero + "! / 2 = "
                    + factorial + " / 2 = " + (factorial / 2.0);
                break;

            default:
                this.texto = "Sin pistas disponibles.";
        }

        // Consume una pista
        this.pistasRestantes--;
    }

    /**
     * Indica si quedan pistas disponibles para la partida.
     *
     * @return {@code true} si {@code pistasRestantes > 0}
     */
    public boolean tienePistasDisponibles() {
        return pistasRestantes > 0;
    }

    /**
     * Reinicia la pista a su estado inicial:
     * restaura {@code pistasRestantes} a 3 y limpia el texto.
     */
    public void reiniciar() {
        pistasRestantes = 3;
        usada = false;
        texto = null;
    }

    // ══════════════════════════════════════════════════════════
    // GETTERS Y SETTERS
    // ══════════════════════════════════════════════════════════

    /** @return id de la pista */
    public int getId() { return id; }

    /** @param id nuevo id */
    public void setId(int id) { this.id = id; }

    /** @return id de la partida asociada */
    public int getIdPartida() { return idPartida; }

    /** @param idPartida nuevo id de partida */
    public void setIdPartida(int idPartida) { this.idPartida = idPartida; }

    /** @return texto de la última pista generada */
    public String getTexto() { return texto; }

    /** @param texto nuevo texto de pista */
    public void setTexto(String texto) { this.texto = texto; }

    /** @return número sobre el que se generó la pista */
    public int getNumero() { return numero; }

    /** @param numero nuevo número de referencia */
    public void setNumero(int numero) { this.numero = numero; }

    /** @return {@code true} si la pista fue usada */
    public boolean isUsada() { return usada; }

    /** @param usada {@code true} para marcar la pista como usada */
    public void setUsada(boolean usada) { this.usada = usada; }

    /** @return cantidad de pistas restantes */
    public int getPistasRestantes() { return pistasRestantes; }
}
