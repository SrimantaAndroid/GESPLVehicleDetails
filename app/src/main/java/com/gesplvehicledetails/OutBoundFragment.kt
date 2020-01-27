package com.gesplvehicledetails


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.gespl.bgv.utils.Alert
import com.gespl.bgv.utils.AppConstant
import com.gespl.bgv.utils.SheardParam
import com.gespl.bgv.utils.SheardPreference
import kotlinx.android.synthetic.main.fragment_in_bound.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*


class OutBoundFragment : Fragment() {
    val type= arrayOf("Select CTP missed Type","YES","NO")
    var btn_next:Button?=null
    var btnnew:Button?=null
    var et_ctpmissreason:EditText?=null
    var et_caseid:EditText?=null
    var sp_cptmissed:Spinner?=null
    var et_ctppercentage:EditText?=null
    var et_parttime:EditText?=null
    var et_actualarrivaltime:EditText?=null
    var et_no_of_fluid:EditText?=null
    var et_noofbags:EditText?=null
    var et_serialno2:EditText?=null
    var et_serialno1:EditText?=null
    var et_Vechcalid:EditText?=null
    var et_v:EditText?=null
    var tv_route:TextView?=null
    var tv_spcode:TextView?=null
    var et_arrvaldate:TextView?=null
    var et_depetdate:TextView?=null
    var rl_sea2lscan:RelativeLayout?=null
    var rl_seal1scan:RelativeLayout?=null


    var serialno:Int=0
    private val PERMISSION_REQUEST_CODE = 200
    var ll_cptmiss:LinearLayout?=null
    var ctpmissed:String=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        var view:View=LayoutInflater.from(activity).inflate(R.layout.fragment_out_bound,null)
        viewbinds(view)
        SheardPreference.setSomeStringValue(activity!!,SheardParam.casetype,"NA")
        SheardPreference.setSomeStringValue(activity!!,SheardParam.reason,"NA")
        val adapter = ArrayAdapter(activity!!, R.layout.liginid_layout, type)
        sp_cptmissed!!.adapter = adapter
        sp_cptmissed!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                ctpmissed=type[position]
                if (ctpmissed.equals("YES")){
                    ll_cptmiss!!.visibility=View.VISIBLE
                }else{
                    ll_cptmiss!!.visibility=View.GONE
                }
                //SheardPreference.setSomeStringValue(activity!!,SheardParam.vechitype!!,type[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                ctpmissed=""
            }
        }

        et_parttime!!.setOnClickListener {
            openparttimepicker()
        }
        et_actualarrivaltime!!.setOnClickListener {
            opentimepicker()
        }

        rl_seal1scan!!.setOnClickListener {
            serialno=1
            hideKeyboard(activity!!)
            checkCameraPermission()

        }
        rl_sea2lscan!!.setOnClickListener {
            serialno=2
            hideKeyboard(activity!!)
            checkCameraPermission()
        }

        btn_next!!.setOnClickListener {
           /* val action=OutBoundFragmentDirections.actionOutboundToDetails()
            findNavController().navigate(action)*/
            checkandsave()

        }
        btnnew!!.setOnClickListener {
            val action1=OutBoundFragmentDirections.actionOutboundToFragmentMain()
            findNavController().navigate(action1)

        }
        et_depetdate!!.setOnClickListener {
            opendatepicker()
        }

        et_arrvaldate!!.setOnClickListener {
            opendatepickerarrival()
        }
        return view
    }
    private fun opendatepicker() {
        val mcurrentdate = Calendar.getInstance()
        val year=mcurrentdate.get(Calendar.YEAR)
        val month =mcurrentdate.get(Calendar.MONTH)
        val day=mcurrentdate.get(Calendar.DAY_OF_MONTH)
        var dpd : DatePickerDialog =  DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener(){
                datePicker: DatePicker?, year: Int, month: Int, day: Int ->

            var c :Calendar= Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH,month)
            c.set(Calendar.DAY_OF_MONTH,day)
            //instead of c.set(Calendar.HOUR, hour);
           // c.set(Calendar.MINUTE, selectedMinute);
            var  myFormat:String = "DD-MMM-YYYY"; // your own format
            var  sdf :SimpleDateFormat=  SimpleDateFormat(myFormat, Locale.US);
            var  formated_time:String = sdf.format(c.getTime());
            et_depetdate!!.setText(formated_time)
           // et_depetdate!!.setText(day.toString()+"-"+(month+1).toString()+"-"+year.toString())

        },year,month,day)
        dpd.show()

    }

    private fun opendatepickerarrival() {
        val mcurrentdate = Calendar.getInstance()
        val year=mcurrentdate.get(Calendar.YEAR)
        val month =mcurrentdate.get(Calendar.MONTH)
        val day=mcurrentdate.get(Calendar.DAY_OF_MONTH)
        var dpd : DatePickerDialog =  DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener(){
                datePicker: DatePicker?, year: Int, month: Int, day: Int ->

            var c :Calendar= Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH,month)
            c.set(Calendar.DAY_OF_MONTH,day)
            //instead of c.set(Calendar.HOUR, hour);
            // c.set(Calendar.MINUTE, selectedMinute);
            var  myFormat:String = "DD-MMM-YYYY"; // your own format
            var  sdf :SimpleDateFormat=  SimpleDateFormat(myFormat, Locale.US);
            var  formated_time:String = sdf.format(c.getTime());
            et_arrvaldate!!.setText(formated_time)
          //  et_arrvaldate!!.setText(day.toString()+"-"+(month+1).toString()+"-"+year.toString())

        },year,month,day)
        dpd.show()

    }
    private fun checkandsave() {
        if(!et_v!!.text.toString().equals("")){
            SheardPreference.setSomeStringValue(activity!!,SheardParam.vehicalno,et_v!!.text.toString())
            if(!et_Vechcalid!!.text.toString().equals("")){
                SheardPreference.setSomeStringValue(activity!!,SheardParam.Vid,et_Vechcalid!!.text.toString())
                if(!et_serialno1!!.text.toString().equals("")){
                    SheardPreference.setSomeStringValue(activity!!,SheardParam.Serial1,et_serialno1!!.text.toString())
                    if(!et_serialno2!!.text.toString().equals("")){
                        SheardPreference.setSomeStringValue(activity!!,SheardParam.Serial2,et_serialno2!!.text.toString())
                        if (!et_serialno1!!.text.toString().equals(et_serialno2!!.text.toString())) {
                        if(!et_noofbags!!.text.toString().equals("")){
                            SheardPreference.setSomeStringValue(activity!!,SheardParam.noofbags,et_noofbags!!.text.toString())
                            if(!et_no_of_fluid!!.text.toString().equals("")){
                                SheardPreference.setSomeStringValue(activity!!,SheardParam.nooffluid,et_no_of_fluid!!.text.toString())
                                if (!et_arrvaldate!!.text.toString().equals("")) {
                                    SheardPreference.setSomeStringValue(activity!!, SheardParam.arrivaldate, et_arrvaldate!!.text.toString())

                                    if (!et_actualarrivaltime!!.text.toString().equals("")) {
                                        SheardPreference.setSomeStringValue(activity!!, SheardParam.v_arrivaltime, et_actualarrivaltime!!.text.toString())
                                        if (!et_depetdate!!.text.toString().equals("")) {
                                            SheardPreference.setSomeStringValue(activity!!, SheardParam.deperturedate, et_depetdate!!.text.toString())
                                            if (!et_parttime!!.text.toString().equals("")) {
                                                SheardPreference.setSomeStringValue(
                                                    activity!!,
                                                    SheardParam.vdeparttime,
                                                    et_parttime!!.text.toString()
                                                )
                                                if (!et_ctppercentage!!.text.toString().equals("")) {
                                                    SheardPreference.setSomeStringValue(
                                                        activity!!,
                                                        SheardParam.percentage,
                                                        et_ctppercentage!!.text.toString()
                                                    )
                                                    if (ll_cptmiss!!.visibility == View.VISIBLE) {
                                                        if (!et_caseid!!.text.toString().equals("")) {
                                                            SheardPreference.setSomeStringValue(
                                                                activity!!,
                                                                SheardParam.casetype,
                                                                et_caseid!!.text.toString()
                                                            )
                                                            if (!et_ctpmissreason!!.text.toString().equals(
                                                                    ""
                                                                )
                                                            ) {
                                                                SheardPreference.setSomeStringValue(
                                                                    activity!!,
                                                                    SheardParam.reason,
                                                                    et_ctpmissreason!!.text.toString()
                                                                )

                                                                val action =
                                                                    OutBoundFragmentDirections.actionOutboundToDetails()
                                                                findNavController().navigate(action)

                                                            } else {
                                                                Alert.showalert(
                                                                    activity!!,
                                                                    "Please Enter Reason."
                                                                )
                                                            }
                                                        } else {
                                                            Alert.showalert(
                                                                activity!!,
                                                                "Please Enter Case Id."
                                                            )
                                                        }
                                                    } else {
                                                        val action =
                                                            OutBoundFragmentDirections.actionOutboundToDetails()
                                                        findNavController().navigate(action)
                                                        //Alert.showalert(activity!!,"Please Enter CTP Missed Type.")
                                                    }
                                                } else {
                                                    Alert.showalert(
                                                        activity!!,
                                                        "Please Enter CTP percentage."
                                                    )
                                                }
                                            } else {
                                                Alert.showalert(
                                                    activity!!,
                                                    "Please Enter Vehicle actual Departure time."
                                                )
                                            }
                                        }
                                            else{
                                            Alert.showalert(activity!!, "Please Enter Vehicle actual Departure Date.")
                                        }
                                    } else
                                        Alert.showalert(activity!!, "Please Enter Vehicle actual arrival time.")
                                }else{
                                    Alert.showalert(activity!!, "Please Enter Vehicle actual arrival Date.")
                                }

                            }else
                                Alert.showalert(activity!!,"Please Enter no of Fluid.")

                        }else
                            Alert.showalert(activity!!,"Please Enter no of bags.")
                        }else
                            Alert.showalert(activity!!,"SEAL no 1 and SEAL no 2 are same")
                    }else
                        Alert.showalert(activity!!,"Please Scan Serial no 2")
                }else
                    Alert.showalert(activity!!,"Please Scan Serial no 1")

            }else
                Alert.showalert(activity!!,"Please Enter VR ID")

        }else
            Alert.showalert(activity!!,"Please Enter Vehicle no")
    }

    private fun viewbinds(view: View) {
        btn_next=view.findViewById(R.id.btn_next)
        btnnew=view.findViewById(R.id.btnnew)
        et_ctpmissreason=view.findViewById(R.id.et_ctpmissreason)
        et_caseid=view.findViewById(R.id.et_caseid)
        sp_cptmissed=view.findViewById(R.id.sp_cptmissed)
        et_ctppercentage=view.findViewById(R.id.et_ctppercentage)
        et_parttime=view.findViewById(R.id.et_parttime)
        et_actualarrivaltime=view.findViewById(R.id.et_actualarrivaltime)
        et_no_of_fluid=view.findViewById(R.id.et_no_of_fluid)
        et_noofbags=view.findViewById(R.id.et_associatnoofbags)
        et_serialno2=view.findViewById(R.id.et_serialno2)
        et_serialno1=view.findViewById(R.id.et_serialno1)
        et_Vechcalid=view.findViewById(R.id.et_Vechcalid)
        et_v=view.findViewById(R.id.et_v)
        et_arrvaldate=view.findViewById(R.id.et_arrvaldate)
        et_depetdate=view.findViewById(R.id.et_depetdate)
        tv_route=view.findViewById(R.id.tv_route)
        tv_spcode=view.findViewById(R.id.tv_spcode)
        ll_cptmiss=view.findViewById(R.id.ll_cptmiss)
        rl_sea2lscan=view!!.findViewById(R.id.rl_sea2lscan)
        rl_seal1scan=view!!.findViewById(R.id.rl_seal1scan)
       /// et_serialno1!!.setShowSoftInputOnFocus(false);
        //et_serialno2!!.setShowSoftInputOnFocus(false)
        et_actualarrivaltime!!.setShowSoftInputOnFocus(false)
        et_parttime!!.setShowSoftInputOnFocus(false)
        et_arrvaldate!!.setShowSoftInputOnFocus(false)
        et_depetdate!!.setShowSoftInputOnFocus(false)

        tv_spcode!!.setText("Sp-Code: "+ SheardPreference.getSomeStringValue(activity!!, SheardParam.loginuserstation!!))
        tv_route!!.setText("Route: "+ SheardPreference.getSomeStringValue(activity!!,
            SheardParam.trns_form)+" > "+ SheardPreference.getSomeStringValue(activity!!,
                SheardParam.trns_to))

    }

    private fun opentimepicker() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(activity,
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
              //  val time = (if (selectedHour > 12) selectedHour % 12 else selectedHour) + ":" + (if (minute < 10) "0$minute" else minute) + " " + if (selectedHour >= 12) "PM" else "AM"

                var c :Calendar= Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, selectedHour);
                //instead of c.set(Calendar.HOUR, hour);
                c.set(Calendar.MINUTE, selectedMinute);
                var  myFormat:String = "hh:mm a"; // your own format
                var  sdf :SimpleDateFormat=  SimpleDateFormat(myFormat, Locale.US);
                var  formated_time:String = sdf.format(c.getTime());
                // et_actualarrivaltime!!.setText("$selectedHour:$selectedMinute")
                et_actualarrivaltime!!.setText(formated_time)
               // et_actualarrivaltime!!.setText("$selectedHour:$selectedMinute"
                //)
            }, hour, minute, false
        )//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val  intent= Intent(activity!!,SimpleScannerActivity::class.java)
                    startActivityForResult(intent!!, AppConstant.scanresult)
                } else {
                    checkCameraPermission()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === AppConstant.scanresult) {
            val message = data!!.getStringExtra(AppConstant.scanstring)
            if (serialno==1)
                et_serialno1!!.setText(message)
            else {
                if (!et_serialno1!!.text.toString().equals(message))
                  et_serialno2!!.setText(message)
                else
                    Alert.showalert(activity!!,"Same Serial no found after Scan")
            }
        }
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
        } else {
            val  intent= Intent(activity!!,SimpleScannerActivity::class.java)
            startActivityForResult(intent!!, AppConstant.scanresult)
        }
    }
    private fun openparttimepicker() {
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
                var  sdf : SimpleDateFormat =  SimpleDateFormat(myFormat, Locale.US);
                var  formated_time:String = sdf.format(c.getTime());
                // et_actualarrivaltime!!.setText("$selectedHour:$selectedMinute")
                et_parttime!!.setText(formated_time)
              //  et_parttime!!.setText("$selectedHour:$selectedMinute"

            }, hour, minute, false
        )//Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
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
