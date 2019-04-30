package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MovieDetail() :Parcelable {
    @SerializedName("poster_path")
    private var  posterPath: String?=null
    @SerializedName("vote_average")
    private var voteAverage: Double? = null
    @SerializedName("overview")
    private  var overview: String?=null
    @SerializedName("release_date")
    private var releaseDate: String? = null
    @SerializedName("title")
    private var title: String? = null
    @SerializedName("original_language")
    private var originalLanguage:String?=null

    constructor(parcel: Parcel) : this() {
        posterPath = parcel.readString()
        voteAverage = parcel.readValue(Double::class.java.classLoader) as? Double
        overview = parcel.readString()
        releaseDate = parcel.readString()
        title = parcel.readString()
        originalLanguage = parcel.readString()
    }


    fun getPosterPath(): String? {
        return posterPath
    }

    fun setPosterPath(posterPath: String) {
        this.posterPath = posterPath
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
    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }
    fun getVoteAverage(): Double? {
        return voteAverage
    }

    fun setVoteAverage(voteAverage: Double?) {
        this.voteAverage = voteAverage

    }
    fun getOriginalLanguage(): String? {
        return originalLanguage
    }

    fun setOriginalLanguage(originalLanguage: String) {
        this.originalLanguage = originalLanguage
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(posterPath)
        parcel.writeValue(voteAverage)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeString(title)
        parcel.writeString(originalLanguage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieDetail> {
        override fun createFromParcel(parcel: Parcel): MovieDetail {
            return MovieDetail(parcel)
        }

        override fun newArray(size: Int): Array<MovieDetail?> {
            return arrayOfNulls(size)
        }
    }


}



