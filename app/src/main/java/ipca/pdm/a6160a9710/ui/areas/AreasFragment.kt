package ipca.pdm.a6160a9710.ui.areas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import ipca.pdm.a6160a9710.R
import ipca.pdm.a6160a9710.databinding.FragmentAreasBinding

class AreasFragment : Fragment() {

    var areas = arrayListOf<Area>()
    private val adapter = AreasAdapter()

    private var _binding: FragmentAreasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAreasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = activity?.baseContext

        if (context != null) {
            AreasBackend.fetchAreas(context, lifecycleScope) {
                areas = it
                adapter.notifyDataSetChanged()
            }
        }

        binding.areasList.adapter = adapter
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
}