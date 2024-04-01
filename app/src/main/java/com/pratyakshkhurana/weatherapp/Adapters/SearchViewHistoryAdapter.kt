package com.pratyakshkhurana.weatherapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pratyakshkhurana.weatherapp.Entity.SearchViewHistory
import com.pratyakshkhurana.weatherapp.databinding.RecyclerViewSearchViewHistoryEachItemBinding

class SearchViewHistoryAdapter(
    private val listOfSearchItems: List<SearchViewHistory>,
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
        holder.btnText.text =
            listOfSearchItems[position].id.toString() + " " + listOfSearchItems[position].history
        holder.btnText.setOnClickListener {
            listen.onSearchViewHistoryItemClickedResponse(position)
        }
    }

    class SearchViewHistoryViewHolder(binding: RecyclerViewSearchViewHistoryEachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val btnText = binding.btnSearchViewRecyclerViewEachItem
    }
}

interface OnSearchViewHistoryItemClicked {
    fun onSearchViewHistoryItemClickedResponse(pos: Int)
}
