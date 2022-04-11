package com.paddy.bookseatapp.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.paddy.bookseatapp.R
import com.paddy.bookseatapp.databinding.FragmentBookSeatInLibraryBinding
import com.paddy.bookseatapp.utils.hide
import com.paddy.bookseatapp.utils.show

class BookSeatInLibraryFragment : Fragment(R.layout.fragment_book_seat_in_library) {

    companion object {
        fun newInstance() = BookSeatInLibraryFragment()
    }
    private var mLayoutViewBinding : FragmentBookSeatInLibraryBinding? = null
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLayoutViewBinding = FragmentBookSeatInLibraryBinding.bind(view)
        setViewClickListener()
        bindScannerView()
    }


    private fun setViewClickListener() {
        mLayoutViewBinding?.apply {
            btnScanQRCode.setOnClickListener {
                fmScannerViewContainer.show()
            }

            btnEndSession.setOnClickListener {

            }

            scannerView?.setOnClickListener {
                if (::codeScanner.isInitialized) {
                    codeScanner.startPreview()
                }
            }
        }
    }

    private fun bindScannerView(){
        mLayoutViewBinding?.apply {
            scannerView?.let {
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
                            Log.e("TTT", "Scan result: ${it.text}")
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
        super.onDestroyView()
    }
}