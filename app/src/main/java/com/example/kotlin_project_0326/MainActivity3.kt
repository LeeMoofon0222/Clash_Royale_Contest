package com.example.kotlin_project_0326

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.IOException

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val recordButton = findViewById<Button>(R.id.button6)
        val stopRecordButton = findViewById<Button>(R.id.button8)
        val nextButton = findViewById<Button>(R.id.button11)
        var recordtext = findViewById<TextView>(R.id.textView11)

        nextButton.setOnClickListener {
            val currentIntent = intent
            val resourceId1 = currentIntent.getIntExtra("resourceId1", 0)
            val resourceId2 = currentIntent.getIntExtra("resourceId2", 0)
            val imageName1 = currentIntent.getStringExtra("imageName1")
            val imageName2 = currentIntent.getStringExtra("imageName2")

            Log.d("MainActivity3", "$resourceId1, $resourceId2 $imageName2")

            val intent = Intent(this, MainActivity4::class.java)
            intent.putExtra("resourceId1", resourceId1)
            intent.putExtra("resourceId2", resourceId2)
            intent.putExtra("imageName1", imageName1)
            intent.putExtra("imageName2", imageName2)
            intent.putExtra("outputFile", outputFile)

            startActivity(intent)
        }
        recordButton.setOnClickListener {
            recordtext.text = "Recording …"
            startRecording()
            recordButton.isEnabled = false
            stopRecordButton.isEnabled = true
        }

        stopRecordButton.setOnClickListener {
            recordtext.text = "Recording to ${outputFile}"
            stopRecording()
            recordButton.isEnabled = true
            stopRecordButton.isEnabled = false
        }
        val playbackButton = findViewById<Button>(R.id.button9)
        val stopPlaybackButton = findViewById<Button>(R.id.button10)

        playbackButton.setOnClickListener {
            recordtext.text = "Playback …"
            playRecording()
            playbackButton.isEnabled = false
            stopPlaybackButton.isEnabled = true
        }

        stopPlaybackButton.setOnClickListener {
            recordtext.text = "Playback Stop"
            stopPlaying()
            playbackButton.isEnabled = true
            stopPlaybackButton.isEnabled = false
        }
    }
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: String? = null

    private fun startRecording() {
        outputFile = "${externalCacheDir?.absolutePath}/recording.mp3"
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e("APP", "prepare() failed")
            }
            start()
        }
    }
    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    private var mediaPlayer: MediaPlayer? = null

    private fun playRecording() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(outputFile)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("APP", "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}