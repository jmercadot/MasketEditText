package com.fabrica.innovacion.components.money

import android.text.Editable

interface MoneyEditTextContract {
    interface Presenter {
        fun formatText(s: Editable?, decimal: Int)
    }

    interface View {
        fun cambiarSeleccion(length: Int)
        fun cambiarTexto(temp: String)
        fun obtenerUtlimaSeleccion(): Int
    }
}