package com.chall.api.Literatura.Conversion;

public interface IConversion {
    <T> T obtenerDatos(String json, Class<T> clase);
}
