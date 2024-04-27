package com.smartplugin.android.ui.place

import MqttMessageViewModel
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.smartplugin.android.SunnyWeatherApplication
import com.smartplugin.android.databinding.FragmentMainBinding
import com.smartplugin.android.logic.model.MqttStatusResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class MqttFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private var isResetSwtichExecuted = false

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var job: Job
    private var timeLeft: Int = 0
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


        //plugin power
        binding.interactFragmentInclude.pluginToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The switch is checked.
                Log.d(
                    SunnyWeatherApplication.TAG,
                    "${binding.interactFragmentInclude.pluginToggle.text}  uploadMqttMessage 0"
                )
                viewModel.uploadMqttMessage("on", "1");
            } else {
                // The switch isn't checked.
                Log.d(
                    SunnyWeatherApplication.TAG,
                    "${binding.interactFragmentInclude.pluginToggle.text}  uploadMqttMessage 1"
                )
                viewModel.uploadMqttMessage("on", "0");
                if (binding.interactFragmentInclude.timerToggle.isChecked) {
                    binding.interactFragmentInclude.timerToggle.toggle()
                }
            }
        }
        //timer
        binding.interactFragmentInclude.timerInput.visibility = View.GONE
        binding.interactFragmentInclude.timerToggle.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                // The switch is checked
//                Snackbar.make(this,"timerToggle checked!",Snackbar.LENGTH_SHORT)

                binding.interactFragmentInclude.timerInput.visibility = View.VISIBLE
                Log.d(
                    SunnyWeatherApplication.TAG,
                    "${binding.interactFragmentInclude.pluginToggle.text}  uploadMqttMessage "
                )
//                    viewModel.uploadMqttMessage("countdown","1");
                viewModel.uploadMqttMessage("countdown", "0");

            } else {
                // The switch isn't checked.
                binding.interactFragmentInclude.timerInput.visibility = View.GONE
                Log.d(
                    SunnyWeatherApplication.TAG,
                    "${binding.interactFragmentInclude.pluginToggle.text}  uploadMqttMessage "
                )
                viewModel.uploadMqttMessage("countdown", "0");
            }


        }
        //timerStartButton
        binding.interactFragmentInclude.countdownStartButton.setOnClickListener() {

            viewModel.uploadMqttMessage(
                "countdown",
                binding.interactFragmentInclude.inputMinutes.text.toString()
            );
            Snackbar.make(
                binding.interactFragmentInclude.inputMinutes,
                "倒计时已启用，时长:" +
                        "${binding.interactFragmentInclude.inputMinutes.text}分钟",
                Snackbar.LENGTH_SHORT
            ).show();
            startTimer(
                1000 * 60 * binding.interactFragmentInclude.inputMinutes.text.toString().toLong()
            )
            timeLeft = 60 * binding.interactFragmentInclude.inputMinutes.text.toString().toInt()


        }
        //timerToggle
        binding.timerFragmentLayoutInclude.timerToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.uploadMqttMessage("timer","1")
                viewModel.uploadMqttMessage("starttime",
                    binding.timerFragmentLayoutInclude.startTimerTextview.text.toString())
                viewModel.uploadMqttMessage("endtime",
                    binding.timerFragmentLayoutInclude.stopTimerTextview.text.toString())
            }else{
                viewModel.uploadMqttMessage("timer","0")
            }
        }
        //timer button
        //starttime
        binding.timerFragmentLayoutInclude.timerStartButton.setOnClickListener{
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Appointment time")
                    .setInputMode(INPUT_MODE_CLOCK)
                    .build()
            parentFragmentManager?.let { it1 -> picker.show(it1, "tag") };
            picker.addOnPositiveButtonClickListener {
                // call back code
                val formattedTime = String.format("%02d:%02d", picker.hour, picker.minute)
                binding.timerFragmentLayoutInclude.startTimerTextview.text = formattedTime
            }

        }
        //endtime
        binding.timerFragmentLayoutInclude.timerStopButton.setOnClickListener{
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Appointment time")
                    .setInputMode(INPUT_MODE_CLOCK)
                    .build()
            parentFragmentManager?.let { it1 -> picker.show(it1, "tag") };
            picker.addOnPositiveButtonClickListener {
                // call back code
                val formattedTime = String.format("%02d:%02d", picker.hour, picker.minute)
                binding.timerFragmentLayoutInclude.stopTimerTextview.text = formattedTime
            }

        }

        //观察mqttLiveData，一旦出现变化，执行下面的代码
        viewModel.mqttLiveData.observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                val mqttResponse = result.getOrNull()
                Log.d(SunnyWeatherApplication.TAG, "viewModel.mqttLiveData  $result  ")
                //backGroudImageView.visibility = View.GONE
                if (mqttResponse != null) {
                    viewModel.mqttStatusResponse = mqttResponse as MqttStatusResponse
                    Log.d(
                        SunnyWeatherApplication.TAG,
                        " viewModel.mqttStatusResponse  $mqttResponse  "
                    )
                    Log.d(
                        SunnyWeatherApplication.TAG,
                        "mqttResponse.state $mqttResponse.state"
                    )
                    if (mqttResponse.state == "1") {
                        binding.pluginStatusFragmentInclude.statusText.text = "Online"
                    } else {
                        binding.pluginStatusFragmentInclude.statusText.text = "Offline"
                    }

                    Log.d(SunnyWeatherApplication.TAG, "start time ")
                    binding.pluginStatusFragmentInclude.powerText.text = mqttResponse.power
                }

            } else {
                Toast.makeText(activity, "未能查询到", Toast.LENGTH_SHORT)
                    .show()

            }
        })


        viewModel.requestMqttLiveData.observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                val mqttResponse = result.getOrNull()
                if (mqttResponse != null) {
                    Log.d(SunnyWeatherApplication.TAG, "viewModel.mqttLiveData  $result  ")
                    viewModel.mqttStatusResponse = mqttResponse as MqttStatusResponse
                    Log.d(
                        SunnyWeatherApplication.TAG,
                        " viewModel.mqttStatusResponse  $mqttResponse  "
                    )
                    binding.pluginStatusFragmentInclude.statusText.text =
                        if (mqttResponse.state.equals("1")) "Online" else "Offline"
                    // timer
                    binding.pluginStatusFragmentInclude.powerText.text = mqttResponse.power

                    if (!isResetSwtichExecuted) {
                        if (binding.interactFragmentInclude.pluginToggle.isChecked) {//switch is checked
                            Toast.makeText(
                                activity,
                                "isChecked:${binding.interactFragmentInclude.pluginToggle.isChecked}," +
                                        "mqttResponse.on :${mqttResponse.on}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            if (mqttResponse.on.toString().equals("0")) {
                                binding.interactFragmentInclude.pluginToggle.toggle()
                            }
                        } else {

                            if (mqttResponse.on.toString().equals("1")) {
                                binding.interactFragmentInclude.pluginToggle.toggle()
                            }
                        }
                        binding.timerFragmentLayoutInclude.startTimerTextview.text =
                            mqttResponse.starttime
                        Log.d(
                            SunnyWeatherApplication.TAG,
                            "mqttResponse.starttime ${mqttResponse.starttime} "
                        )
                        binding.timerFragmentLayoutInclude.stopTimerTextview.text = mqttResponse.endtime
                        Log.d(
                            SunnyWeatherApplication.TAG,
                            "  mqttResponse.endtime ${mqttResponse.endtime} "
                        )
                    }
                    isResetSwtichExecuted = true

                }

            } else {
                Toast.makeText(activity, "未能查询到", Toast.LENGTH_SHORT)
                    .show()

            }
        })
        job = scope.launch {
            while (isActive) {
                viewModel.requestData()

                // 暂停协程一段时间，实现定时刷新
                // 这里的 1000L 是刷新的间隔，单位是毫秒
                delay(3000L)
            }
        }

    }

    private fun startTimer(duration: Long) {

        val timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 显示剩余时间
                Snackbar.make(
                    binding.interactFragmentInclude.inputMinutes,
                    "倒计时剩余时间:" +
                            "${timeLeft--} s",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onFinish() {
                // 定时器结束，执行相应操作
                if (binding.interactFragmentInclude.timerToggle.isChecked) {
                    binding.interactFragmentInclude.timerToggle.toggle()
                }
            }
        }
        timer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}