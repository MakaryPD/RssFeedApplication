package makarypd.pl.com.toptendownload

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.graphics.BitmapCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import java.net.HttpURLConnection
import java.net.URL

class FeedAdapter(context: Context, val resource: Int, private val applications: List<AppEntity>): ArrayAdapter<AppEntity>(context, resource, applications) {

    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentApp = applications[position]

        viewHolder.tv_name.text = currentApp.name
        viewHolder.tv_artist.text = "Created By: ${currentApp.artist}"
        viewHolder.tv_summary.text = "Summary: ${currentApp.summary}"

        return view
    }

    override fun getCount(): Int {
        return applications.size
    }
}
