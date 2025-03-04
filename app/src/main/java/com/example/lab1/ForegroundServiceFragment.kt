package com.example.lab1

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.lab1.databinding.FragmentForegroundServiceBinding

class ForegroundServiceFragment : Fragment() {
    private var _binding: FragmentForegroundServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentForegroundServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener { checkAndRequestPermission() }
        binding.btnPause.setOnClickListener { startMusicService("PAUSE") }
        binding.btnStop.setOnClickListener { startMusicService("STOP") }
    }


    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startMusicService("START")
        } else {
            Toast.makeText(requireContext(), "Foreground service permission required!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK)
            } else {
                startMusicService("START")
            }
        } else {
            startMusicService("START")
        }
    }

    private fun startMusicService(action: String) {
        val intent = Intent(requireContext(), MusicService::class.java).apply { this.action = action }
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
