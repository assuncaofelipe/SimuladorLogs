package home.felipe.topswaptest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    // Modem Silent
    val sampleLogMessagesModemSilent = listOf(
        "Modem is operating normally.", // info
        "Fatal error on modem! Detected issue UNSOL_MODEM_RESTART", // error
        "Connection lost, retrying..." // warning
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSimulateANR).setOnClickListener {
            simulateAnrTP()
        }

        findViewById<Button>(R.id.btnSimulateTopSwap).setOnClickListener {
            simulateTopSwap()
        }

        /* findViewById<Button>(R.id.btnsimulateFatalException).setOnClickListener {
            simulateFatalExceptionOutOfMemoryTS()
        } */

        findViewById<Button>(R.id.btnFatalWFC).setOnClickListener {
            criticalWifiStateMachine()
        }

        findViewById<Button>(R.id.btnModemCrash).setOnClickListener {
            modemCrashMC()
        }

        findViewById<Button>(R.id.btnModemFatalError).setOnClickListener {
            modemFatalErrorMC()
        }

        findViewById<Button>(R.id.btnModemSilent).setOnClickListener {
            modemSilent(sampleLogMessagesModemSilent)
        }

        findViewById<Button>(R.id.btnKernelPanic).setOnClickListener {
            kernelPanic()
        }

        findViewById<Button>(R.id.btnOverallCritical).setOnClickListener {
            overallCritica()
        }

        findViewById<Button>(R.id.btnKernelPanicLoop).setOnClickListener {
            simulateKernelPanicLoop()
        }
    }

    private fun simulateAnrTP() {
        try {
            Thread.sleep(20000) // Simula um ANR (espera por 20 segundos)
        } catch (e: InterruptedException) {
            Log.e("ThirdParty", "Erro ao simular ANR: ${e.message}")
        }
    }

    // Faz com que a thread durma por um tempo indeterminado
    private fun simulateKernelPanicLoop() {
        while (true) {
            try {
                Thread.sleep(Long.MAX_VALUE)
            } catch (e: InterruptedException) {
                Log.e("KernelPanic", "Erro ao simular KP: ${e.message}")
            }
        }
    }

    // criando um erro "kernel panic" de fatal exception.
    // Ele resultará em um erro genérico
    // a função possui adb que preciso de acesso ao root e tb vai alocar um erro de
    private fun simulateFatalExceptionOutOfMemoryTS() {
        try {
            val largeList = mutableListOf<ByteArray>()
            while (true) {
                val array = ByteArray(1024 * 1024 * 1024) // 1 GB
                largeList.add(array)
                Log.i("TopSwap", "Alocado 1GB, total alocado: ${largeList.size} GB")

                // Executa comando ADB para verificar o status da memória
                val memoryInfo = Runtime.getRuntime()
                    .exec("adb shell dumpsys meminfo") // verificar o status da memória durante a execução do teste de alocação de memória.
                val reader = BufferedReader(InputStreamReader(memoryInfo.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    Log.i("MemoryInfo", line.toString())
                }
            }
        } catch (e: OutOfMemoryError) {
            Log.e("TopSwap", "${e.message}")

            // Executa comando ADB para coletar informações sobre o crash
            val crashInfo = Runtime.getRuntime()
                .exec("adb shell dumpsys activity crash") // verificar o status da memória durante a execução do teste de alocação de memória.
            val reader = BufferedReader(InputStreamReader(crashInfo.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                Log.e("CrashInfo", line.toString())
            }
        }
    }

    private fun simulateTopSwap() {
        try {
            val largeList = mutableListOf<ByteArray>()
            while (true) {
                // aloca 1GB por vez enquanto tiver memória livre
                val array = ByteArray(1024 * 1024 * 1024) // 1 GB
                largeList.add(array)
                Log.i("TopSwap", "Alocado 1GB, total alocado: ${largeList.size} GB")
            }
        } catch (e: OutOfMemoryError) {
            Log.e("TopSwap", "${e.message}")
        }
    }

    private fun criticalWifiStateMachine() {
        Log.e("WifiCritical", "FATAL EXCEPTION IN SYSTEM PROCESS: WifiStateMachine")
    }

    private fun modemCrashMC() {
        Log.e("ModemCritical", "Modem crashed")
    }

    private fun modemFatalErrorMC() {
        Log.e("ModemCritical", "Modem Fatal Error")
    }

    private fun kernelPanic() {
        Log.e("KernelPanic", "Kernel panic")
    }

    private fun overallCritica(){
        Log.e("OverallCritical", "CPU usage from")
    }

    private fun modemSilent(logMessages: List<String>) {
        val modemErrorPattern = Regex("Fatal error on modem!.*UNSOL_MODEM_RESTART", RegexOption.IGNORE_CASE)

        logMessages.forEach { message ->
            if (modemErrorPattern.containsMatchIn(message)) {
                Log.e("ModemSilent", "$message")
            }
        }
    }
}