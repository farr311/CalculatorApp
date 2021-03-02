package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView

//TODO: Make buttons change colors when pressed
//TODO: Display text as integer if fractional part is 0
//TODO: Add ripple effect for buttons
//TODO: Add automated tests for every operation
//TODO: Add self-building automated tests with logging
//TODO: Hide application title
//TODO: Add support for daisy-chaining operations (immediately assign result as value1)
//TODO: Fix Clear button
//TODO: Add landscape orientation support with enhances specter of available operations
//TODO: Add support for dark theme
//TODO: Set selected operation button color to @colors/colorSelected
class MainActivity : AppCompatActivity() {
    private var buttons = arrayOfNulls<Button>(11)
    private var operationButtons = arrayOfNulls<Button>(7)
    private lateinit var valueTextView : TextView
    private lateinit var clearButton: Button

    //Signifies that the current displayed value must be reset with the next input
    private var refill = false

    private var value1 : Double? = null //First operand for both unary and binary operations
    private var value2 : Double? = null //Second operand for binary operations

    private var operation = Operations.ADDITION //Current operation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        valueTextView = findViewById(R.id.valueText)


        //TODO: replace with group selection
        operationButtons[0] = findViewById(R.id.buttonDivide)
        operationButtons[1] = findViewById(R.id.buttonMultiply)
        operationButtons[2] = findViewById(R.id.buttonSubtract)
        operationButtons[3] = findViewById(R.id.buttonAdd)
        operationButtons[4] = findViewById(R.id.buttonEquals)
        operationButtons[5] = findViewById(R.id.buttonSign)
        operationButtons[6] = findViewById(R.id.buttonPercent)

        buttons[0] = findViewById(R.id.button0)
        buttons[1] = findViewById(R.id.button1)
        buttons[2] = findViewById(R.id.button2)
        buttons[3] = findViewById(R.id.button3)
        buttons[4] = findViewById(R.id.button4)
        buttons[5] = findViewById(R.id.button5)
        buttons[6] = findViewById(R.id.button6)
        buttons[7] = findViewById(R.id.button7)
        buttons[8] = findViewById(R.id.button8)
        buttons[9] = findViewById(R.id.button9)
        buttons[10] = findViewById(R.id.buttonDot)

        clearButton = findViewById(R.id.buttonClear)

        for (b in buttons) {
            b?.setOnClickListener{
                if (valueTextView.length() < 15) {
                    var nextChar = b.text as String

                    if (nextChar == "." && valueTextView.text.contains("\\.")) {
                        nextChar = ""
                    }

                    //If current value is 0, it must be overridden with the next digit
                    //Otherwise it should be concatenated with the next digit
                    valueTextView.text = if (valueTextView.text == "0" && nextChar != "." || refill)
                        nextChar else valueTextView.text.toString() + nextChar

                    refill = false
                }
            }
        }

        for (b in operationButtons) {
            b?.setOnClickListener{
                refill = true

                if (b.id != R.id.buttonEquals) {
                    value1 = valueTextView.text.toString().toDouble()
                } else {
                    value2 = valueTextView.text.toString().toDouble()
                }

                when (b.id) {
                    R.id.buttonAdd -> operation = Operations.ADDITION
                    R.id.buttonSubtract -> operation = Operations.SUBTRACTION
                    R.id.buttonMultiply -> operation = Operations.MULTIPLICATION
                    R.id.buttonDivide -> operation = Operations.DIVISION
                    R.id.buttonPercent -> {
                        operation = Operations.NEGATION
                        valueTextView.text = applyOperation().toString()
                    }
                    R.id.buttonSign -> {
                        operation = Operations.PERCENTAGE
                        valueTextView.text = applyOperation().toString()
                    }
                    R.id.buttonEquals -> {
                        refill = false
                        valueTextView.text = applyOperation().toString()
                    }
                }
            }
        }

        clearButton.setOnClickListener{
            valueTextView.text = "0"
            value1 = null
            value2 = null
        }
    }

    private fun applyOperation() : Double {
        val returnValue : Double

        if (value1 != null && value2 != null) {
            returnValue = when (operation) {
                Operations.SUBTRACTION -> value1 as Double - value2 as Double
                Operations.DIVISION -> value1 as Double / value2 as Double
                Operations.ADDITION -> value1 as Double + value2 as Double
                Operations.MULTIPLICATION -> value1 as Double * value2 as Double
                else -> {
                    if (operation == Operations.NEGATION) {
                        -(value1 as Double)
                    } else {
                        value1 as Double / 100
                    }
                }
            }

            value1 = null
            value2 = null
        } else {
            returnValue = value1 ?: value2 ?: 0.0
        }

        //Doesn't let user directly increase the value of the result without using operations
        refill = true

        return returnValue
    }

    /*private fun resetOperationButtonsColors() {
        for (b in operationButtons) {
            //
        }
    }*/
}
