package com.gesplvehicledetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.gespl.bgv.utils.Alert
import com.gespl.bgv.utils.AppConstant
import com.gespl.bgv.utils.SheardParam
import com.gespl.bgv.utils.SheardPreference

class LogInActivity : AppCompatActivity() {

    val station= arrayOf("Select Sp Code","KGPA",
            "MDNA",
            "MDNB",
            "MDNC",
            "MIDD",
            "MIDE",
            "MIDG")
    var sp_station:Spinner?=null
    var btnsubmit:Button?=null
    var et_name:EditText?=null
    var select_name:String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sp_station=findViewById(R.id.sp_station)
        btnsubmit=findViewById(R.id.btnsubmit)
        et_name=findViewById(R.id.et_name)
        val adapter = ArrayAdapter(this, R.layout.liginid_layout, station)
        sp_station!!.adapter = adapter

        sp_station!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                select_name = station[position]
                SheardPreference.setSomeStringValue(this@LogInActivity,SheardParam.loginuserstation!!,select_name!!)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                select_name=""
            }
        }

        btnsubmit!!.setOnClickListener {
            if (!et_name!!.text.toString().equals("")) {

                if (!select_name.equals("")){

                    SheardPreference.setSomeStringValue(this@LogInActivity, SheardParam.loginusername!!, et_name!!.text.toString())
                    SheardPreference.setSomeStringValue(this@LogInActivity, SheardParam.loginuserstation!!, select_name!!)
                    SheardPreference.setSomeStringValue(this@LogInActivity, SheardParam.ISlogin!!, true)

                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }else{
                Alert.showalert(this,"Please Enter Name")
            }
        }
    }
}
