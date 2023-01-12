package ipca.pdm.a6160a9710.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ipca.pdm.a6160a9710.R
import ipca.pdm.a6160a9710.databinding.FragmentHomeBinding
import ipca.pdm.a6160a9710.ui.areas.Area
import ipca.pdm.a6160a9710.ui.areas.AreasBackend
import ipca.pdm.a6160a9710.ui.bookings.Booking
import ipca.pdm.a6160a9710.ui.bookings.BookingDetailsFragment
import ipca.pdm.a6160a9710.ui.clocks.Clock
import ipca.pdm.a6160a9710.ui.parseDateFull

class HomeFragment : Fragment() {

    var areas = arrayListOf<Area>()
    var clocks = arrayListOf<Clock>()
    var bookings = arrayListOf<Booking>()
    private val adapterAreas = AreasAdapter()
    private val adapterClocks = ClocksAdapter()
    private val adapterBookings = BookingsAdapter()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = activity?.baseContext

        if (context != null) {
            HomeBackend.getUser(context, lifecycleScope) {
                binding.homeName.text = it.name
                binding.homeRole.text = it.role

                if(it.userAreas != null) {
                    areas = it.userAreas!!
                    adapterAreas.notifyDataSetChanged()
                }

                if(it.userClocks != null) {
                    clocks = it.userClocks!!
                    adapterClocks.notifyDataSetChanged()
                }

                if(it.userBookings != null) {
                    bookings = it.userBookings!!
                    adapterBookings.notifyDataSetChanged()
                }
            }
        }

        binding.homeAreasList.adapter = adapterAreas
        binding.homeClocksList.adapter = adapterClocks
        binding.homeBookingsList.adapter = adapterBookings
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class AreasAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return areas.size
        }

        override fun getItem(i: Int): Any {
            return areas[i]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
            val rowArea = layoutInflater.inflate(R.layout.row_area, viewGroup, false)
            val id = rowArea.findViewById<TextView>(R.id.rwAreaId)
            val name = rowArea.findViewById<TextView>(R.id.rwAreaName)

            val area = areas[i]
            id.text = "ID: ${area.id}"
            name.text = area.name

            return rowArea
        }
    }

    inner class ClocksAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return clocks.size
        }

        override fun getItem(i: Int): Any {
            return clocks[i]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
            val rowClock = layoutInflater.inflate(R.layout.row_clock, viewGroup, false)
            val userName = rowClock.findViewById<TextView>(R.id.rwClockUserName)
            val dateTime = rowClock.findViewById<TextView>(R.id.rwClockDateTime)
            val direction = rowClock.findViewById<TextView>(R.id.rwClockDirection)

            val clock = clocks[i]
            userName.text = clock.userName
            dateTime.text = clock.datetime?.parseDateFull()
            direction.text = clock.direction

            return rowClock
        }
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

            return rowBooking
        }
    }
}