package makarypd.pl.com.toptendownload

import android.view.View
import android.widget.TextView

class ViewHolder(v: View) {
    val tv_name = v.findViewById<TextView>(R.id.entity_name)
    val tv_artist = v.findViewById<TextView>(R.id.entity_artist)
    val tv_summary = v.findViewById<TextView>(R.id.entity_summary)
}