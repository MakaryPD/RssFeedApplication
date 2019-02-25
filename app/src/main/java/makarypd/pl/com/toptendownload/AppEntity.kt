package makarypd.pl.com.toptendownload

class AppEntity(var name: String = "", var artist: String = "", var releaseDate: String = "", var summary: String = "", var imgUrl: String = "" ) {


    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            summary = $summary
            imgUrl = $imgUrl
            """.trimIndent()
    }
}