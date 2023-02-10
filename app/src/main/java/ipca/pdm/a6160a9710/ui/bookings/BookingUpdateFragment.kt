package ipca.pdm.a6160a9710.ui.bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import ipca.pdm.a6160a9710.databinding.FragmentBookingUpdateBinding
import ipca.pdm.a6160a9710.ui.parseLongToDateString
import org.json.JSONObject

class BookingUpdateFragment : Fragment() {

    private var bookingID: Int? = null

    private var _binding: FragmentBookingUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookingID = it.getInt(BookingDetailsFragment.BOOKING_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingUpdateBinding.inflate(inflater, container, false)
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

        binding.bookingUpdateDescription.editText?.doOnTextChanged { inputText, _, _, _ ->
            description = inputText.toString()
        }

        binding.bookingUpdateRoom.editText?.doOnTextChanged { inputText, _, _, _ ->
            room = inputText.toString().toInt()
        }

        binding.bookingUpdateStartDateBtn.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecione a Data do Inicio")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(this.requireActivity().supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                startDate = datePicker.selection?.parseLongToDateString()
                binding.bookingUpdateStartDate.text = startDate
            }
        }

        binding.bookingUpdateStartTimeBtn.setOnClickListener {
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
                binding.bookingUpdateStartTime.text = startTime
            }
        }

        binding.bookingUpdateFinalDateBtn.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecione a Data Final")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(this.requireActivity().supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener {
                finalDate = datePicker.selection?.parseLongToDateString()
                binding.bookingUpdateFinalDate.text = finalDate
            }
        }

        binding.bookingUpdateFinalTimeBtn.setOnClickListener {
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
                binding.bookingUpdateFinalTime.text = finalTime
            }
        }

        binding.bookingUpdateBtn.setOnClickListener {
            val context = activity?.baseContext

            if (description == null && room == null && startDate == null && startTime == null && finalDate == null && finalTime == null) {
                parentFragmentManager.popBackStack()
            }

            val json = JSONObject()
            if (description != null) json.put("description", description)
            if (room != null) json.put("room", room)

            if (context != null) {
                BookingsBackend.updateBooking(context, lifecycleScope, bookingID!!, json) {
                    if (it) parentFragmentManager.popBackStack()
                    else Toast.makeText(context, "Atualização Falhada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val BOOKING_ID = "1"

        @JvmStatic
        fun newInstance(bookingID: Int) =
            BookingDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(BOOKING_ID, bookingID)
                }
            }
    }
}