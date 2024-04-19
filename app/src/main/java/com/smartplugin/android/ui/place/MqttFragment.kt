package com.smartplugin.android.ui.place

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

import com.smartplugin.android.SunnyWeatherApplication
import com.smartplugin.android.databinding.FragmentMainBinding
import com.smartplugin.android.logic.model.MqttStatusResponse


class MqttFragment : Fragment() {
    private var _binding: FragmentMainBinding ? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    private val viewModel by lazy { ViewModelProviders.of(this)[MqttMessageViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentMainBinding.inflate(layoutInflater)
        val view = binding.root
        return view


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.searchFragmentInclude.searchDebugEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                Log.d(SunnyWeatherApplication.TAG, "viewModel.uploadMqttMessage ")
                viewModel.uploadMqttMessage("on",content)
            } else {
                //recyclerView.visibility = View.GONE
                binding.backGroudImageView?.visibility = View.VISIBLE


            }
        }
        binding.interactFragmentInclude.pluginToggle.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked) {
                // The switch is checked.
                Log.d(SunnyWeatherApplication.TAG, "${binding.interactFragmentInclude.pluginToggle.text}  uploadMqttMessage 0")
                viewModel.uploadMqttMessage("on","1");
            } else {
                // The switch isn't checked.
                Log.d(SunnyWeatherApplication.TAG, "${binding.interactFragmentInclude.pluginToggle.text}  uploadMqttMessage 1")
                viewModel.uploadMqttMessage("on","0");
            }


        }
        //观察mqttLiveData，一旦出现变化，执行下面的代码
        viewModel.mqttLiveData.observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                val mqttResponse = result.getOrNull()
                Log.d(SunnyWeatherApplication.TAG, "viewModel.mqttLiveData  $result  ")
                //backGroudImageView.visibility = View.GONE
                viewModel.mqttStatusResponse = mqttResponse as MqttStatusResponse
                Log.d(SunnyWeatherApplication.TAG, " viewModel.mqttStatusResponse  $mqttResponse  ")
                binding.pluginStatusFragmentInclude.statusText.text = mqttResponse.state.let { "Online" }
                    ?: "Offline"
                binding.pluginStatusFragmentInclude.powerText.text = mqttResponse.power
            } else {
                Toast.makeText(activity, "未能查询到", Toast.LENGTH_SHORT)
                    .show()

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}