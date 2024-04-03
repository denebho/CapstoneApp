package com.example.ayosapp.ayosPackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.ayosapp.databinding.FragmentAyosInProgressBinding
import com.paymaya.sdk.android.common.LogLevel
import com.paymaya.sdk.android.common.PayMayaEnvironment
import com.paymaya.sdk.android.common.models.RedirectUrl
import com.paymaya.sdk.android.common.models.TotalAmount
import com.paymaya.sdk.android.paywithpaymaya.PayWithPayMaya
import com.paymaya.sdk.android.paywithpaymaya.models.SinglePaymentRequest
import org.json.JSONObject


class AyosInProgress: Fragment() {
    lateinit var redirectUrl: RedirectUrl
    lateinit var totalAmount: TotalAmount
    lateinit var metadata: JSONObject
    lateinit var binding: FragmentAyosInProgressBinding
    val BUNDLE_PAYMENT_TOTAL_AMOUNT = "total_amount"
    val BUNDLE_PAYMENT_BUYER = "buyer"
    val EXTRAS_BUNDLE_PAYMENT = "payment"

    private val MY_SCAN_REQUEST_CODE = 100
    private val CLIENT_KEY = "pk-sHQWci2P410ppwFQvsi7IQCpHsIjafy74jrhYb8qfxu"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAyosInProgressBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //var singlePaymentResult = SinglePaymentResult()
        val payWithPayMayaClient = PayWithPayMaya.newBuilder()
            .clientPublicKey(CLIENT_KEY)
            .environment(PayMayaEnvironment.SANDBOX)
            .logLevel(LogLevel.ERROR)
            .build()

        val requestReferenceNumber: String = "1234"
        val request = SinglePaymentRequest(
            totalAmount,
            requestReferenceNumber,
            redirectUrl,
            metadata
        )
        RedirectUrl(
            success = "http://success.com",
            failure = "http://failure.com",
            cancel = "http://cancel.com"
        )
        //payMayaCheckoutClient.startSinglePaymentActivityForResult(this, request)
    }
}
