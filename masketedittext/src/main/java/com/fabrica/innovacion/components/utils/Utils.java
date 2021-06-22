package com.fabrica.innovacion.components.utils;

public class Utils {
    public static String limpiarCadenas(String string) {
        string = string.replaceAll("[$,.() -]", "");
        string = string.replace(".00", "");
        string = string.replace(".0", "");
        string = string.replace("+","");
        return string;
    }
}
