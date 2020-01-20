package com.gesplvehicledetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gespl.bgv.utils.SheardParam
import com.gespl.bgv.utils.SheardPreference

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        SheardPreference.init(this)
        android.os.Handler().postDelayed(Runnable {
            if(SheardPreference.getSomeStringValue(this,SheardParam.ISlogin!!,false)==false){
              val intent: Intent = Intent(this,LogInActivity::class.java)
               startActivity(intent)
                finish()
            }else{
                val intent: Intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        },2000)
    }
}
