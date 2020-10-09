package com.example.calculator

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.calculator.code.Algorithm
import com.example.calculator.code.Parser

class MainActivity : AppCompatActivity() {
    lateinit var textForm: EditText
    lateinit var textView: TextView
    lateinit var computeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textForm = findViewById(R.id.editText)
        textView = findViewById(R.id.textView2)
        computeButton = findViewById(R.id.button2)

        computeButton.setOnClickListener {
            Calculator().execute(textForm.text.toString())
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class Calculator : AsyncTask<String, Unit, String>() {
        override fun doInBackground(vararg params: String): String? {
            try {
                val result = Algorithm(Parser.parseToTokens(params[0])).evaluate()
                return "Result:\n$result"
            } catch (e: IllegalArgumentException) {
                return "Error while parsing:\n" + e.message
            } catch (e: ArithmeticException) {
                return "Error while computing:\n" + e.message
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            textView.text = result
        }
    }
}
