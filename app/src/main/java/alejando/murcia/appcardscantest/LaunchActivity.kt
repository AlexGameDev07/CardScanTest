package alejando.murcia.appcardscantest

import alejando.murcia.appcardscantest.databinding.ActivityLaunchBinding
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.stripe.android.stripecardscan.cardimageverification.CardImageVerificationSheet
import com.stripe.android.stripecardscan.cardimageverification.CardImageVerificationSheetResult
import com.stripe.android.stripecardscan.cardscan.CardScanSheet
import com.stripe.android.stripecardscan.cardscan.CardScanSheetResult
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import alejando.murcia.appcardscantest.retrofit.ApiService
import alejando.murcia.appcardscantest.model.Card
import alejando.murcia.appcardscantest.model.VerificationResponse

class LaunchActivity : AppCompatActivity(), CardScanSheet.CardScanResultCallback, CardImageVerificationSheet.CardImageVerificationResultCallback {

    private lateinit var cardScanSheet: CardScanSheet
    private lateinit var cardImageVerificationSheet: CardImageVerificationSheet
    private lateinit var binding: ActivityLaunchBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://tu-backend.com/api/") // Cambia esta URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Configuración de Stripe
        cardScanSheet = CardScanSheet.create(
            from = this,
            stripePublishableKey = "pk_test_51QFejhGahKZKcf3DzJucAJodMJKg99whCV7141ICxy7gpreeubVO6oLAsHTUf9bCPUmv4r9MXVWARWilnRGod5YS00XQL09pXh",
            cardScanSheetResultCallback = this
        )

        cardImageVerificationSheet = CardImageVerificationSheet.create(
            from = this,
            stripePublishableKey = "pk_test_51QFejhGahKZKcf3DzJucAJodMJKg99whCV7141ICxy7gpreeubVO6oLAsHTUf9bCPUmv4r9MXVWARWilnRGod5YS00XQL09pXh",
            cardImageVerificationResultCallback = this
        )

        binding.btnEscanear.setOnClickListener {
            cardScanSheet.present()
        }

        binding.btnVerification.setOnClickListener {
            createVerificationIntent()
        }
    }

    private fun createVerificationIntent() {
        // Llamada a la API para crear la verificación
        val card = Card(pan = "4000001234567899", expiryMonth = 12, expiryYear = 2025, cvc = "123")
        val call = apiService.createVerification(card)

        call.enqueue(object : Callback<VerificationResponse> {
            override fun onResponse(call: Call<VerificationResponse>, response: Response<VerificationResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    verificationResponse?.let {
                        // Usar los valores de intentId e intentSecret para presentar la verificación
                        cardImageVerificationSheet.present(
                            cardImageVerificationIntentId = it.intentId,
                            cardImageVerificationIntentSecret = it.intentSecret
                        )
                    }
                } else {
                    Log.e("Verification", "Error en la respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
                Log.e("Verification", "Error de red: ${t.message}")
            }
        })
    }

    // Manejo de resultados del escaneo de tarjeta
    override fun onCardScanSheetResult(result: CardScanSheetResult) {
        when (result) {
            is CardScanSheetResult.Completed -> {
                val scannedCard = result.scannedCard
                binding.editTextCardNumber.setText(scannedCard.pan)
                Log.i("CardScan", "Scanned PAN: ${scannedCard.pan}")
            }
            is CardScanSheetResult.Canceled -> {
                Log.i("CardScan", "Scan canceled due to: ${result.reason}")
            }
            is CardScanSheetResult.Failed -> {
                Log.e("CardScan", "Scan failed with error: ${result.error.message}")
            }
            else -> Log.e("CardScan", "Unknown scan result.")
        }
    }

    override fun onCardImageVerificationSheetResult(result: CardImageVerificationSheetResult) {
        when(result){
            is CardImageVerificationSheetResult.Completed -> {
                Log.i("ImageVerification", "Card scanned with PAN: ${result.scannedCard.pan}")
            }
            is CardImageVerificationSheetResult.Canceled -> {
                Log.i("ImageVerificationCanceled", result.reason.toString())
            }
            is CardImageVerificationSheetResult.Failed -> {
                Log.e("ImageVerificationError", result.error.message.toString())
            }
        }
    }
}
