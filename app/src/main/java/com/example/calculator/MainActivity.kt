package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.calculator.code.Algorithm
import com.example.calculator.code.Calculator
import com.example.calculator.code.Parser
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textForm = findViewById<EditText>(R.id.editText)
        val textView = findViewById<TextView>(R.id.textView2)
        findViewById<Button>(R.id.button2).setOnClickListener {
            textView.text = Calculator.computeFromString(textForm.text.toString())
        }
    }
}
