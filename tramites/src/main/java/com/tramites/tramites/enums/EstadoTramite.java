package com.tramites.tramites.enums;

/**
 * Enum que define los estados válidos para un trámite.
 */
public enum EstadoTramite {
    PENDIENTE("PENDIENTE"),
    APROBADO("APROBADO"),
    RECHAZADO("RECHAZADO");

    private final String valor;

    EstadoTramite(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    /**
     * Valida si un estado es válido.
     * @param estado Estado a validar
     * @return true si es válido, false si no
     */
    public static boolean isValid(String estado) {
        if (estado == null) {
            return false;
        }
        for (EstadoTramite e : EstadoTramite.values()) {
            if (e.valor.equals(estado)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el enum a partir del valor string.
     * @param valor Valor del estado
     * @return EstadoTramite correspondiente
     * @throws IllegalArgumentException si el valor no es válido
     */
    public static EstadoTramite fromValor(String valor) {
        for (EstadoTramite e : EstadoTramite.values()) {
            if (e.valor.equals(valor)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estado de trámite inválido: " + valor + 
            ". Los valores permitidos son: PENDIENTE, APROBADO, RECHAZADO");
    }
}
