package com.gesplvehicledetails


import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gespl.bgv.utils.AppConstant.Companion.scanresult
import android.R.attr.data
import com.gespl.bgv.utils.AppConstant.Companion.scanstring
import android.Manifest.permission
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.app.Activity
import android.app.DatePickerDialog
import android.view.inputmethod.InputMethodManager
import com.gespl.bgv.utils.SheardParam
import com.gespl.bgv.utils.SheardPreference
import android.app.TimePickerDialog
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gespl.bgv.utils.Alert
import kotlinx.android.synthetic.main.fragment_in_bound.*
import java.text.SimpleDateFormat
import java.util.*


class InBoundFragment : Fragment() {
    private val PERMISSION_REQUEST_CODE = 200
    var et_serialno1:EditText?=null
    var et_serialno2:EditText?=null
    var tv_spcode:TextView?=null
    var tv_route:TextView?=null
    var et_actualarrivaltime:EditText?=null
    var et_no_ofbags:EditText?=null
    var serialno:Int=0
    var et_v:EditText?=null
    var et_Vechcalid:EditText?=null
    var btnnew:Button?=null
    var btn_next:Button?=null
    var et_actualarrivaldate:EditText?=null
    var rl_sea2lscan:RelativeLayout?=null
    var rl_seal1scan:RelativeLayout?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=LayoutInflater.from(activity).inflate(R.layout.fragment_in_bound,null)
        viewbinds(view)
        SheardPreference.setSomeStringValue(activity!!,SheardParam.vdeparttime,"NA")
        SheardPreference.setSomeStringValue(activity!!,SheardParam.percentage,"NA")
        SheardPreference.setSomeStringValue(activity!!,SheardParam.casetype,"NA")
        SheardPreference.setSomeStringValue(activity!!,SheardParam.reason,"NA")
        SheardPreference.setSomeStringValue(activity!!,SheardParam.deperturedate,"NA")
        rl_seal1scan!!.setOnClickListener { serialno=1
            hideKeyboard(activity!!)
            checkCameraPermission()
        }
        rl_sea2lscan!!.setOnClickListener {
            serialno=2
            hideKeyboard(activity!!)
            checkCameraPermission()
        }
        et_actualarrivaltime!!.setOnClickListener {
            opentimepicker()
        }
        btn_next!!.setOnClickListener {
            checkblank()
           /* val action=InBoundFragmentDirections.actionInboundToDetails()
            findNavController().navigate(action)*/

        }
        btnnew!!.setOnClickListener {
            val action=InBoundFragmentDirections.actionInboundToFragmentMain()
            findNavController().navigate(action)

        }
        et_actualarrivaldate!!.setOnClickListener {
            opendatepicker()
        }
        return view
    }


    private fun opendatepicker() {
        val mcurrentdate = Calendar.getInstance()
        val year=mcurrentdate.get(Calendar.YEAR)
        val month =mcurrentdate.get(Calendar.MONTH)
        val day=mcurrentdate.get(Calendar.DAY_OF_MONTH)
        var dpd :DatePickerDialog=  DatePickerDialog(activity!!,DatePickerDialog.OnDateSetListener(){
           datePicker: DatePicker?, year: Int, month: Int, day: Int ->
            et_actualarrivaldate!!.setText(day.toString()+"-"+(month+1).toString()+"-"+year.toString())
            
        },year,month,day)
        dpd.show()

    }

    private fun checkblank() {
        if(!et_v!!.text.toString().equals("")){
            SheardPreference.setSomeStringValue(activity!!,SheardParam.vehicalno,et_v!!.text.toString())
            if(!et_Vechcalid!!.text.toString().equals("")){
                SheardPreference.setSomeStringValue(activity!!,SheardParam.Vid,et_Vechcalid!!.text.toString())
                if(!et_serialno1!!.text.toString().equals("")){
                    SheardPreference.setSomeStringValue(activity!!,SheardParam.Serial1,et_serialno1!!.text.toString())
                    if(!et_serialno2!!.text.toString().equals("")){
                        SheardPreference.setSomeStringValue(activity!!,SheardParam.Serial2,et_serialno2!!.text.toString())
                        if(!et_associatnoofbags!!.text.toString().equals("")){
                            SheardPreference.setSomeStringValue(activity!!,SheardParam.noofbags,et_associatnoofbags!!.text.toString())
                            if(!et_no_of_fluid!!.text.toString().equals("")){
                                SheardPreference.setSomeStringValue(activity!!,SheardParam.nooffluid,et_no_of_fluid!!.text.toString())
                                if(!et_actualarrivaldate!!.text.toString().equals("")) {
                                    SheardPreference.setSomeStringValue(activity!!, SheardParam.arrivaldate, et_actualarrivaldate!!.text.toString())
                                    if (!et_actualarrivaltime!!.text.toString().equals("")) {
                                        SheardPreference.setSomeStringValue(activity!!, SheardParam.v_arrivaltime, et_actualarrivaltime!!.text.toString())
                                        val action = InBoundFragmentDirections.actionInboundToDetails()
                                        findNavController().navigate(action)
                                    } else
                                        Alert.showalert(activity!!, "Please Enter Vehicle actual arrival time.")
                                }else

                                Alert.showalert(activity!!, "Please Enter Vehicle actual arrival Date.")

                            }else
                                Alert.showalert(activity!!,"Please Enter no of Fluid.")

                        }else
                            Alert.showalert(activity!!,"Please Enter no of bags.")

                    }else
                        Alert.showalert(activity!!,"Please Scan SEAL no 2")
                }else
                    Alert.showalert(activity!!,"Please Scan SEAL no 1")

            }else
                Alert.showalert(activity!!,"Please Enter VR ID")

        }else
            Alert.showalert(activity!!,"Please Enter Vehicle no")
    }

    private fun opentimepicker() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(activity,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->

               var c :Calendar= Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, selectedHour);
                //instead of c.set(Calendar.HOUR, hour);
                c.set(Calendar.MINUTE, selectedMinute);
                var  myFormat:String = "hh:mm a"; // your own format
                var  sdf :SimpleDateFormat=  SimpleDateFormat(myFormat, Locale.US);
                var  formated_time:String = sdf.format(c.getTime());
               // et_actualarrivaltime!!.setText("$selectedHour:$selectedMinute")
                et_actualarrivaltime!!.setText(formated_time)
            }, hour, minute, false
        )//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    private fun viewbinds(view: View?) {
        et_serialno1=view!!.findViewById(R.id.et_serialno1)
        et_serialno2=view!!.findViewById(R.id.et_serialno2);
        tv_spcode=view!!.findViewById(R.id.tv_spcode)
        tv_route=view!!.findViewById(R.id.tv_route)
        et_actualarrivaltime=view!!.findViewById(R.id.et_actualarrivaltime)
        et_v=view!!.findViewById(R.id.et_v)
        et_no_ofbags=view!!.findViewById(R.id.et_associatnoofbags)
        et_Vechcalid=view!!.findViewById(R.id.et_Vechcalid)
        btnnew=view!!.findViewById(R.id.btnnew)
        btn_next=view!!.findViewById(R.id.btn_next)
        et_actualarrivaldate=view!!.findViewById(R.id.et_actualarrivaldate)
        rl_sea2lscan=view!!.findViewById(R.id.rl_sea2lscan)
        rl_seal1scan=view!!.findViewById(R.id.rl_seal1scan)
      //  et_serialno1!!.setShowSoftInputOnFocus(false);
      //  et_serialno2!!.setShowSoftInputOnFocus(false)
        et_actualarrivaltime!!.setShowSoftInputOnFocus(false)
        et_actualarrivaldate!!.setShowSoftInputOnFocus(false)
        tv_spcode!!.setText("Sp-Code: "+ SheardPreference.getSomeStringValue(activity!!, SheardParam.loginuserstation!!))
        tv_route!!.setText("Route: "+SheardPreference.getSomeStringValue(activity!!,SheardParam.trns_form)+" > "+SheardPreference.getSomeStringValue(activity!!,SheardParam.trns_to))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === scanresult) {
            val message = data!!.getStringExtra(scanstring)
            if (serialno==1)
              et_serialno1!!.setText(message)
            else {
                if (!et_serialno1!!.text.toString().equals(message))
                    et_serialno2!!.setText(message)
                else
                    Alert.showalert(activity!!, "Same Serial no found after Scan")
            }
        }
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
        } else {
            val  intent= Intent(activity!!,SimpleScannerActivity::class.java)
            startActivityForResult(intent!!,scanresult)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val  intent= Intent(activity!!,SimpleScannerActivity::class.java)
                    startActivityForResult(intent!!,scanresult)
                } else {
                    checkCameraPermission()
                }
                return
            }
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm!!.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
