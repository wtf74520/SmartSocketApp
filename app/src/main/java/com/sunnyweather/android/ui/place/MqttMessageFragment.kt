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
import kotlinx.android.synthetic.main.user_fragment.searchUserEdit
import kotlinx.android.synthetic.main.user_fragment.usrBgImageView

class MqttMessageFragment : Fragment() {

    val viewModel by lazy { ViewModelProviders.of(this).get(MqttMessageViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        searchUserEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                Log.d(SunnyWeatherApplication.TAG, "viewModel.searchUser(content) ")
                viewModel.uploadMqttMessage("on",content)
            } else {
                //recyclerView.visibility = View.GONE
                usrBgImageView.visibility = View.VISIBLE


            }
        }
        //观察mqttLiveData，一旦出现变化，执行下面的代码
        viewModel.mqttLiveData.observe(this, Observer { result ->
            if (result != null) {
//                Toast.makeText(activity, "查询到了", Toast.LENGTH_SHORT)
//                    .show()
                //activity?.let { Snackbar.make(it.findViewById(android.R.id.content), "查询到了,${viewModel.mqttResponse.toString()}", Snackbar.LENGTH_SHORT).show() }

                usrBgImageView.visibility = View.GONE

                //Toast.makeText(activity, "user: ${result.getOrNull().toString()}", Toast.LENGTH_SHORT)
                //   .show()
            } else {
                Toast.makeText(activity, "未能查询到", Toast.LENGTH_SHORT)
                    .show()

            }
        })
    }

}