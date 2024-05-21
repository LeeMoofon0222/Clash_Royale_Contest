package com.example.kotlin_project_0326

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivity4 : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var outputFile: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        val img1 = findViewById<ImageView>(R.id.imageView2)
        val img2 = findViewById<ImageView>(R.id.imageView3)
        val name1 = findViewById<TextView>(R.id.textView5)
        val name2 = findViewById<TextView>(R.id.textView6)
        val context = findViewById<Button>(R.id.button3)
        val back = findViewById<Button>(R.id.button7)
        val imgFrame = findViewById<ImageView>(R.id.imageView4)
        imgFrame.setBackgroundResource(R.drawable.loading_animation2)
        val animation = imgFrame.background as AnimationDrawable
        animation.start()

        val resourceId1 = intent.getIntExtra("resourceId1", 0)
        val resourceId2 = intent.getIntExtra("resourceId2", 0)
        val imageName1 = intent.getStringExtra("imageName1")
        val imageName2 = intent.getStringExtra("imageName2")

        val player1speed = ((resourceId1 % 10)*5 + (5..90).random())
        val player2speed = ((resourceId1 % 10)*5 + (5..90).random())

        img1.setImageResource(resourceId1)
        img2.setImageResource(resourceId2)
        name1.text = imageName1
        name2.text = imageName2
        Log.d("MainActivity4", resourceId1.toString())

        outputFile = intent.getStringExtra("outputFile") ?: ""


        context.setOnClickListener {
            playRecording()
            startContest(player1speed,player2speed)
        }

        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startContest(player1speed: Int, player2speed: Int) {
        val seekBar1 = findViewById<SeekBar>(R.id.seekBar)
        val seekBar2 = findViewById<SeekBar>(R.id.seekBar2)
        CoroutineScope(Dispatchers.Main).launch {
            val job1 = launch {
                for (i in 0..100) {
                    delay((150 - player1speed).toLong()) // 速度越快，延遲越短
                    seekBar1.progress = i
                }
            }

            val job2 = launch {
                for (i in 0..100) {
                    delay((150 - player2speed).toLong()) // 速度越快，延遲越短
                    seekBar2.progress = i
                }
            }
            joinAll(job1, job2)
            checkWinner(player1speed,player2speed)
        }
    }
    private fun playRecording() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(outputFile)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("APP", "prepare() failed", e)
            }
        }
    }

    private fun checkWinner(player1speed: Int, player2speed: Int) {
        val imgFrame = findViewById<ImageView>(R.id.imageView4)
        imgFrame.setBackgroundResource(R.drawable.loading_animation1)
        val animation = imgFrame.background as AnimationDrawable
        animation.start()
        if(player1speed>player2speed){
            Toast.makeText(this, "Player1 is the winner~", Toast.LENGTH_SHORT).show()
        }
        if(player1speed<player2speed){
            Toast.makeText(this, "Player2 is the winner~", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "tied", Toast.LENGTH_SHORT).show()
        }
    }
}
