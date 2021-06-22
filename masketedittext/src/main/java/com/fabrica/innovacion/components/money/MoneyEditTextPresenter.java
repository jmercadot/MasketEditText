package com.fabrica.innovacion.components.money;


import android.text.Editable;
import android.util.Log;

import com.fabrica.innovacion.components.MasketEditText;
import com.fabrica.innovacion.components.utils.Utils;

import org.jetbrains.annotations.Nullable;

public class MoneyEditTextPresenter implements MoneyEditTextContract.Presenter {

    private final MoneyEditTextContract.View viewListener;
    private static final String TAG = MoneyEditTextPresenter.class.getSimpleName();

    private String temp = "";
    private String cleanString = "";
    private int isDecimal = 0;

    private int cursor;

    public MoneyEditTextPresenter(MoneyEditTextContract.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void formatText(@Nullable Editable s, int decimal) {
        isDecimal = decimal;
        Log.i(TAG, "despues");
        switch (isDecimal) {
            case MasketEditText.True:
                setTextWhitDecimal(s);
                break;
            case MasketEditText.False:
                setTextWhitOutDecimal(s);
                break;
            case MasketEditText.Hidden:
                setTextWhitDecimalHidden(s);
                break;
        }
    }

    private void setTextWhitDecimal(Editable s) {
        if (!s.toString().equals(temp)) {
            if (s.length() < 21 && !s.toString().isEmpty()) {
                String cleanString = Utils.limpiarCadenas(s.toString());
                double parsed = Double.parseDouble(cleanString);

                java.util.Currency usd = java.util.Currency.getInstance("USD");
                java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(
                        java.util.Locale.US);
                format.setCurrency(usd);

                String formatted = format.format((parsed / 100));
                temp = parsed == 0.0 ? "" : formatted;
                viewListener.cambiarTexto(temp);
                viewListener.cambiarSeleccion(temp.length());
            } else {
                viewListener.cambiarTexto(temp);
                viewListener.cambiarSeleccion(temp.length());
            }
        }
    }

    private void setTextWhitOutDecimal(Editable s) {
        if (!s.toString().equals(temp)) {
            if (s.length() < 21 && !s.toString().isEmpty()) {
                boolean isFinalCursor;
                if (s.toString().contains(".00")) {
                    String[] arr = s.toString().split("\\.00");
                    if (arr.length != 1) {
                        cleanString = Utils.limpiarCadenas(arr[0]) + Utils.limpiarCadenas(arr[1]);
                        isFinalCursor = true;
                    } else {
                        cleanString = s.toString().replace(".00", "");
                        if (cleanString.equals("$")) {
                            cleanString = "0";
                            isFinalCursor = true;
                        } else {
                            cleanString = Utils.limpiarCadenas(cleanString);
                            isFinalCursor = false;
                        }
                    }

                } else {
                    isFinalCursor = true;
                    if (s.toString().contains(".0")) {
                        String stringTemp = s.toString().replace(".0", "");
                        stringTemp = stringTemp.substring(0, stringTemp.length() - 1);
                        cleanString = Utils.limpiarCadenas(stringTemp);
                    } else if (!s.toString().contains(".")) {
                        String stringTemp;
                        if (s.toString().length() != 1) {
                            stringTemp = s.toString();//.substring(0, s.toString().length() - 3);
                        } else {
                            stringTemp = s.toString();
                        }
                        stringTemp = Utils.limpiarCadenas(stringTemp);
                        cleanString = stringTemp;
                    } else {
                        String[] arr = s.toString().split("\\.");
                        if (arr[1].length() > 2) {
                            cleanString = arr[0] + arr[1].substring(0, 1);
                            cleanString = Utils.limpiarCadenas(cleanString);
                            isFinalCursor = false;
                        } else {
                            cleanString = Utils.limpiarCadenas(s.toString());
                        }
                    }
                }

                double parsed = Double.parseDouble(cleanString.isEmpty() ? "0" : cleanString);

                java.util.Currency usd = java.util.Currency.getInstance("USD");
                java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(
                        java.util.Locale.US);
                format.setCurrency(usd);

                String formatted = format.format(parsed);

                temp = parsed == 0.0 ? "" : formatted;
                if (isFinalCursor) {
                    cursor = temp.length();
                } else {
                    cursor = viewListener.obtenerUtlimaSeleccion();
                    if (formatted.contains(",")) {
                        cursor = viewListener.obtenerUtlimaSeleccion() + 1;
                    }
                }
                viewListener.cambiarTexto(temp);
                viewListener.cambiarSeleccion(cursor);

            } else {

                viewListener.cambiarTexto(temp);
                viewListener.cambiarSeleccion(cursor);
            }
        }
    }

    private void setTextWhitDecimalHidden(Editable s) {
        if (!s.toString().equals(temp)) {
            if (s.length() < 21 && !s.toString().isEmpty()) {
                String cleanString = Utils.limpiarCadenas(s.toString());
                double parsed = Double.parseDouble(cleanString.isEmpty() ? "0" : cleanString);

                java.util.Currency usd = java.util.Currency.getInstance("USD");
                java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(
                        java.util.Locale.US);
                format.setCurrency(usd);

                String formatted = format.format(parsed).replace(".00", "");
                temp = parsed == 0.0 ? "" : formatted;
                viewListener.cambiarTexto(temp);
                viewListener.cambiarSeleccion(temp.length());
            } else {
                viewListener.cambiarTexto(temp);
                viewListener.cambiarSeleccion(temp.length());
            }
        }
    }
}
