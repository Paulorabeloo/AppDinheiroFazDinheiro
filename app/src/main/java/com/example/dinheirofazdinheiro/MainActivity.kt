package com.example.dinheirofazdinheiro

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var valorInvestidoInput: TextInputEditText
    private lateinit var aporteMensalInput: TextInputEditText
    private lateinit var taxaJurosInput: TextInputEditText
    private lateinit var inflacaoInput: TextInputEditText
    private lateinit var periodoInput: TextInputEditText
    private lateinit var resultadoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        valorInvestidoInput = findViewById(R.id.investmentInput)
        aporteMensalInput = findViewById(R.id.monthlyInput)
        taxaJurosInput = findViewById(R.id.interestRateInput)
        inflacaoInput = findViewById(R.id.inflationInput)
        periodoInput = findViewById(R.id.periodInput)
        resultadoText = findViewById(R.id.resultText)

        val botaoCalcular: Button = findViewById(R.id.calculateButton)
        val botaoLimpar: Button = findViewById(R.id.clearButton)

        botaoCalcular.setOnClickListener {
            calcularInvestimento()
        }

        botaoLimpar.setOnClickListener {
            limparCampos()
        }
    }

    private fun calcularInvestimento() {
        val valorInvestido = formatarEntrada(valorInvestidoInput.text.toString())
        val aporteMensal = formatarEntrada(aporteMensalInput.text.toString())
        val taxaJuros = formatarEntrada(taxaJurosInput.text.toString())
        val inflacao = formatarEntrada(inflacaoInput.text.toString())
        val periodo = periodoInput.text.toString().toIntOrNull() ?: 0

        if (valorInvestido <= 0 || aporteMensal < 0 || taxaJuros < 0 || inflacao < 0 || periodo <= 0) {
            resultadoText.text = "Por favor, insira valores válidos."
            return
        }

        val taxaJurosMensal = (taxaJuros / 100) / 12

        var totalInvestimento = valorInvestido * Math.pow(1 + taxaJurosMensal, (periodo * 12).toDouble())

        for (i in 1..(periodo * 12)) {
            totalInvestimento += aporteMensal * Math.pow(1 + taxaJurosMensal, ((periodo * 12) - i).toDouble())
        }

        val totalAjustado = totalInvestimento / Math.pow(1 + (inflacao / 100), periodo.toDouble())

        val df = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale("pt", "BR")))
        val resultadoFormatado = df.format(totalAjustado)

        resultadoText.text = "Investimento total no período de $periodo anos: \nR$ $resultadoFormatado"
    }

    private fun formatarEntrada(entrada: String): Double {
        return entrada.replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0
    }

    private fun limparCampos() {
        valorInvestidoInput.text?.clear()
        aporteMensalInput.text?.clear()
        taxaJurosInput.text?.clear()
        inflacaoInput.text?.clear()
        periodoInput.text?.clear()
        resultadoText.text = "Resultado: R$ 0,00"
    }
}
