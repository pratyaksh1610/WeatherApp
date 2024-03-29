package com.pratyakshkhurana.weatherapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pratyakshkhurana.weatherapp.databinding.RecyclerViewSearchViewHistoryEachItemBinding

class SearchViewHistoryAdapter(
    private val listOfSearchItems: List<String>,
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
        holder.btntext.text = listOfSearchItems[position]
        holder.btntext.setOnClickListener {
            listen.onSearchViewHistoryItemClickedResponse(position)
        }
    }

    class SearchViewHistoryViewHolder(binding: RecyclerViewSearchViewHistoryEachItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val btntext = binding.btn
    }
}

interface OnSearchViewHistoryItemClicked {
    fun onSearchViewHistoryItemClickedResponse(pos: Int)
}
