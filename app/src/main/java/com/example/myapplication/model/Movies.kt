package com.example.myapplication.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Movies() : Parcelable, Serializable {
    @SerializedName("poster_path")
    private var  posterPath: String?=null
    @SerializedName("adult")
    private var adult: Boolean = false
    @SerializedName("overview")
    private  var overview: String?=null
    @SerializedName("release_date")
    private var releaseDate: String? = null
    @SerializedName("genre_ids")
    private var genreIds = ArrayList<Int>()
    @SerializedName("id")
    private var id: Int? = null
    @SerializedName("original_title")
    private var originalTitle: String? = null
    @SerializedName("original_language")
    private var originalLanguage:String?=null
    @SerializedName("title")
    private var title: String? = null
    @SerializedName("backdrop_path")
    private var backdropPath: String? = null
    @SerializedName("popularity")
    private var popularity: Double? = null
    @SerializedName("vote_count")
    private var voteCount: Int? = null
    @SerializedName("video")
    private var video: Boolean = false
    @SerializedName("vote_average")
    private var voteAverage: Double? = null

    var isFavorite:Boolean=false


    constructor(parcel: Parcel) : this() {
        posterPath = parcel.readString()
        adult = parcel.readByte() != 0.toByte()
        overview = parcel.readString()
        releaseDate = parcel.readString()
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        originalTitle = parcel.readString()
        originalLanguage = parcel.readString()
        title = parcel.readString()
        backdropPath = parcel.readString()
        popularity = parcel.readValue(Double::class.java.classLoader) as? Double
        voteCount = parcel.readValue(Int::class.java.classLoader) as? Int
        video = parcel.readByte() != 0.toByte()
        voteAverage = parcel.readValue(Double::class.java.classLoader) as? Double
    }


    fun getPosterPath(): String? {
        return posterPath
    }

    fun setPosterPath(posterPath: String) {
        this.posterPath = posterPath
    }

    fun getAdult(): Boolean {
        return adult
    }

    fun setAdult(adult: Boolean) {
        this.adult = adult
    }

    fun getOverview(): String? {
        return overview
    }

    fun setOverview(overview: String) {
        this.overview = overview
    }

    fun getReleaseDate(): String? {
        return releaseDate
    }

    fun setReleaseDate(releaseDate: String) {
        this.releaseDate = releaseDate
    }

    fun getGenreIds(): List<Int> {
        return genreIds
    }

    fun setGenreIds(genreIds: ArrayList<Int>) {
        this.genreIds = genreIds
    }

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getOriginalTitle(): String? {
        return originalTitle
    }

    fun setOriginalTitle(originalTitle: String) {
        this.originalTitle = originalTitle
    }

    fun getOriginalLanguage(): String? {
        return originalLanguage
    }

    fun setOriginalLanguage(originalLanguage: String) {
        this.originalLanguage = originalLanguage
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getBackdropPath(): String? {
        return backdropPath
    }

    fun setBackdropPath(backdropPath: String) {
        this.backdropPath = backdropPath
    }

    fun getPopularity(): Double? {
        return popularity
    }

    fun setPopularity(popularity: Double?) {
        this.popularity = popularity
    }

    fun getVoteCount(): Int? {
        return voteCount
    }

    fun setVoteCount(voteCount: Int?) {
        this.voteCount = voteCount
    }

    fun getVideo(): Boolean {
        return video
    }

    fun setVideo(video: Boolean) {
        this.video = video
    }

    fun getVoteAverage(): Double? {
        return voteAverage
    }

    fun setVoteAverage(voteAverage: Double?) {
        this.voteAverage = voteAverage

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(posterPath)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeValue(id)
        parcel.writeString(originalTitle)
        parcel.writeString(originalLanguage)
        parcel.writeString(title)
        parcel.writeString(backdropPath)
        parcel.writeValue(popularity)
        parcel.writeValue(voteCount)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeValue(voteAverage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movies> {
        override fun createFromParcel(parcel: Parcel): Movies {
            return Movies(parcel)
        }

        override fun newArray(size: Int): Array<Movies?> {
            return arrayOfNulls(size)
        }
    }


}
