package ipca.pdm.a6160a9710.ui.bookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ipca.pdm.a6160a9710.R
import ipca.pdm.a6160a9710.databinding.FragmentBookingDetailsBinding
import ipca.pdm.a6160a9710.ui.parseDateFull

class BookingDetailsFragment : Fragment() {

    var bookingID: Int? = null

    private var _binding: FragmentBookingDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookingID = it.getInt(BOOKING_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = activity?.baseContext

        if (context != null) {
            BookingsBackend.getBooking(context, lifecycleScope, bookingID!!) {
                binding.BookingDetailsDescription.text = it.description
                binding.BookingDetailsStartDate.text = it.startDate!!.parseDateFull()
                binding.BookingDetailsFinalDate.text = it.finalDate!!.parseDateFull()
                binding.BookingDetailsRoom.text = it.roomName
                binding.BookingDetailsUser.text = it.userName
                binding.BookingDetailsValidated.text = if (it.validated == true) "Está validado" else "Não está validado"
            }
        }

        binding.BookingDetailsBtnUpdate.setOnClickListener {
            findNavController().navigate(
                R.id.action_bookingDetailsFragment_to_bookingUpdateFragment,
                Bundle().apply {
                    putInt(BookingDetailsFragment.BOOKING_ID, bookingID!!)
                }
            )
        }

        binding.BookingDetailsBtnDelete.setOnClickListener {
            if (context != null) {
                BookingsBackend.deleteBooking(context, lifecycleScope, bookingID!!) {
                    if (it) parentFragmentManager.popBackStack()
                    else Toast.makeText(context, "Destruição Falhada", Toast.LENGTH_SHORT).show()
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