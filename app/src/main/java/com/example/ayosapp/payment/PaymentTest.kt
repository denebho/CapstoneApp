package com.example.ayosapp.payment

//import com.example.ayosapp.CartContract
//import com.example.ayosapp.CartPresenter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ayosapp.MainActivity
import com.example.ayosapp.R
import com.paymaya.sdk.android.common.LogLevel
import com.paymaya.sdk.android.common.PayMayaEnvironment
import com.paymaya.sdk.android.common.models.AmountDetails
import com.paymaya.sdk.android.common.models.RedirectUrl
import com.paymaya.sdk.android.common.models.TotalAmount
import com.paymaya.sdk.android.paywithpaymaya.PayWithPayMaya
import com.paymaya.sdk.android.paywithpaymaya.models.SinglePaymentRequest
import org.json.JSONObject

class PaymentTest : AppCompatActivity() {
    private lateinit var button: Button
    private var REQUEST_REFERENCE_NUMBER =0
    private val SINGLE_PAYMENT_REQUEST_CODE = 1001
    private val payWithPayMayaClient = PayWithPayMaya.newBuilder()
        .clientPublicKey("pk-rpwb5YR6EfnKiMsldZqY4hgpvJjuy8hhxW2bVAAiz2N")
        .environment(PayMayaEnvironment.SANDBOX)
        .logLevel(LogLevel.ERROR)
        .build()
    //private val presenter: CartContract.Presenter = ProfileFragment.PresenterModule.getCartPresenter()

    private fun getReferenceNumber() =
        (++REQUEST_REFERENCE_NUMBER).toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_test)
        button = findViewById(R.id.buttonTest)
        val totalAmount = intent.getStringExtra("totalAmount")
        val addressid = intent.getStringExtra("addressid")
        val addressline = intent.getStringExtra("addressline")
        val amount = 500.00
        val metadata = JSONObject("{\"pf\":{\"smn\":\"Ayos!\",\"smi\":\"EFS100001033\",\"mci\":\"pasay\",\"mpc\":\"608\",\"mco\":\"PHL\"}}")

        val redirectUrl= RedirectUrl(
            success = "http://success.com",
            failure = "http://failure.com",
            cancel = "http://cancel.com"
        )

        val samplepayment =
            SinglePaymentRequest(
                TotalAmount(amount.toBigDecimal(),
                    "PHP",
                    AmountDetails()
                ),
                getReferenceNumber(),
                redirectUrl,
                metadata
            )

        button.setOnClickListener {
            payWithPayMayaClient.startSinglePaymentActivityForResult(this, samplepayment)
            Log.d("Maya","payWithPayMayaClient Starting")
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        button = findViewById(R.id.buttonTest)
        val totalAmount = intent.getStringExtra("totalAmount")
        val addressid = intent.getStringExtra("addressid")
        val addressline = intent.getStringExtra("addressline")
        val amount = 500.00
        val metadata = JSONObject("{\"pf\":{\"smn\":\"Ayos!\",\"smi\":\"EFS100001033\",\"mci\":\"pasay\",\"mpc\":\"608\",\"mco\":\"PHL\"}}")

        val redirectUrl= RedirectUrl(
            success = "http://success.com",
            failure = "http://failure.com",
            cancel = "http://cancel.com"
        )

        val samplepayment =
            SinglePaymentRequest(
                TotalAmount(amount.toBigDecimal(),
                    "PHP",
                    AmountDetails()
                ),
                getReferenceNumber(),
                redirectUrl,
                metadata
            )
        payWithPayMayaClient.startSinglePaymentActivityForResult(this, samplepayment)
        return super.onCreateView(name, context, attrs)
    }

   /* val client = OkHttpClient()

    val mediaType = "application/json".toMediaTypeOrNull()
    val body = RequestBody.create(mediaType, "{\"totalAmount\":{\"currency\":\"PHP\",\"value\":\"100\"},\"redirectUrl\":{\"success\":\"https://www.merchantsite.com/success?id=567834590\",\"failure\":\"https://www.merchantsite.com/failure?id=567834590\",\"cancel\":\"https://www.merchantsite.com/cancel?id=567834590\"},\"metadata\":{\"pf\":{\"smn\":\"ayos\",\"smi\":\"1\",\"mci\":\"pasay\",\"mpc\":\"608\",\"mco\":\"PHL\"}},\"requestReferenceNumber\":\"5b4a6d60-2165-4bc1-bb0e-e610d1a3f82d\"}")
    val request = Request.Builder()
        .url("https://pg-sandbox.paymaya.com/payby/v2/paymaya/payments")
        .post(body)
        .addHeader("accept", "application/json")
        .addHeader("content-type", "application/json")
        .addHeader("authorization", "Basic cGstcnB3YjVZUjZFZm5LaU1zbGRacVk0aGdwdkpqdXk4aGh4VzJiVkFBaXoyTjpzay02czlkd25ZR0ZKZFpPWXUxSENVQWZVWmN0V0VmOUFqdEhJRzM4a2V6WDhX")
        .build()

    private val response = client.newCall(request).execute()
   // Log.d("maya",response.body.toString())
    val uri = Uri.parse(response.body.toString())
   // Log.d("maya", uri)
*/
//    internal object PresenterModule {
//        fun getCartPresenter(): CartContract.Presenter =
//            CartPresenter(
//                RepositoryModule.cartRepository
//            )
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Maya", "requestcode:$requestCode")
        Log.d("Maya", "$resultCode")
        data?.extras?.keySet()?.forEach { key ->
            Log.d("Maya", "Extra Key: $key, Value: ${data.extras?.get(key)}")
        }
        if (resultCode == Activity.RESULT_OK) {
            // Single payment completed successfully
            val paymentId = data?.getStringExtra("EXTRAS_RESULT_ID")
            paymentSuccessDialog()
            if (paymentId != null) {
                // Handle the single payment result
                Log.d("Maya", "Single Payment Successful. Payment ID: $paymentId")
            } else {
                // Handle parsing error or null result
                Log.d("Maya", "Error parsing single payment result or result is null")
            }
        }else if (resultCode == 1063) {
            val message = "Maya services are currently down. Please try again in a few minutes."
            paymentFailedDialog(message)
            Log.d("Maya", "Error: Payment failed with result code 1063")
            // Add additional error handling logic as needed
        }else if (resultCode == 0) {
            val message = "Payment was cancelled."
            paymentFailedDialog(message)
            Log.d("Maya", "Payment Cancelled")
            // Add additional error handling logic as needed
        }else {
            val message = "Something else happened. Please Try Again. Result Code: $resultCode"
            paymentFailedDialog(message)
            Log.d("Maya", "Payment Failed or Canceled. resultCode:$resultCode")
        }

    }

    private fun paymentSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ayos na ba?")
        builder.setMessage("Are you sure you want to confirm that this booking done?")
        builder.setNegativeButton("OK") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun paymentFailedDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment Failed")
        builder.setMessage("Something happened: $message")
        builder.setNegativeButton("OK") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val dialog = builder.create()
        dialog.show()
    }
}