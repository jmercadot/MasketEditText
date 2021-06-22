package com.fabrica.innovacion.components.creditcard;

import android.text.Editable;

import com.fabrica.innovacion.components.utils.Utils;

public class CreditCardEditTextPresenter implements CreditCardEditTextContract.Presenter {
    private CreditCardEditTextContract.View viewListener;
    private String temp = "";
    private String realText = "";
    private String cleanString = "";
    private int selection;


    public CreditCardEditTextPresenter(CreditCardEditTextContract.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void formatText(Editable s) {
        if (!s.toString().equals(temp)) {
            if (s.toString().length() < 24) {
                cleanString = Utils.limpiarCadenas(s.toString()).replace(" ", "");
                if (cleanString.length() < 5) {
                    temp = cleanString;
                    selection = viewListener.obtenerUtlimaSeleccion() == 5 ? temp.length() : viewListener.obtenerUtlimaSeleccion();
                } else if (cleanString.length() < 9) {
                    temp = cleanString.substring(0, 4) + " " + cleanString.substring(4);
                    selection = viewListener.obtenerUtlimaSeleccion() == 5 ? viewListener.obtenerUtlimaSeleccion() + 1 :
                            viewListener.obtenerUtlimaSeleccion() == 10 ? temp.length() : viewListener.obtenerUtlimaSeleccion();
                } else if (cleanString.length() < 13) {
                    temp = cleanString.substring(0, 4) + " " + cleanString.substring(4, 8) + " " + cleanString.substring(8);
                    selection = viewListener.obtenerUtlimaSeleccion() == 10 ? temp.length() : viewListener.obtenerUtlimaSeleccion() == 15 ? temp.length() : viewListener.obtenerUtlimaSeleccion();
                } else if (cleanString.length() < 17) {
                    temp = cleanString.substring(0, 4) + " " + cleanString.substring(4, 8) + " " + cleanString.substring(8, 12) + " " + cleanString.substring(12);
                    selection = viewListener.obtenerUtlimaSeleccion() == 15 ? temp.length() : viewListener.obtenerUtlimaSeleccion();
                }

                viewListener.checkTargeta(s.toString().isEmpty() ? "": s.toString().substring(0,1));
                viewListener.cambiarTexto(temp);
                viewListener.cambiarSeleccion(selection);
            } else {
                viewListener.cambiarTexto(temp);
            }
        }
    }
}
