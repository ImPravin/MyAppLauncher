package com.sample.myapplauncher

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.appsmodule.AppInfo
import java.util.*
import kotlin.collections.ArrayList


class AppListAdapter(private val currentList: List<AppInfo>) :
    RecyclerView.Adapter<AppListAdapter.AppViewHolder>(), Filterable {
    private var appsFilterList: List<AppInfo> = ArrayList()

    init {
        appsFilterList = currentList
    }

    class AppViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val appNameTextView: TextView = itemView.findViewById(R.id.appNameTextView)
        private val appIconImageView: ImageView = itemView.findViewById(R.id.appIconImageView)
        private var currentApp: AppInfo? = null
        private val context: Context = itemView.context

        init {
            itemView.setOnClickListener {
                currentApp?.let {
                    val launchIntent: Intent? = context.packageManager
                        .getLaunchIntentForPackage(it.packageName)
                    context.startActivity(launchIntent)
                }
            }

            itemView.setOnLongClickListener {
                currentApp?.let {
                    val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    i.addCategory(Intent.CATEGORY_DEFAULT)
                    i.data = Uri.parse("package:" + it.packageName)
                    context.startActivity(i)
                }
                true
            }
        }

        fun bind(appInfo: AppInfo) {
            currentApp = appInfo
            appNameTextView.text = appInfo.label
            appIconImageView.setImageDrawable(appInfo.appIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_launcher_row, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val appInfo = appsFilterList[position]
        holder.bind(appInfo)
    }

    override fun getItemCount(): Int {
        return appsFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                appsFilterList = if (charSearch.isEmpty()) {
                    currentList
                } else {
                    val resultList = ArrayList<AppInfo>()
                    for (row in currentList) {
                        if (row.label.lowercase(Locale.getDefault()).startsWith(
                                constraint.toString()
                                    .lowercase(Locale.getDefault())
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = appsFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                appsFilterList = results?.values as List<AppInfo>
                notifyDataSetChanged()
            }
        }
    }
}