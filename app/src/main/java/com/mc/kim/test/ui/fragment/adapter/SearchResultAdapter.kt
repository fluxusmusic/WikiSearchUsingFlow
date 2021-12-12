package com.mc.kim.test.ui.fragment.adapter

import android.graphics.Bitmap
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mc.kim.test.R
import com.mc.kim.test.toolkit.clicks
import com.mc.kim.test.ui.fragment.loader.ResourceLoader
import com.mc.kim.test.ui.fragment.row.WikiRow


sealed class SearchResultViewHolder<E : WikiRow>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    data class HeaderHolder(val view: View) : SearchResultViewHolder<WikiRow.HeaderWikiRow>(view) {
        private val thumbnailView: ImageView = view.findViewById(R.id.thumbnail)
        private val titleView: TextView = view.findViewById(R.id.title_view)
        private val descriptionView: TextView = view.findViewById(R.id.description_view)

        override fun bind(item: WikiRow.HeaderWikiRow) {
            val wikiData = item.wikiData
            if (wikiData.thumbnail != null) {
                item.resourceLoader.loadResource(
                    wikiData.thumbnail,
                    object : ResourceLoader.ResourceCallback {
                        override fun onLoaded(bitmap: Bitmap?) {
                            thumbnailView.setImageBitmap(bitmap)
                        }
                    })
            }
            titleView.text = wikiData.displayTitle
            descriptionView.text = Html.fromHtml(wikiData.extractHtml)
            view.clicks()
            view.setOnClickListener {
                item.onItemClickListener?.onItemClick(wikiData,item.type)
            }
        }

        override fun unBind() {
            thumbnailView.setImageDrawable(null)
        }
    }

    data class RelatedHolder(val view: View) :
        SearchResultViewHolder<WikiRow.RelatedWikiRow>(view) {
        private val thumbnailView: ImageView = view.findViewById(R.id.thumbnail)
        private val titleView: TextView = view.findViewById(R.id.title_view)
        private val descriptionView: TextView = view.findViewById(R.id.description_view)

        override fun bind(item: WikiRow.RelatedWikiRow) {
            val wikiData = item.wikiData
            if (wikiData.thumbnail != null){
                item.resourceLoader.loadResource(
                    wikiData.thumbnail,
                    object : ResourceLoader.ResourceCallback {
                        override fun onLoaded(bitmap: Bitmap?) {
                            thumbnailView.setImageBitmap(bitmap)
                        }
                    })
            }

            titleView.text = wikiData.displayTitle
            descriptionView.text = wikiData.extract

            view.setOnClickListener {
                item.onItemClickListener?.onItemClick(wikiData,item.type)
            }
        }

        override fun unBind() {
            thumbnailView.setImageDrawable(null)
        }
    }

    abstract fun bind(item: E)
    abstract fun unBind()
}


class SearchResultAdapter : RecyclerView.Adapter<SearchResultViewHolder<WikiRow>>() {

    private var searchItemList: ArrayList<WikiRow> = arrayListOf()
    private val searchResultViewHolderFactory: SearchResultViewHolderFactory =
        SearchResultViewHolderFactory()

    fun reset() {
        searchItemList.clear()
        notifyDataSetChanged()
    }

    fun addItem(wikiRow: WikiRow) {
        when (wikiRow) {
            is WikiRow.HeaderWikiRow -> {
                if (searchItemList.isEmpty()) {
                    searchItemList.add(wikiRow)
                } else {
                    searchItemList[0] = wikiRow
                }
                notifyItemInserted(0)
            }
            is WikiRow.RelatedWikiRow -> {
                searchItemList.add(wikiRow)
                notifyItemInserted(searchItemList.lastIndex)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder<WikiRow> {
        return searchResultViewHolderFactory.getViewHolder(
            parent,
            WikiRow.RowType.values()[viewType]
        )
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder<WikiRow>, position: Int) {
        holder.bind(searchItemList[position])
    }

    override fun onViewRecycled(holder: SearchResultViewHolder<WikiRow>) {
        super.onViewRecycled(holder)
        holder.unBind()
    }

    override fun onViewDetachedFromWindow(holder: SearchResultViewHolder<WikiRow>) {
        super.onViewDetachedFromWindow(holder)
        holder.unBind()
    }

    override fun getItemViewType(position: Int): Int {
        return searchItemList[position].type.viewType
    }


    override fun getItemCount(): Int {
        return searchItemList.size
    }
}

class SearchResultViewHolderFactory {
    fun getViewHolder(
        parent: ViewGroup,
        rowType: WikiRow.RowType
    ): SearchResultViewHolder<WikiRow> {
        val inflater = LayoutInflater.from(parent.context)
        return when (rowType) {
            WikiRow.RowType.Header ->
                SearchResultViewHolder.HeaderHolder(inflater.inflate(R.layout.item_header, null))
            WikiRow.RowType.Item ->
                SearchResultViewHolder.RelatedHolder(inflater.inflate(R.layout.item_related, null))
        } as SearchResultViewHolder<WikiRow>
    }
}