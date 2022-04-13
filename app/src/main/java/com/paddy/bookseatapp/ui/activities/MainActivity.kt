package com.paddy.bookseatapp.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.paddy.bookseatapp.R
import com.paddy.bookseatapp.domain.ShowOnGoingNotification
import com.paddy.bookseatapp.ui.fragments.BookSeatInLibraryFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mShowOnGoingNotification: ShowOnGoingNotification

    companion object {
        private var PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permission()
    }

    private fun permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                initFragment()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
            }
        } else {
            initFragment()
        }

    }

    private fun initFragment() {
        addFragment(R.id.mainActivityContainer, BookSeatInLibraryFragment.newInstance())
    }

    private fun addFragment(containerViewId : Int, fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(containerViewId, fragment)
            commitAllowingStateLoss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    initFragment()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mShowOnGoingNotification.cancel()
    }

    override fun onStop() {
        super.onStop()
        mShowOnGoingNotification.show()
    }
}