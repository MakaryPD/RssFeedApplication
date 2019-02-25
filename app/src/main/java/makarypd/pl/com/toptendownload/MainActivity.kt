package makarypd.pl.com.toptendownload

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var urlPath : String = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var limit = 10


    private val listView: ListView by lazy { findViewById<ListView>( R.id.xmlListView ) }
    private var dataDownloader: DownloadData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        downloadUrl(urlPath.format(limit))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when( item?.itemId ){
            R.id.paid_application_menu_item -> urlPath = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.free_apps_menu_item -> urlPath = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.songs_menu_item -> urlPath = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.top25_menu_item, R.id.top10_menu_item ->
                if( !item.isChecked ) {
                    item.isChecked = true
                    limit = 35 - limit
                }
            else -> return super.onOptionsItemSelected(item)
        }

        downloadUrl( urlPath.format(limit))
        return true
    }

    private fun downloadUrl( feedUrl: String){
        dataDownloader = DownloadData(this, listView)
        dataDownloader?.execute( feedUrl )
    }


    override fun onDestroy() {
        super.onDestroy()
        dataDownloader?.cancel(true)
    }

    companion object {

         class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
            private val TAG: String = "DownloadData"

            var downloadDataContext: Context by Delegates.notNull()
            var downloadDataListView: ListView by Delegates.notNull()

            init {
                downloadDataContext = context
                downloadDataListView = listView
            }

            override fun doInBackground(vararg url: String?): String {
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "DoInBackground: Error while Downloading")
                } else {
                    Log.d(TAG, "Download Completed")
                }
                return rssFeed
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                val parseApplication = ParseApplications()
                parseApplication.parse(result)
                val adapter = FeedAdapter(downloadDataContext, R.layout.list_record, parseApplication.applications)
                downloadDataListView.adapter = adapter
            }


            private fun downloadXML(urlPath: String?): String {

                val xmlResult = StringBuilder()

                try {
                    val url = URL(urlPath)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode

                    val stream = connection.inputStream
                    stream.buffered().reader().use { reader ->
                        xmlResult.append(reader.readText())
                    }

                    return xmlResult.toString()

                } catch (e: Exception) {
                    val errorMessage: String = when (e) {
                        is MalformedURLException -> "DoInBackground: Error - Invalid URL: ${e.message}"
                        is IOException -> "Download - IOException: ${e.message}"
                        is SecurityException -> "Download XML: Security Exception - missing permissions? ${e.message}"
                        else -> "Unknown Error: ${e.message}"
                    }
                    Log.e(TAG, errorMessage)
                }
                return "" //If it gets to here there have been a problem
            }
        }
    }
}
