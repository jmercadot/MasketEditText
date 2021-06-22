package com.fabrica.innovacion.components.phone;


import android.text.Editable;
import android.util.Log;

import com.fabrica.innovacion.components.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneEdiTextPresenter implements PhoneEditTextContract.Presenter {

    private final PhoneEditTextContract.View viewListener;
    private int isParenthesis;

    private String temp = "";
    private String cleanString = "";

    private static final String TAG = PhoneEdiTextPresenter.class.getSimpleName();
    private Editable s;

    public PhoneEdiTextPresenter(PhoneEditTextContract.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void formatText(@Nullable Editable s, @Nullable Integer parenthesis) {
        this.isParenthesis = parenthesis;
        this.s = s;

        if (!s.toString().equals(temp)) {
            if (!s.toString().equals("0")) {
                switch (isParenthesis) {
                    case 0:
                    case 3:
                        formatoGuion();
                        break;
                    case 1:
                    case 2:
                        formatoParentesis();
                        break;
                }
            } else {
                viewListener.cambiarTexto(temp);
            }
            viewListener.cambiarSeleccion(temp.length());
        }
    }


    private void formatoParentesis() {
        if (s.length() < 15) {
            cleanString = Utils.limpiarCadenas(s.toString());
            if (cleanString.length() >= 2) {
                if (validarRepetidos() && validarConsecutivos()) {
                    formatearParentesis();
                    formatearGuionParentesis();
                } else {
                    cleanString = temp;
                }
            } else {
                temp = cleanString;
            }
            Log.i(TAG, "Diference:\n" + s + "\n" + temp);
            viewListener.cambiarTexto(temp);
        } else {
            viewListener.cambiarTexto(temp);
        }
    }

    private void formatearGuionParentesis() {
        if (cleanString.substring(0, 2).equals("55") || cleanString.substring(0, 2).equals("33") || cleanString.substring(0, 2).equals("81")) {
            if (cleanString.length() >= 7) {
                temp = temp.substring(0, 5) + cleanString.substring(2, 6);
                if (isParenthesis == 2) {
                    temp = temp + " " + cleanString.substring(6);
                } else {
                    temp = temp + "-" + cleanString.substring(6);
                }
            }
        } else {
            if (cleanString.length() >= 8) {
                temp = temp.substring(0, 6) + cleanString.substring(3, 6);
                if (isParenthesis == 2) {
                    temp = temp + " " + cleanString.substring(6);
                } else {
                    temp = temp + "-" + cleanString.substring(6);
                }
            }
        }
    }

    private void formatearParentesis() {
        if (cleanString.substring(0, 2).equals("55") || cleanString.substring(0, 2).equals("33") || cleanString.substring(0, 2).equals("81")) {
            if (s.length() != 4) {
                temp = "(" + cleanString.substring(0, 2) + ") ";
                temp = temp + cleanString.substring(2);
            } else {
                temp = cleanString.substring(0, 1);
            }
        } else {
            if (cleanString.length() >= 3) {
                if (s.length() != 5) {
                    temp = "(" + cleanString.substring(0, 3) + ") ";
                    temp = temp + cleanString.substring(3);
                } else {
                    temp = cleanString.substring(0, 2);
                }
            } else {
                temp = cleanString;
            }
        }
    }

    private void formatoGuion() {
        if (s.length() < 13) {
            cleanString = Utils.limpiarCadenas(s.toString());
            if (cleanString.length() >= 2) {
                if (validarRepetidos() && validarConsecutivos()) {
                    formatearGuionLada();
                    formatearGuion();
                } else {
                    cleanString = temp;
                }
            } else {
                temp = cleanString;
            }

            Log.i(TAG, "Diference:\n" + s + "\n" + temp);
            viewListener.cambiarTexto(temp);
            viewListener.cambiarSeleccion(temp.length());
        } else {
            viewListener.cambiarTexto(temp);
        }
    }

    private void formatearGuionLada() {
        if (cleanString.substring(0, 2).equals("55") || cleanString.substring(0, 2).equals("33") || cleanString.substring(0, 2).equals("81")) {
            if (s.length() != 2) {
                if (temp.length() == 4 && cleanString.length() == 2) {
                    temp = cleanString;
                } else {
                    if (isParenthesis == 3) {
                        temp = cleanString.substring(0, 2) + " ";
                    } else {
                        temp = cleanString.substring(0, 2) + "-";
                    }
                    temp = temp + cleanString.substring(2);
                }
            } else {
                temp = cleanString;
            }
        } else {
            if (cleanString.length() >= 3) {
                if (s.length() != 3) {
                    if (temp.length() == 5 && cleanString.length() == 3) {
                        temp = cleanString.substring(0, 3);
                    } else {
                        if (isParenthesis == 3) {
                            temp = cleanString.substring(0, 3) + " ";
                        } else {
                            temp = cleanString.substring(0, 3) + "-";
                        }
                        temp = temp + cleanString.substring(3);
                    }
                } else {
                    temp = cleanString.substring(0, 3);
                }
            } else {
                temp = cleanString;
            }
        }
    }

    private void formatearGuion() {
        if (cleanString.substring(0, 2).equals("55") || cleanString.substring(0, 2).equals("33") || cleanString.substring(0, 2).equals("81")) {
            if (cleanString.length() >= 7) {
                temp = temp.substring(0, 3) + cleanString.substring(2, 6);
                if (isParenthesis == 3) {
                    temp = temp + " " + cleanString.substring(6);
                } else {
                    temp = temp + "-" + cleanString.substring(6);
                }
            }
        } else {
            if (cleanString.length() >= 8) {
                temp = temp.substring(0, 4) + cleanString.substring(3, 6);
                if (isParenthesis == 3) {
                    temp = temp + " " + cleanString.substring(6);
                } else {
                    temp = temp + "-" + cleanString.substring(6);
                }

            }
        }
    }

    private boolean validarRepetidos() {
        String regex = "(.)\\1{5}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cleanString);
        return !matcher.find();
    }

    private boolean validarConsecutivos() {
        String regex = "^(?!.*(?:012345|123456|234567|543210|345678|654321|456789|765432|876543|987654)).+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cleanString);
        return matcher.find();
    }

    @NotNull
    @Override
    public String getPhone() {
        return Utils.limpiarCadenas(temp);
    }

    @NotNull
    @Override
    public String getLada() {
        String lada = "";
        String phoneNumber = Utils.limpiarCadenas(temp);
        if (phoneNumber.length() >= 2) {
            if (phoneNumber.substring(0, 2).equals("55") || phoneNumber.substring(0, 2).equals("33") || phoneNumber.substring(0, 2).equals("81")) {
                lada = phoneNumber.substring(0, 2);
            } else if (phoneNumber.length() >= 3) {
                lada = phoneNumber.substring(0, 3);
            }
        }
        return lada;
    }

    @NotNull
    @Override
    public String getSimplePhone() {
        String number = "";
        String phoneNumber = Utils.limpiarCadenas(temp);
        if (phoneNumber.length() >= 2) {
            if (phoneNumber.substring(0, 2).equals("55") || phoneNumber.substring(0, 2).equals("33") || phoneNumber.substring(0, 2).equals("81")) {
                number = phoneNumber.substring(2);
            } else if (phoneNumber.length() >= 3) {
                number = phoneNumber.substring(3);
            }
        }
        return number;

    }
}
