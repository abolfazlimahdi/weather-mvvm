package abolfazli.mahdi.weather.weather.ui

import abolfazli.mahdi.weather.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_search.view.*
import java.util.*


class SearchAdapter(var cities: List<String>, val onClick: (String) ->Unit) : RecyclerView.Adapter<SearchAdapter.SearchVH>(),
    Filterable {

    var filteredCities: List<String> = cities
    private lateinit var valueFilter: ValueFilter

    override fun getFilter(): Filter {
        if (!::valueFilter.isInitialized){
            valueFilter = ValueFilter()
        }
        return valueFilter
    }

    override fun getItemCount(): Int {
        return cities.size
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): SearchVH {
        return SearchVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {

        holder.bind(cities[position])
    }


    inner class SearchVH(view: View) : RecyclerView.ViewHolder(view) {

        val cityNameTV = view.tv_cityName

        fun bind(city:String) {
            cityNameTV.text = city

            itemView.setOnClickListener {
                onClick.invoke(city)
            }
        }


    }


    private inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            val results = Filter.FilterResults()

            if (constraint != null && constraint.isNotEmpty()) {
                val filterList = ArrayList<String>()
                for (i in filteredCities.indices) {
                    if (filteredCities[i].toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filterList.add(filteredCities[i])
                    }
                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = filteredCities.size
                results.values = filteredCities
            }
            return results

        }

        override fun publishResults(
            constraint: CharSequence,
            results: Filter.FilterResults
        ) {
            cities = results.values as List<String>
            notifyDataSetChanged()
        }

    }

}
