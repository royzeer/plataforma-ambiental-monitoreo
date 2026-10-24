package co.edu.sena.impactoambiental.monitoreo.modelo;

/**
 * Indica cómo se compara un valor medido con su límite permisible.
 *
 * MAXIMO: el valor no puede pasar el límite (ej. DBO5, coliformes, PM10).
 * MINIMO: el valor no puede quedar por debajo (ej. oxígeno disuelto).
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
public enum TipoLimite {

    MAXIMO("maximo", "Máximo permitido"),
    MINIMO("minimo", "Mínimo exigido");

    /** Valor tal como se guarda en la columna ENUM de MySQL. */
    private final String valorBaseDatos;
    private final String descripcion;

    TipoLimite(String valorBaseDatos, String descripcion) {
        this.valorBaseDatos = valorBaseDatos;
        this.descripcion = descripcion;
    }

    public String getValorBaseDatos() {
        return valorBaseDatos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Convierte el texto de la base de datos en el enum. */
    public static TipoLimite desdeBaseDatos(String valor) {
        for (TipoLimite tipo : values()) {
            if (tipo.valorBaseDatos.equalsIgnoreCase(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de límite desconocido: " + valor);
    }
}
