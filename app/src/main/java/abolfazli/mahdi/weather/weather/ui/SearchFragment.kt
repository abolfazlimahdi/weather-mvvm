package abolfazli.mahdi.weather.weather.ui

import abolfazli.mahdi.weather.R
import abolfazli.mahdi.weather.databinding.FragmentSearchBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModel: WeatherViewModel

    lateinit var binding: FragmentSearchBinding

    lateinit var searchAdapter: SearchAdapter
    private val cities = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tv_cancelSearch.setOnClickListener {
            requireActivity().onBackPressed()
        }

        iv_clearSearch.setOnClickListener {
            requireActivity().onBackPressed()
        }

        loadCities()

        searchAdapter = SearchAdapter(cities, onClick = { onCityClicked(it) })
        rv_searchList.layoutManager = LinearLayoutManager(requireContext())

        rv_searchList.adapter = searchAdapter

        sv_searchField.isActivated = true
        sv_searchField.queryHint = "Type your city here"
        sv_searchField.onActionViewExpanded()
        sv_searchField.isIconified = false
        sv_searchField.clearFocus()

        sv_searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAdapter.filter.filter(newText)
                return false
            }

        })
    }


    private fun loadCities() {
        CoroutineScope(Dispatchers.IO).launch {
            val jsonFile: String =
                requireContext().assets.open("city_list.json").bufferedReader().use { it.readText() }
            val array = JSONArray(jsonFile)
            for (i in 0 until array.length()) {
                val j: JSONObject = array.getJSONObject(i)
                cities.add(j.getString("name") + "," + j.getString("country"))
            }
            withContext(Dispatchers.Main) {

            }
        }

    }


    private fun onCityClicked(cityName: String) {
        val bundle = bundleOf("cityName" to cityName)
        findNavController().navigate(R.id.actoin_searchFragment_to_weatherFragment, bundle)

    }

}