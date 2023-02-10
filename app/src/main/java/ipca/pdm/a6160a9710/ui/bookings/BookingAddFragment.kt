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
import ipca.pdm.a6160a9710.ui.parseDateTimeISO
import ipca.pdm.a6160a9710.ui.parseLongToDateString
import org.json.JSONObject

class BookingAddFragment : Fragment() {

    private var _binding: FragmentBookingAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var description: String? = null
        var room: Int? = null
        var startDate: String? = null
        var startTime: String? = null
        var finalDate: String? = null
        var finalTime: String? = null

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
            datePicker.addOnPositiveButtonClickListener {
                startDate = datePicker.selection?.parseLongToDateString()
                binding.bookingAddStartDate.text = startDate
            }
        }

        binding.bookingAddStartTimeBtn.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Selecione a Hora Inicial")
                .build()
            timePicker.show(this.requireActivity().supportFragmentManager, "tag")
            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour.toString()
                val minute = timePicker.minute.toString()
                startTime = "${hour}:${minute}"
                binding.bookingAddStartTime.text = startTime
            }
        }

        binding.bookingAddFinalDateBtn.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecione a Data Final")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(this.requireActivity().supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                finalDate = datePicker.selection?.parseLongToDateString()
                binding.bookingAddFinalDate.text = finalDate
            }
        }

        binding.bookingAddFinalTimeBtn.setOnClickListener {
            val timePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Selecione a Hora Final")
                .build()
            timePicker.show(this.requireActivity().supportFragmentManager, "tag")
            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour.toString()
                val minute = timePicker.minute.toString()
                finalTime = "${hour}:${minute}"
                binding.bookingAddFinalTime.text = finalTime
            }
        }

        binding.bookingAddBtn.setOnClickListener {
            if (description == null || room == null || startDate == null || startTime == null || finalDate == null || finalTime == null) {
                Toast.makeText(context, "Campos Obrigatórios Vazios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateTimeStart = parseDateTimeISO(startDate!!, startTime!!)
            val dateTimeFinal = parseDateTimeISO(finalDate!!, finalTime!!)

            val json = JSONObject()
            json.put("description", description)
            json.put("room", room)
            json.put("start", dateTimeStart)
            json.put("final", dateTimeFinal)

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