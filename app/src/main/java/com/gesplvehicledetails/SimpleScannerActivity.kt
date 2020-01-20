package com.gesplvehicledetails

import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.os.Bundle
import android.R.id.message
import android.content.Intent
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.gespl.bgv.utils.AppConstant.Companion.scanresult
import com.gespl.bgv.utils.AppConstant.Companion.scanstring


class SimpleScannerActivity:AppCompatActivity(),ZXingScannerView.ResultHandler {

    private var mScannerView: ZXingScannerView? = null
     var  view:View?=null

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
       // view=mScannerView
        setContentView(mScannerView)                // Set the scanner view as the content view
    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()           // Stop camera on pause
    }
    override fun handleResult(rawResult: Result?) {
        val intent = Intent()
        intent.putExtra(scanstring,  rawResult!!.toString())
        setResult(scanresult, intent)
        finish()
    }

    override fun onBackPressed() {
        lateinit var navController: NavController
        super.onBackPressed()
        val intent: Intent = Intent(this,MainActivity::class.java)
        intent.flags= (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent)
        finish()
      //  navController = Navigation.findNavController(this, R.id.nav_host)
       // navController.popBackStack()
       // val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host)
       // navHostFragment.po
      /*  val action=MainFragmentDirections.actionFragmentMainToInbound()
        findNavController().navigate(action)*/
       // val action =SimpleScannerActivityDirections.actionScanactivityToInbound()
       // findNavController(mScannerView).navigate(action)
    }


}