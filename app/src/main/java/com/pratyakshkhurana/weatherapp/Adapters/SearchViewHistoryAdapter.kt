package com.pratyakshkhurana.weatherapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory
import com.pratyakshkhurana.weatherapp.databinding.RecyclerViewSearchViewHistoryEachItemBinding

class SearchViewHistoryAdapter(
    private var listOfSearchItems: MutableList<SearchViewHistory>,
    private val listen: OnSearchViewHistoryItemClicked,
) :
    RecyclerView.Adapter<SearchViewHistoryAdapter.SearchViewHistoryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchViewHistoryViewHolder {
        val viewBinding =
            RecyclerViewSearchViewHistoryEachItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return SearchViewHistoryViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return listOfSearchItems.size
    }

    override fun onBindViewHolder(
        holder: SearchViewHistoryViewHolder,
        position: Int,
    ) {
        holder.txt.text = listOfSearchItems[position].history
        holder.ivDelete.setOnClickListener {
            // delete from database
            listen.deleteItem(listOfSearchItems[position])
            // delete from adapter in real time to notify user
            deleteItem(position)
        }
        holder.txt.setOnClickListener {
            listen.getWeather(listOfSearchItems[position])
        }
    }

    private fun deleteItem(p: Int) {
        listOfSearchItems.removeAt(p)
        val updated = listOfSearchItems
        listOfSearchItems = updated
        notifyItemRemoved(p)
        notifyDataSetChanged()
    }

    fun emptyRecyclerView() {
        val l = mutableListOf<SearchViewHistory>()
        listOfSearchItems = l
        notifyDataSetChanged()
    }

    class SearchViewHistoryViewHolder(binding: RecyclerViewSearchViewHistoryEachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val txt = binding.btnSearchViewRecyclerViewEachItem
        val ivDelete = binding.ivDeleteSearchViewItem
    }
}

interface OnSearchViewHistoryItemClicked {
    fun deleteItem(s: SearchViewHistory)

    fun getWeather(s: SearchViewHistory)
}
