package co.edu.sena.impactoambiental.monitoreo.modelo;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Traduce el enum TipoLimite a los valores en minúscula de la base de datos
 * ('maximo' / 'minimo') y al revés. Así en Java seguimos el estándar de
 * constantes en MAYÚSCULA sin cambiar la tabla que ya usa la API.
 *
 * @author Roycer Cervantes
 * @author Sebastián Rodríguez
 */
@Converter(autoApply = true)
public class TipoLimiteConvertidor implements AttributeConverter<TipoLimite, String> {

    @Override
    public String convertToDatabaseColumn(TipoLimite tipo) {
        return tipo == null ? null : tipo.getValorBaseDatos();
    }

    @Override
    public TipoLimite convertToEntityAttribute(String valor) {
        return valor == null ? null : TipoLimite.desdeBaseDatos(valor);
    }
}
