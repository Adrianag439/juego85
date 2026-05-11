package modelo;

public class Pista {
    private int id;
    private int idPartida;
    private String texto;
    private int numero;
    private boolean usada;
    private int pistasRestantes = 3;

    public Pista() {}

    public Pista(int id, int idPartida, String texto, int numero, boolean usada) {
        this.id = id;
        this.idPartida = idPartida;
        this.texto = texto;
        this.numero = numero;
        this.usada = usada;
    }

    // ── Lógica de pista ──────────────────────────────────────
    public void generarPista(int numero, long factorial) {
        this.numero = numero;
        this.usada = true;

        switch (pistasRestantes) {
            case 3:
                // Pista 1 → muestra la operación del factorial
                // Ej: 4! = 1 × 2 × 3 × 4
                StringBuilder operacion = new StringBuilder();
                operacion.append("💡 Pista 1: ").append(numero).append("! = ");
                for (int i = 1; i <= numero; i++) {
                    operacion.append(i);
                    if (i < numero) operacion.append(" x ");
                }
                this.texto = operacion.toString();
                break;

            case 2:
                // Pista 2 → muestra la operación n!/2
                // Ej: 4! / 2 = 24 / 2
                this.texto = "💡 Pista 2: " + numero + "! / 2 = "
                    + factorial + " / 2";
                break;

            case 1:
                // Pista 3 → muestra la formula completa con resultado
                // Ej: 4! / 2 = 24 / 2 = 12
                this.texto = "💡 Pista 3: " + numero + "! / 2 = "
                    + factorial + " / 2 = " + (factorial / 2.0);
                break;

            default:
                this.texto = "Sin pistas disponibles.";
        }

        this.pistasRestantes--;
    }

    public boolean tienePistasDisponibles() {
        return pistasRestantes > 0;
    }

    public void reiniciar() {
        pistasRestantes = 3;
        usada = false;
        texto = null;
    }
    // ─────────────────────────────────────────────────────────

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdPartida() { return idPartida; }
    public void setIdPartida(int idPartida) { this.idPartida = idPartida; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public boolean isUsada() { return usada; }
    public void setUsada(boolean usada) { this.usada = usada; }
    public int getPistasRestantes() { return pistasRestantes; }
}