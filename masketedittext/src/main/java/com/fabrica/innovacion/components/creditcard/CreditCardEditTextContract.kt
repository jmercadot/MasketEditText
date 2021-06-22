package com.fabrica.innovacion.components.creditcard

import android.text.Editable

interface CreditCardEditTextContract {
    interface View{
        fun cambiarSeleccion(length: Int)
        fun cambiarTexto(temp: String)
        fun obtenerUtlimaSeleccion(): Int
        fun checkTargeta(substring: String)
    }
    interface Presenter {
        fun formatText(s: Editable?)
    }
}