package com.smartplugin.android.ui.place;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.smartplugin.android.databinding.FragmentMainBinding
import com.smartplugin.android.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(){
    private var _binding: FragmentProfileBinding ? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root


    }
}
