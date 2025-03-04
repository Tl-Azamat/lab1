package com.example.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lab1.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnIntent.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_intentsFragment) }
        binding.btnService.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_foregroundServiceFragment) }
        binding.btnBroadcast.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_broadcastReceiverFragment) }
        binding.btnContent.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_contentProviderFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
