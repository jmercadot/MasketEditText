package com.fabrica.innovacion.components


import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.PopupMenu
import com.fabrica.innovacion.components.creditcard.CreditCardEditTextContract
import com.fabrica.innovacion.components.creditcard.CreditCardEditTextPresenter
import com.fabrica.innovacion.components.mail.MailEditTextContract
import com.fabrica.innovacion.components.mail.MailEditTextPresenter
import com.fabrica.innovacion.components.money.MoneyEditTextContract
import com.fabrica.innovacion.components.money.MoneyEditTextPresenter
import com.fabrica.innovacion.components.phone.PhoneEdiTextPresenter
import com.fabrica.innovacion.components.phone.PhoneEditTextContract
import java.util.*
import kotlin.collections.ArrayList


class MasketEditText : AppCompatEditText, TextWatcher, MoneyEditTextContract.View,
    PhoneEditTextContract.View, MailEditTextContract.View, CreditCardEditTextContract.View {


    private lateinit var gsMoneyEditTextPresenter: MoneyEditTextContract.Presenter
    private lateinit var phoneEdiTextPresenter: PhoneEditTextContract.Presenter
    private lateinit var mailEditTextPresenter: MailEditTextContract.Presenter
    private lateinit var creditCardEditTextPresenter: CreditCardEditTextPresenter

    private var textFormat: Int? = 0
    private var isDecimal: Int? = 0
    private var isParenthesis: Int? = 0
    private var isMaterialSelector: Int? = 0

    private lateinit var opciones: ArrayList<String>

    public var lastError: String = ""

    companion object {
        const val MONEY: Int = 0
        const val PHONE: Int = 1
        const val MAIL: Int = 2
        const val NO_EDITABLE_TEXT = 3
        const val CREDITCARD: Int = 4

        const val False = 0
        const val True = 1
        const val Hidden = 2
    }

    constructor(context: Context?) : super(context) {
        setParams()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val attrs = context.obtainStyledAttributes(attrs, R.styleable.MasketEditText)

        textFormat = attrs.getInt(R.styleable.MasketEditText_editTextFormat, 0)
        isDecimal = attrs.getInt(R.styleable.MasketEditText_isDecimal, 0)
        isParenthesis = attrs.getInt(R.styleable.MasketEditText_parentesis, 0)
        isMaterialSelector = attrs.getInt(R.styleable.MasketEditText_materialSelector, 0)
        opciones = ArrayList()
        setParams()
    }

    private fun setParams() {

        this.addTextChangedListener(this)

        gsMoneyEditTextPresenter =
            MoneyEditTextPresenter(this)
        phoneEdiTextPresenter =
            PhoneEdiTextPresenter(this)
        mailEditTextPresenter =
            MailEditTextPresenter(this)
        creditCardEditTextPresenter =
            CreditCardEditTextPresenter(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        }

        when (textFormat) {
            MONEY, PHONE, CREDITCARD -> {
                this.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                this.keyListener = DigitsKeyListener.getInstance("0123456789")
            }
            MAIL -> {
                this.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }

            NO_EDITABLE_TEXT->{
                this.isClickable = true
                this.isFocusable = false
                this.isLongClickable = false
                this.keyListener = null
                this.inputType = InputType.TYPE_NULL
                this.isCursorVisible = false;
                this.isPressed = false;
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.anim_arrow,
                    0
                )
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        when (textFormat) {
            MONEY -> gsMoneyEditTextPresenter.formatText(s, isDecimal!!)
            PHONE -> phoneEdiTextPresenter.formatText(s, isParenthesis!!)
            MAIL -> mailEditTextPresenter.format(s)
            CREDITCARD -> creditCardEditTextPresenter.formatText(s)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {

    }

    override fun cambiarSeleccion(length: Int) {
        try {
            setSelection(length)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun cambiarTexto(temp: String) {
        setText(temp)
    }

    override fun obtenerUtlimaSeleccion(): Int {
        return getSelectionEnd()
    }

    override fun ocultarIconoMenu() {
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val DRAWABLE_RIGHT = 2
        try {
            if (event!!.action == MotionEvent.ACTION_UP && event.rawX >= this.right - this.compoundDrawables[DRAWABLE_RIGHT].getBounds()
                    .width()
            ) {
                configuraPupUP()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return super.onTouchEvent(event)
        }
    }

    private fun configuraPupUP() {
        //Creating the instance of PopupMenu
        val popup = PopupMenu(context, this, Gravity.END)
        //Inflating the Popup using xml file
        for (opcion in opciones) {
            popup.menu.add(opcion)
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener { item ->
            mailEditTextPresenter.configurarCorreoSeleccionado(text, item.title)
            true
        }

        popup.show() //showing popup menu
    }


    override fun configurarIconoMenu(opciones: ArrayList<String>) {
        var materialSelector = R.drawable.anim_arrow
        if (isMaterialSelector == True) {
            materialSelector = R.drawable.anim_arrow_circle
        }
        this.opciones = opciones
        this.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            materialSelector,
            0
        )

        val MAX_LEVEL = 10000

        val myTextViewCompoundDrawables = this.getCompoundDrawables()
        for (drawable in myTextViewCompoundDrawables) {
            if (drawable == null)
                continue

            val anim = ObjectAnimator.ofInt(drawable, "level", 0, MAX_LEVEL)
            anim.startDelay = 500
            anim.start()
        }
    }

    override fun checkTargeta(substring: String) {
        var materialSelector = 0

        when (substring) {
            "4" -> {
                 materialSelector = R.drawable.ic_visa
            }
            "5" -> {
                 materialSelector = R.drawable.ic_mastercard
            }
            "3" -> {
                 materialSelector = R.drawable.ic_american_express
            }
            else -> {
                 materialSelector = 0
            }
        }

        this.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            materialSelector,
            0
        )
    }

    //Metodos publicos
    fun setTextFormat(textFormat: Int) {
        this.textFormat = textFormat
        setParams()
    }

    fun setDecimal(isDecimal: Int) {
        this.isDecimal = isDecimal
        setParams()
    }

    fun getLongValue(): Long? {
        if (text.toString().isEmpty()) {
            return 0
        }
        val arr = Objects.requireNonNull<Editable>(text).toString().split("\\.".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        var cleanString = arr[0]
        cleanString = cleanString.replace("[$,]".toRegex(), "")

        return java.lang.Long.parseLong(if (cleanString.isEmpty()) "0" else cleanString)
    }

    fun getDoubleValue(): Double {
        if (text.toString().isEmpty()) {
            return 0.0
        }
        var cleanString = Objects.requireNonNull<Editable>(text).toString()
        cleanString = cleanString.replace("[$,]".toRegex(), "")
        return java.lang.Double.parseDouble(if (cleanString.isEmpty()) "0" else cleanString)
    }

    fun getPhoneNumber(): String {
        return if (textFormat == PHONE) phoneEdiTextPresenter.getPhone() else ""
    }

    fun getLada(): String {
        return if (textFormat == PHONE) phoneEdiTextPresenter.getLada() else ""
    }

    fun getSimplePhone(): String {
        return if (textFormat == PHONE) phoneEdiTextPresenter.getSimplePhone() else ""
    }

    fun esTelefonoValido(): Boolean {
        val phone = if (textFormat == PHONE) phoneEdiTextPresenter.getPhone() else ""
        if (phone.equals("")) {
            lastError = "El telefono se encuentra vacio, valida tus datos"
            return false;
        } else if (phone.length < 10) {
            lastError = "El numero de telefono debe de ser de diez numeros, favor, valida tus datos"
            return false;
        }
        return true;
    }
}