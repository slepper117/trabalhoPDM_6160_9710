package ipca.pdm.a6160a9710.ui.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ipca.pdm.a6160a9710.R
import ipca.pdm.a6160a9710.databinding.FragmentBookingsBinding
import ipca.pdm.a6160a9710.ui.parseDateFull

class BookingsFragment : Fragment() {

    var bookings = arrayListOf<Booking>()
    private val adapter = BookingsAdapter()

    private var _binding: FragmentBookingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = activity?.baseContext

        if (context != null) {
            BookingsBackend.fetchBookings(context, lifecycleScope) {
                bookings = it
                adapter.notifyDataSetChanged()
            }
        }

        binding.bookingsBtnAdd.setOnClickListener {
            findNavController().navigate(
                R.id.action_nav_bookings_to_bookingAddFragment
            )
        }

        binding.bookingsList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class BookingsAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return bookings.size
        }

        override fun getItem(i: Int): Any {
            return bookings[i]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
            val rowBooking = layoutInflater.inflate(R.layout.row_booking, viewGroup, false)
            val userName = rowBooking.findViewById<TextView>(R.id.rwBookingUserName)
            val description = rowBooking.findViewById<TextView>(R.id.rwBookingDescription)
            val startDate = rowBooking.findViewById<TextView>(R.id.rwBookingStartDate)

            val booking = bookings[i]
            userName.text = booking.userName
            description.text = booking.description
            startDate.text = booking.startDate?.parseDateFull()

            rowBooking.setOnClickListener {
                findNavController().navigate(
                    R.id.action_nav_bookings_to_bookingDetailsFragment,
                    Bundle().apply {
                        putInt(BookingDetailsFragment.BOOKING_ID, booking.id!!)
                    }
                )
            }

            return rowBooking
        }
    }
}