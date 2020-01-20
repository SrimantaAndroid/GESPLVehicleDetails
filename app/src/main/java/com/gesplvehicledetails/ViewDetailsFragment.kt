package com.gesplvehicledetails


import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.gespl.bgv.utils.SheardParam
import com.gespl.bgv.utils.SheardPreference
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.Label
import jxl.write.WritableWorkbook
import jxl.write.WriteException
import jxl.write.biff.RowsExceededException
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ViewDetailsFragment : Fragment() {
    var btnedit: Button?=null
    var btn_send: Button?=null
    var et_ctpmissreason: EditText?=null
    var et_caseid: EditText?=null
    var sp_cptmissed: Spinner?=null
    var et_ctppercentage: EditText?=null
    var et_parttime: EditText?=null
    var et_actualarrivaltime: EditText?=null
    var et_no_of_fluid: EditText?=null
    var et_noofbags: EditText?=null
    var et_serialno2: EditText?=null
    var et_serialno1: EditText?=null
    var et_Vechcalid: EditText?=null
    var et_v: EditText?=null
    var tv_route: TextView?=null
    var tv_spcode: TextView?=null
    var tvbound:TextView?=null
    var et_actualdeprturedate:TextView?=null
    var et_actualarrivaldate:TextView?=null

    val REQUEST_CODE = 100
    var myPath: File?=null
    var exelfile:File?=null

    private val neededPermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View=LayoutInflater.from(activity!!).inflate(R.layout.fragment_view_details,null)
        viewbinds(view)
        setvalue()
        val result: Boolean = checkPermission()
        if (!result){
            checkPermission()
        }
        btnedit!!.setOnClickListener {
            print("click")
            findNavController().popBackStack()
          /*  if(SheardPreference.getSomeStringValue(activity!!,SheardParam.vacti_trns_type).equals("INBOUND")) {
                val action = ViewDetailsFragmentDirections.actionDetailsToInbound()
                findNavController().navigate(action)
            }else{
                val action=ViewDetailsFragmentDirections.actionDetailsToOutbound()
                findNavController().navigate(action)
            }*/
        }

        btn_send!!.setOnClickListener {
            if (!result){
                checkPermission()
            }else {
                createExcelSheet()
                btn_send!!.isEnabled = false
            }

        }
        return view
    }

    private fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            val permissionsNotGranted = ArrayList<String>()

            for (permission in neededPermissions) {
                if (ContextCompat.checkSelfPermission(activity!!, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(permission)
                }
            }
            if (permissionsNotGranted.size> 0) {
                var shouldShowAlert = false
                for (permission in permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)
                }
                if (shouldShowAlert) {
                    showPermissionAlert(permissionsNotGranted.toArray(arrayOfNulls<String>(permissionsNotGranted.size)))
                } else {
                    requestPermissions(permissionsNotGranted.toArray(arrayOfNulls<String>(permissionsNotGranted.size)),REQUEST_CODE)
                }
                return false
            }
        }
        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                for (result in grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(activity, "Permession Denied", Toast.LENGTH_LONG).show()
                        return
                    }
                }


            }
        }
    }
    private fun showPermissionAlert(permissions: Array<String>) {
        val alertBuilder = AlertDialog.Builder(activity!!)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(resources.getString(R.string.app_name))
        alertBuilder.setMessage("Permession")
        alertBuilder.setPositiveButton(android.R.string.yes,
            DialogInterface.OnClickListener { dialog, which -> requestPermissions(permissions,REQUEST_CODE) })
        val alert = alertBuilder.create()
        alert.show()
    }
    private fun createExcelSheet() {
        var   c:Date = Calendar.getInstance().getTime();
        var df:SimpleDateFormat =  SimpleDateFormat("DD-MMMM-yyyy");
        var  formattedDate:String = df.format(c)

       val Fnamexls=  SheardPreference.getSomeStringValue(activity!!,SheardParam.vacti_trns_type)+"_"+SheardPreference.getSomeStringValue(activity!!, SheardParam.loginuserstation!!)+"_" +SheardPreference.getSomeStringValue(activity!!, SheardParam.Vid!!)+"_"+formattedDate+".xls"
      //  val Fnamexls = "excelSheet" + System.currentTimeMillis() + ".xls"
        val sdCard = Environment.getExternalStorageDirectory()
        val directory = File(sdCard.absolutePath + "/Gespl")
        directory.mkdirs()
        val file = File(directory, Fnamexls)
        exelfile = file
        val wbSettings = WorkbookSettings()
        wbSettings.locale = Locale("en", "EN")

        val workbook: WritableWorkbook
        try {

            workbook = Workbook.createWorkbook(file, wbSettings)
            //workbook.createSheet("Report", 0);
            val sheet = workbook.createSheet("First Sheet", 0)
            //  val label = Label(0, 2, "SECOND")
            val label0 = Label(0, 0, "SP_CODE")
            val label1 = Label(0, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.loginuserstation!!))
            val label3 = Label(1, 0, "ROUTE")
            val label4 = Label(1, 1, SheardPreference.getSomeStringValue(activity!!,SheardParam.trns_form)+" > "+SheardPreference.getSomeStringValue(activity!!,SheardParam.trns_to))
            val label5 = Label(2, 0, "VEHICLE_NO")
            val label6 = Label(2, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.vehicalno!!))
            val label7 = Label(3, 0, "VR_ID")
            val label8 = Label(3, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.Vid!!))
            val label9 = Label(4, 0, "SERIAL_NO 1")
            val label10 = Label(4, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.Serial1!!))
            val label11 = Label(5, 0, "SERIAL NO 2")
            val label12 = Label(5, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.Serial2!!))
            val label13 = Label(6, 0, "NO OF BAGS")
            val label14 = Label(6, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.noofbags!!))
            val label15 = Label(7, 0, "NO OF FLUID")
            val label16 = Label(7, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.nooffluid!!))
            val label17 = Label(8, 0, " ARRIVAL TIME")
            val label18 = Label(8, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.v_arrivaltime!!))
            val label19 = Label(9, 0, "DEPARTURE TIME")
            val label20 = Label(9, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.vdeparttime!!))
            val label21 = Label(10, 0, "CTP % Code")
            val label22 = Label(10, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.percentage!!))
            val label23 = Label(11, 0, "CASE ID")
            val label24 = Label(11, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.casetype!!))
            val label25 = Label(12, 0, "REASON")
            val label26 = Label(12, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.reason!!))
            val label27 = Label(13, 0, "Arrival Date")
            val label28 = Label(13, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.arrivaldate!!))
            val label29 = Label(14, 0, "Dep. Date")
            val label30 = Label(14, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.deperturedate))
            /*   val label31 = Label(15, 0, "Mobile 2")
           val label32 = Label(
                 15,
                 1,
                 SheardPreference.getSomeStringValue(activity!!, SheardParam.mob2!!).toString()
             )
             val label33 = Label(16, 0, "DL number")
             val label34 =
                 Label(16, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.dl!!))
             val label35 = Label(17, 0, "DL year of issue")
             val label36 =
                 Label(17, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.dlyear!!))
             val label37 = Label(18, 0, "DL Valid Year")
             val label38 = Label(
                 18,
                 1,
                 SheardPreference.getSomeStringValue(activity!!, SheardParam.dlvalidyear!!)
             )
             val label39 = Label(19, 0, "DL issue state")
             val label40 =
                 Label(19, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.dlstate!!))
             val label41 = Label(20, 0, "PAN")
             val label42 =
                 Label(20, 1, SheardPreference.getSomeStringValue(activity!!, SheardParam.pan!!))
             val label43 = Label(21, 0, "Voter Id")
             val label44 =
                 Label(21, 1, SheardPreference.)getSomeStringValue(activity!!, SheardParam.v_id!!)*/
            try {
                // sheet.addCell(label)
                sheet.addCell(label1)
                sheet.addCell(label0)
                sheet.addCell(label4)
                sheet.addCell(label3)
                sheet.addCell(label5)
                sheet.addCell(label6)
                sheet.addCell(label7)
                sheet.addCell(label8)
                sheet.addCell(label9)
                sheet.addCell(label10)
                sheet.addCell(label11)
                sheet.addCell(label12)
                sheet.addCell(label13)
                sheet.addCell(label14)
                sheet.addCell(label15)
                sheet.addCell(label16)
                sheet.addCell(label17)
                sheet.addCell(label18)
                sheet.addCell(label19)
                sheet.addCell(label20)
                sheet.addCell(label21)
                sheet.addCell(label22)
                sheet.addCell(label23)
                sheet.addCell(label24)
                sheet.addCell(label25)
                sheet.addCell(label26)
                sheet.addCell(label27)
                sheet.addCell(label28)
                // sheet.addCell(label29)
                sheet.addCell(label29)
                sheet.addCell(label30)
                /* sheet.addCell(label31)
                sheet.addCell(label32)
                sheet.addCell(label33)
                sheet.addCell(label34)
                sheet.addCell(label35)
                sheet.addCell(label36)
                sheet.addCell(label37)
                sheet.addCell(label38)
                sheet.addCell(label39)
                sheet.addCell(label40)
                sheet.addCell(label41)
                sheet.addCell(label42)
                sheet.addCell(label43)
                sheet.addCell(label44)*/

            } catch (e: RowsExceededException) {
                e.printStackTrace()
            } catch (e: WriteException) {
                e.printStackTrace()
            }
            workbook.write()
            try {
                workbook.close()
            } catch (e: WriteException) {
                e.printStackTrace()
            }
            //val file = File(file.absolutePath)

             sendemail()

            //createExcel(excelSheet);
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendemail() {
        var   c:Date = Calendar.getInstance().getTime();
        var df:SimpleDateFormat =  SimpleDateFormat("DD-MMMM-yyyy");
        var  formattedDate:String = df.format(c)

        val  mailsuject=  SheardPreference.getSomeStringValue(activity!!,SheardParam.vacti_trns_type)+" || "+SheardPreference.getSomeStringValue(activity!!, SheardParam.loginuserstation!!)+" || " +SheardPreference.getSomeStringValue(activity!!, SheardParam.Vid!!)+"("+SheardPreference.getSomeStringValue(activity!!,SheardParam.loginusername!!)+")"

        val  mailtext="Sp: "+ SheardPreference.getSomeStringValue(activity!!, SheardParam.loginuserstation!!)+"\n"+
                "VR ID: "+SheardPreference.getSomeStringValue(activity!!, SheardParam.Vid!!)  +"\n"+
                "Date: "+formattedDate  +"\n"+
                "Route: "+  SheardPreference.getSomeStringValue(activity!!,SheardParam.vacti_trns_type)+"\n"+
                "Vehicle Route: "+ SheardPreference.getSomeStringValue(activity!!,SheardParam.trns_form)+" > "+SheardPreference.getSomeStringValue(activity!!,SheardParam.trns_to)+"\n"+
                "CTP %: "+ SheardPreference.getSomeStringValue(activity!!,SheardParam.percentage)+"\n"+
                "Case Id: "+ SheardPreference.getSomeStringValue(activity!!,SheardParam.casetype)+"\n"+
                "Reason: "+ SheardPreference.getSomeStringValue(activity!!,SheardParam.reason)


       val i = Intent(Intent.ACTION_SEND)
        // i.type = "image/png"
        i.setType("*/*");
        if(SheardPreference.getSomeStringValue(activity!!,SheardParam.vacti_trns_type).equals("INBOUND")) {
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("avijitgiri@giriexpressservice.com","gespl.ops@giriexpressservice.com"))
        }else{
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("avijitgiri@giriexpressservice.com","gespl.ops@giriexpressservice.com",
                "gopbeher@amazon.com"))
        }
        i.putExtra(Intent.EXTRA_SUBJECT,mailsuject)
        i.putExtra(Intent.EXTRA_TEXT, mailtext)
        //  i.putExtra(Intent.EXTRA_STREAM, photoURI)//pngFile
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        var packageManager:PackageManager=activity!!.packageManager
        var matches: MutableList<ResolveInfo> =packageManager.queryIntentActivities(i,0)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkURI = FileProvider.getUriForFile(activity!!, "com.gesplvehicledetails.provider", exelfile!!)
            // val fileURI = FileProvider.getUriForFile(activity!!, "$packageName.fileprovider", yourFile)
            // intent.setType("application/excel")
            i.putExtra(Intent.EXTRA_STREAM,apkURI)

        } else {
            // intent.setType("application/excel")
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exelfile!!))
        }
        var best: ResolveInfo?=null;
        for (  info : ResolveInfo in matches)
            if (info.activityInfo.packageName.endsWith(".gm") ||
                info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            i.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        startActivityForResult(i,400);

    }
    private fun setvalue() {
        tv_spcode!!.setText("Sp-Code: "+ SheardPreference.getSomeStringValue(activity!!, SheardParam.loginuserstation!!))
        tv_route!!.setText("Route: "+ SheardPreference.getSomeStringValue(activity!!, SheardParam.trns_form)+" > "+ SheardPreference.getSomeStringValue(activity!!, SheardParam.trns_to))
        et_v!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.vehicalno))
        et_Vechcalid!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.Vid))
        et_serialno1!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.Serial1))
        et_serialno2!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.Serial2))
        et_noofbags!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.noofbags))
        et_no_of_fluid!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.nooffluid))
        et_actualarrivaltime!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.v_arrivaltime))
        et_parttime!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.vdeparttime))
        et_ctppercentage!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.percentage))
        et_caseid!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.casetype))
        et_ctpmissreason!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.reason))
        et_actualarrivaldate!!.setText(SheardPreference.getSomeStringValue(activity!!,SheardParam.arrivaldate))
        et_actualdeprturedate!!.setText(SheardPreference.getSomeStringValue(activity!!,SheardParam.deperturedate))

        // et_caseid!!.setText(SheardPreference.getSomeStringValue(activity!!, SheardParam.v_arrivaltime))




    }

    private fun viewbinds(view: View) {
        tvbound=view.findViewById(R.id.tv_inbound)
        tv_spcode=view.findViewById(R.id.tv_spcode)
        tv_route=view.findViewById(R.id.tv_route)
        et_v=view.findViewById(R.id.et_v)
        et_Vechcalid=view.findViewById(R.id.et_Vechcalid)
        et_serialno1=view.findViewById(R.id.et_serialno1)
        et_serialno2=view.findViewById(R.id.et_serialno2)
        et_noofbags=view.findViewById(R.id.et_noofbags)
        et_no_of_fluid=view.findViewById(R.id.et_no_of_fluid)
        et_actualarrivaltime=view.findViewById(R.id.et_actualarrivaltime)
        et_parttime=view.findViewById(R.id.et_parttime)
        et_ctppercentage=view.findViewById(R.id.et_ctppercentage)
        et_caseid=view.findViewById(R.id.et_caseid)
        et_ctpmissreason=view.findViewById(R.id.et_ctpmissreason)
        btn_send=view.findViewById(R.id.btn_send)
        btnedit=view.findViewById(R.id.btnedit)
        et_actualdeprturedate=view.findViewById(R.id.et_actualdeprturedate)
        et_actualarrivaldate=view.findViewById(R.id.et_actualarrivaldate)

        tvbound!!.setText(SheardPreference.getSomeStringValue(activity!!,SheardParam.vacti_trns_type)+" Details")

    }


}
