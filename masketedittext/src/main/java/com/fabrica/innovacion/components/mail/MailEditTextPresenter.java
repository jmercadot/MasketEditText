package com.fabrica.innovacion.components.mail;

import android.text.Editable;

import com.fabrica.innovacion.components.mail.MailEditTextContract;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailEditTextPresenter implements MailEditTextContract.Presenter {
    private MailEditTextContract.View listener;

    private String temp = "";
    private String cleanString = "";
    private int cursor = 0;

    public MailEditTextPresenter(MailEditTextContract.View listener) {
        this.listener = listener;
    }

    @Override
    public void format(@Nullable Editable s) {
        if (!s.toString().equals(temp)) {
            if (validarCorreo(s.toString())){
                cursor = listener.obtenerUtlimaSeleccion();
                temp = s.toString();
                if (temp.contains("@")){
                    ArrayList<String> options = new ArrayList<>(Arrays.asList(
                            "gmail.com",
                            "outlook.com",
                            "live.com",
                            "hotmail.com",
                            "yahoo.com"
                    ));
                    listener.configurarIconoMenu(options);
                }else{
                    listener.ocultarIconoMenu();
                }
                listener.cambiarTexto(temp);
                listener.cambiarSeleccion(cursor);
            }else{
                listener.cambiarTexto(temp);
                listener.cambiarSeleccion(temp.length());
            }
        }
    }

    private boolean validarCorreo(String s) {

         boolean success = false;
         Pattern patron = Pattern.compile("[0-9A-Za-z-@._/]{1,150}");
         Matcher comprobacion = patron.matcher(s);
         if (comprobacion.matches()){
             success = true;
         }
        if (s.isEmpty()){
            success = true;
        }
         return success;

    }

    @Override
    public void configurarCorreoSeleccionado(@Nullable Editable text, @NotNull CharSequence title) {
        String texto = Objects.requireNonNull(text).toString();
        int endPosition = 0;
        for (int i = 0; i <= texto.length(); i++) {
            if (texto.charAt(i) == '@'){
                endPosition = i;
                break;
            }
        }

        temp = texto.substring(0,endPosition+1)+title;

        listener.cambiarTexto(temp);
        listener.cambiarSeleccion(temp.length());
    }
}
