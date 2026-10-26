package com.example.scrobblerhomepage

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlin.div
import kotlin.toString

class MainActivity : AppCompatActivity() {

    lateinit var topLabel: TextView
    lateinit var viewModel: MainViewModel

    lateinit var addSongButton: ExtendedFloatingActionButton

    lateinit var playedTracksCounter: TextView
    lateinit var playedMinutesCounter: TextView
    lateinit var playedArtistsCounter: TextView
    lateinit var playedAlbumsCounter: TextView

    lateinit var coverImage: ImageView

    lateinit var songNameText: TextView
    lateinit var artistNameText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addSongButton = findViewById(R.id.add_track_button)
        topLabel = findViewById(R.id.top_label)

        val provider = ViewModelProvider(this)
        viewModel = provider[MainViewModel::class.java]

        playedTracksCounter = findViewById(R.id.played_tracks_counter)!!
        playedMinutesCounter = findViewById(R.id.played_minutes_counter)!!
        playedArtistsCounter = findViewById(R.id.played_artists_counter)!!
        playedAlbumsCounter = findViewById(R.id.played_albums_counter)!!

        songNameText = findViewById(R.id.song_name_text)
        artistNameText = findViewById(R.id.song_artist_text)
        coverImage = findViewById(R.id.album_image)

        addSongButton.setOnClickListener {
            val dialog = AddSongDialog()
            dialog.show(supportFragmentManager, "AddSongDialog")
        }

        observeLiveData()
    }

    fun observeLiveData() {
        viewModel.tracks.observe(this) {
            playedTracksCounter.text = it.size.toString()
            playedMinutesCounter.text = (viewModel.getCountMinutes() / 60).toString()
            playedArtistsCounter.text = viewModel.getArtistList().distinct().size.toString()
            playedAlbumsCounter.text = viewModel.getAlbumList().distinct().size.toString()
            if (viewModel.getTrackList().isNotEmpty()) {
                songNameText.text = viewModel.getTrackList().last().getName()
                artistNameText.text = viewModel.getTrackList().last().getArtist()
                coverImage.setImageResource(viewModel.getTrackList().last().getCoverResId())
            } else {
                songNameText.text = "Пока ничего нет"
                artistNameText.text = "Добавь трек!"
                coverImage.setImageResource(R.drawable.no_track_cover)
            }
        }
    }
}