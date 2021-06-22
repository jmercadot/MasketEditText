package com.fabrica.innovacion.components.phone

import android.text.Editable

interface PhoneEditTextContract {
    interface Presenter {
        fun formatText(s: Editable?, parenthesis: Int?)
        fun getPhone(): String
        fun getLada(): String
        fun getSimplePhone(): String
    }

    interface View {
        fun cambiarSeleccion(length: Int)
        fun cambiarTexto(temp: String)
    }
}