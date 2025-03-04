package com.example.lab1

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab1.databinding.FragmentContentProviderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContentProviderFragment : Fragment() {
    private var _binding: FragmentContentProviderBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            loadCalendarEvents()
        } else {
            Toast.makeText(requireContext(), "Calendar permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            loadCalendarEvents()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_CALENDAR)
        }
    }

    private fun loadCalendarEvents() {
        val events = getCalendarEvents()
        if(events.isEmpty()){
            binding.noEventsText.isVisible = true
            binding.recyclerView.isVisible = false
        }
        else{
            binding.noEventsText.isVisible = false
            binding.recyclerView.isVisible = true
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = EventsAdapter(events)
    }

    private fun getCalendarEvents(): List<String> {
        val eventList = mutableListOf<String>()
        val projection = arrayOf(CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART)
        val cursor = requireContext().contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection, null, null,
            "${CalendarContract.Events.DTSTART} ASC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val title = it.getString(0) ?: "No Title"
                val date = Date(it.getLong(1))
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
                eventList.add("$title - $formattedDate")
            }
        }
        return eventList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
