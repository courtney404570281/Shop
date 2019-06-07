package com.courtney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_movie.*
import kotlinx.android.synthetic.main.row_movie.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import java.net.URL

class MovieActivity : AppCompatActivity(), AnkoLogger {

    var movies: List<Movie>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        // Get json
        doAsync {
            val json = URL("http://api.myjson.com/bins/wk2gd").readText()
            movies = Gson().fromJson<List<Movie>>(json,
                object : TypeToken<List<Movie>>(){}.type)
            movies?.forEach {
                info{ "${it.Title} ${it.imdbRating}" }
            }
            uiThread {
                recycler_movie.layoutManager = LinearLayoutManager(this@MovieActivity)
                recycler_movie.setHasFixedSize(true)
                recycler_movie.adapter = MovieAdapter()
            }
        }
    }

    inner class MovieAdapter : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_movie, parent, false)
            return MovieHolder(view)
        }

        override fun getItemCount(): Int {
            val size = movies?.size?: 0
            return size
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies?.get(position)
            holder.bindMovie(movie!!)
        }
    }

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText = view.txt_title
        val ratingText = view.txt_rating
        val directorText = view.txt_director
        val movieImage = view.img_movie
        fun bindMovie(movie: Movie) {
            titleText.text = movie.Title
            ratingText.text = movie.imdbRating
            directorText.text = movie.Director
            Glide.with(this@MovieActivity)
                .load(movie.Poster)
                .override(300)
                .into(movieImage)
        }
    }

    data class Movie(
    val Actors: String,
    val Awards: String,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String
    )
}
