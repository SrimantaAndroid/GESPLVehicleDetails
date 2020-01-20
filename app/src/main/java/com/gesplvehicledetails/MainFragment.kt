package com.gesplvehicledetails


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.gespl.bgv.utils.Alert
import com.gespl.bgv.utils.SheardParam
import com.gespl.bgv.utils.SheardPreference


class MainFragment : Fragment() {

    var spcode:TextView?=null
    var sp_vesicaltype:Spinner?=null
    val type= arrayOf("Select Type","INBOUND","OUTBOUND")
    var vectype:String=""
    var vehicalto:EditText?=null
    var vehicalform:EditText?=null
    var btn_next:Button?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      val view:View=LayoutInflater.from(activity!!).inflate(R.layout.fragment_main,null)
        viewbinds(view)
        spcode!!.setText("Sp-Code: "+ SheardPreference.getSomeStringValue(activity!!,SheardParam.loginuserstation!!))
        val adapter = ArrayAdapter(activity!!, R.layout.liginid_layout, type)
        sp_vesicaltype!!.adapter = adapter
        sp_vesicaltype!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                vectype=type[position]
                SheardPreference.setSomeStringValue(activity!!,SheardParam.vechitype!!,type[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                vectype=""
            }
        }

        btn_next!!.setOnClickListener {
            if (!vectype.equals("")){
                SheardPreference.setSomeStringValue(activity!!,SheardParam.vacti_trns_type,vectype)
                if (!vehicalform!!.text.toString().equals("")){
                    SheardPreference.setSomeStringValue(activity!!,SheardParam.trns_form,vehicalform!!.text.toString())
                    if (!vehicalto!!.text.toString().equals("")){
                        SheardPreference.setSomeStringValue(activity!!,SheardParam.trns_to,vehicalto!!.text.toString())

                       if (vectype.equals("INBOUND")) {
                           val action = MainFragmentDirections.actionFragmentMainToInbound()
                           findNavController().navigate(action)
                       }else{
                           val action1=MainFragmentDirections.actionFragmentMainToOutbound()
                           findNavController().navigate(action1)
                       }
                    }else
                        Alert.showalert(activity!!,"Please Select Transport To.")

                }else
                    Alert.showalert(activity!!,"Please Select Transport From.")

            }else
                Alert.showalert(activity!!,"Please Select Transport type.")

        }
        return view

    }

    private fun viewbinds(view: View) {
         spcode=view.findViewById(R.id.spcode)
         sp_vesicaltype=view.findViewById(R.id.sp_vesicaltype)
         vehicalto=view.findViewById(R.id.vehicalto)
         vehicalform=view.findViewById(R.id.vehicalform)
         btn_next=view.findViewById(R.id.btn_next)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


}
