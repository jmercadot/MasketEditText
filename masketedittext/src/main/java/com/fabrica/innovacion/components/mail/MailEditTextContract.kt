package com.fabrica.innovacion.components.mail

import android.text.Editable
import java.util.ArrayList

interface MailEditTextContract {
    interface Presenter {
        fun format(s: Editable?)
        fun configurarCorreoSeleccionado(text: Editable?, title: CharSequence)
    }

    interface View {
        fun cambiarTexto(temp: String)
        fun cambiarSeleccion(length: Int)
        fun obtenerUtlimaSeleccion() :Int
        fun ocultarIconoMenu()
        fun configurarIconoMenu(options: ArrayList<String>)
    }
}
