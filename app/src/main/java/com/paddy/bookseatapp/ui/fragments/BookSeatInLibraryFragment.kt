package com.paddy.bookseatapp.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.paddy.bookseatapp.R
import com.paddy.bookseatapp.data.model.LibraryQRScanResult
import com.paddy.bookseatapp.databinding.FragmentBookSeatInLibraryBinding
import com.paddy.bookseatapp.utils.CommonUtils
import com.paddy.bookseatapp.utils.LibraryConstant
import com.paddy.bookseatapp.utils.appendZero
import com.paddy.bookseatapp.utils.enable
import com.paddy.bookseatapp.utils.hide
import com.paddy.bookseatapp.utils.show
import com.paddy.bookseatapp.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookSeatInLibraryFragment : Fragment(R.layout.fragment_book_seat_in_library) {

    enum class ScannerType {
        ENTER, EXIT
    }

    companion object {
        fun newInstance() = BookSeatInLibraryFragment()
    }

    private lateinit var codeScanner: CodeScanner
    private lateinit var mScannerType: ScannerType

    private var mContext: Context? = null
    private var mLayoutViewBinding: FragmentBookSeatInLibraryBinding? = null
    private val bookSeatInLibraryViewModel: BookSeatInLibraryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutViewBinding = FragmentBookSeatInLibraryBinding.bind(view)
        setViewClickListener()
        bindScannerView()
        observerData()
    }

    private fun observerData() {
        lifecycleScope.launchWhenResumed {
            observeViewsItemData()
            observeSessionStatus()
        }
    }

    private suspend fun observeViewsItemData() {

        bookSeatInLibraryViewModel.getUpdatedTime().observe(viewLifecycleOwner) { dataResult ->
            mLayoutViewBinding?.apply {
                if (dataResult.data != null) {
                    val session = dataResult.data as LibraryQRScanResult
                    updateData(session)
                }
            }
        }
    }

    private fun updateData(libraryQRScanResult: LibraryQRScanResult?) {
        mLayoutViewBinding?.apply {
            libraryQRScanResult?.let { session ->

                mContext?.let { ctx ->
                    tvLibraryDataScreenLocationIdValue.text = session.locationId
                    tvLibraryDataScreenLocationDetailValue.text = String.format(ctx.getString(R.string.address_label), session.locationDetails)
                    tvLibraryDataScreenPriceValue.text = String.format(ctx.getString(R.string.price_per_minutes_label), session.pricePerMin.toString())
                    tvLibraryDataScreenStartTimeValue.text = String.format(ctx.getString(R.string.start_time_label), CommonUtils.convertMillisToFormattedTime(session.startTime))
                    tvLibraryDataScreenEndTimeValue.text = String.format(ctx.getString(R.string.end_time_label), CommonUtils.convertMillisToFormattedTime(session.endTime))
                    tvLibraryDataScreenTotalPrice.text = String.format(ctx.getString(R.string.total_price_label), session.totalPrice.toString())
                    tvLibraryDataScreenTimer.show()
                    tvLibraryDataScreenTimer.text = String.format(
                        getString(R.string.duration),
                        session.hour.appendZero(),
                        session.minute.appendZero(),
                        session.seconds.appendZero(),
                    )
                }

            } ?: kotlin.run {
                tvLibraryDataScreenLocationIdValue.text = LibraryConstant.EMPTY
                tvLibraryDataScreenLocationDetailValue.text = LibraryConstant.EMPTY
                tvLibraryDataScreenPriceValue.text = LibraryConstant.EMPTY
                tvLibraryDataScreenStartTimeValue.text = LibraryConstant.EMPTY
                tvLibraryDataScreenEndTimeValue.text = LibraryConstant.EMPTY
                tvLibraryDataScreenTotalPrice.text = LibraryConstant.EMPTY
                tvLibraryDataScreenTimer.hide()
                btnScanQRCode.show()
                btnEndSession.hide()
                btnScanQRCode.enable()
                btnEndSession.enable()
            }
        }

    }

    private suspend fun observeSessionStatus() {
        bookSeatInLibraryViewModel.getLibraryStatus().observe(viewLifecycleOwner) { active ->
            mLayoutViewBinding?.apply {
                if (active) {
                    btnScanQRCode.hide()
                    btnEndSession.show()
                    tvLibraryDataScreenEndTimeValue.hide()
                    tvLibraryDataScreenTotalPrice.hide()
                    tvLibraryDataScreenClearSession.hide()
                } else {
                    btnScanQRCode.show()
                    btnEndSession.hide()
                    tvLibraryDataScreenEndTimeValue.show()
                    tvLibraryDataScreenTotalPrice.show()
                    tvLibraryDataScreenClearSession.show()
                }
            }
        }
    }

    private suspend fun observeClearSession() {
        bookSeatInLibraryViewModel.clearSession().observe(viewLifecycleOwner) { active ->
            mLayoutViewBinding?.apply {
                showInfoView()
                updateData(null)
                tvLibraryDataScreenClearSession.hide()
            }
        }
    }

    private fun setViewClickListener() {
        mLayoutViewBinding?.apply {
            btnScanQRCode.setOnClickListener {
                mScannerType = ScannerType.ENTER
                showScannerView()
                startScanner()
            }

            btnEndSession.setOnClickListener {
                mScannerType = ScannerType.EXIT
                showScannerView()
                startScanner()
            }

            tvLibraryDataScreenClearSession.setOnClickListener {
                lifecycleScope.launch {
                    observeClearSession()
                }
            }

            scannerView.setOnClickListener {
                startScanner()
            }
        }
    }

    private fun showScannerView() {
        mLayoutViewBinding?.apply {
            fmScannerViewContainer.show()
            clInfoMainContainer.hide()
        }
    }

    private fun showInfoView() {
        mLayoutViewBinding?.apply {
            fmScannerViewContainer.hide()
            clInfoMainContainer.show()
        }
    }

    private fun startScanner() {
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    private suspend fun observeSubmitSession(qrResult: String) {
        bookSeatInLibraryViewModel.submitPayload(mContext, qrResult, System.currentTimeMillis()).observe(viewLifecycleOwner) {

            mLayoutViewBinding?.apply {
                if (it.data != null) {
                    btnScanQRCode.show()
                    btnEndSession.hide()
                    mContext?.getString(R.string.submission_success)?.let { it1 -> mContext?.showMessage(it1) }
                } else if (it.error != null) {
                    btnScanQRCode.hide()
                    btnEndSession.show()
                    mContext?.showMessage(it.error)
                }
            }
        }
    }

    private fun bindScannerView() {
        mLayoutViewBinding?.apply {
            scannerView.let {
                codeScanner = CodeScanner(activity as Context, it).apply {
                    // Parameters (default values)
                    camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
                    formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
                    // ex. listOf(BarcodeFormat.QR_CODE)
                    autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
                    scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
                    isAutoFocusEnabled = true // Whether to enable auto focus or not
                    isFlashEnabled = false // Whether to enable flash or not

                    // Callbacks
                    decodeCallback = DecodeCallback {
                        (activity as Activity).runOnUiThread {
                            fmScannerViewContainer.hide()
                            lifecycleScope.launch {
                                if (mScannerType == ScannerType.ENTER) {
                                    showInfoView()
                                    bookSeatInLibraryViewModel.bookSeat(it.text)
                                } else if (mScannerType == ScannerType.EXIT) {
                                    tvLibraryDataScreenClearSession.show()
                                    showInfoView()
                                    observeSubmitSession(it.text)
                                }
                            }
                        }
                    }

                    errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS

                        (activity as Activity).runOnUiThread {
                            Toast.makeText(activity, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        mLayoutViewBinding = null
        mScannerType = ScannerType.ENTER
        super.onDestroyView()
    }
}