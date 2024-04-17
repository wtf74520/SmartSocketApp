package com.sunnyweather.android.ui.place

import MqttMessageViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication

import kotlinx.android.synthetic.main.fragment_main.backGroudImageView
import kotlinx.android.synthetic.main.debug_edit_text_fragment.searchDebugEdit
import kotlinx.android.synthetic.main.interact.plugin_toggle
class MqttFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(MqttMessageViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        searchDebugEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                Log.d(SunnyWeatherApplication.TAG, "viewModel.uploadMqttMessage ")
                viewModel.uploadMqttMessage("on",content)
            } else {
                //recyclerView.visibility = View.GONE
                backGroudImageView.visibility = View.VISIBLE


            }
        }
        plugin_toggle.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                // The switch is checked.
                Log.d(SunnyWeatherApplication.TAG, "${plugin_toggle.text}  uploadMqttMessage 0")
                viewModel.uploadMqttMessage("on","0");
            } else {
                // The switch isn't checked.
                Log.d(SunnyWeatherApplication.TAG, "${plugin_toggle.text}  uploadMqttMessage 1")
                viewModel.uploadMqttMessage("on","1");
            }


        }
        //观察mqttLiveData，一旦出现变化，执行下面的代码
        viewModel.mqttLiveData.observe(this, Observer { result ->
            if (result != null) {

                Log.d(SunnyWeatherApplication.TAG, "viewModel.mqttLiveData  ${result}  ")
                backGroudImageView.visibility = View.GONE

            } else {
                Toast.makeText(activity, "未能查询到", Toast.LENGTH_SHORT)
                    .show()

            }
        })
    }

}