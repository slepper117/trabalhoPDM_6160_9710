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
import ipca.pdm.a6160a9710.R
import ipca.pdm.a6160a9710.databinding.FragmentBookingAddBinding
import ipca.pdm.a6160a9710.databinding.FragmentBookingUpdateBinding
import ipca.pdm.a6160a9710.ui.parseLongToDateString
import org.json.JSONObject

class BookingUpdateFragment : Fragment() {

    var bookingID: Int? = null

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
    ): View? {
        _binding = FragmentBookingUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var description: String? = null
        var room: Int? = null

        binding.bookingUpdateDescription.editText?.doOnTextChanged { inputText, _, _, _ ->
            description = inputText.toString()
        }

        binding.bookingUpdateRoom.editText?.doOnTextChanged { inputText, _, _, _ ->
            room = inputText.toString().toInt()
        }

        binding.bookingUpdateBtn.setOnClickListener {
            val context = activity?.baseContext

            if (description == null && room == null ) {
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