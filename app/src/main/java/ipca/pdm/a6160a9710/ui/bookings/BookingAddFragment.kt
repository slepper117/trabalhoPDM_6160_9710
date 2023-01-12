package ipca.pdm.a6160a9710.ui.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import ipca.pdm.a6160a9710.databinding.FragmentBookingAddBinding
import ipca.pdm.a6160a9710.ui.parseLongToDateString
import org.json.JSONObject
import java.util.*

class BookingAddFragment : Fragment() {

    private var _binding: FragmentBookingAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var description: String? = null
        var room: Int? = null
        var startDateTime = "2022-12-21T12:00:00.000Z"
        var finalDateTime = "2022-12-21T12:00:00.000Z"

        binding.bookingAddDescription.editText?.doOnTextChanged { inputText, _, _, _ ->
            description = inputText.toString()
        }

        binding.bookingAddRoom.editText?.doOnTextChanged { inputText, _, _, _ ->
            room = inputText.toString().toInt()
        }

        binding.bookingAddStartDateBtn.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecione a Data do Inicio")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(this.requireActivity().supportFragmentManager, "tag")
            binding.bookingAddStartDate.text = datePicker.selection?.parseLongToDateString()
        }

        binding.bookingAddFinalTimeBtn.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Selecione a Hora Inicial")
                    .build()
            picker.show(this.requireActivity().supportFragmentManager, "tag")
            val hour = picker.hour.toString()
            val minute = picker.minute.toString()
            binding.bookingAddStartTime.text = "${hour}:${minute}"        }

        binding.bookingAddFinalDateBtn.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecione a Data Final")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(this.requireActivity().supportFragmentManager, "tag");
            binding.bookingAddFinalDate.text = datePicker.selection?.parseLongToDateString()
        }

        binding.bookingAddFinalTimeBtn.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Selecione a Hora Final")
                    .build()
            picker.show(this.requireActivity().supportFragmentManager, "tag")
            val hour = picker.hour.toString()
            val minute = picker.minute.toString()
            binding.bookingAddFinalTime.text = "${hour}:${minute}"
        }

        binding.bookingAddBtn.setOnClickListener {
            if (description == null || room == null ) {
                Toast.makeText(context, "Campos vazios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val json = JSONObject()
            json.put("description", description)
            json.put("room", room)
            json.put("start", startDateTime)
            json.put("final", finalDateTime)

            val context = activity?.baseContext
            if (context != null) {
                BookingsBackend.postBooking(context, lifecycleScope, json) {
                    if (it) parentFragmentManager.popBackStack()
                    else Toast.makeText(context, "Criação Falhada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}