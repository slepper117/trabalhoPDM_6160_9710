package ipca.pdm.a6160a9710.ui.clocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ipca.pdm.a6160a9710.R
import ipca.pdm.a6160a9710.databinding.FragmentClocksBinding
import ipca.pdm.a6160a9710.ui.parseDateFull

class ClocksFragment : Fragment() {

    var clocks = arrayListOf<Clock>()
    private val adapter = ClockAdapter()

    private var _binding: FragmentClocksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClocksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = activity?.baseContext

        if (context != null) {
            ClocksBackend.fetchClocks(context, lifecycleScope) {
                clocks = it
                adapter.notifyDataSetChanged()
            }
        }

        binding.clocksList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class ClockAdapter : BaseAdapter() {
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
}