package com.example.kotlin_project_0326

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity2 : AppCompatActivity() {

    private val imageNameList = listOf(
        "archer_queen",
        "electro_giant",
        "elixir_golem",
        "giant",
        "giant_skeleton",
        "goblin_giant",
        "golem",
        "lava_hound",
        "mega_knight",
        "royal_hog",
        "royal_giant",
        "skeleton_king"
    )
    private lateinit var playerOneTextView: TextView
    private lateinit var playerTwoTextView: TextView
    private lateinit var playerOneImageView: ImageView
    private lateinit var playerTwoImageView: ImageView

    private var selectedPlayerOne: String? = null
    private var selectedPlayerTwo: String? = null
    private var selectedPlayerTwoImageName: String? = null
    private var selectedPlayerTwoResourceId: Int? = null
    private var resourceId: String? = null

    private fun handleImageClick(imageName: String) {
        val resourceId = resources.getIdentifier(imageName, "drawable", packageName)

        if (selectedPlayerOne == null) {
            selectedPlayerOne = imageName
            playerOneTextView.text = "Player 1: $imageName"
            playerOneImageView.setImageResource(resourceId)
        } else if (selectedPlayerTwo == null && imageName != selectedPlayerOne) {
            selectedPlayerTwo = imageName
            selectedPlayerTwoImageName = imageName
            selectedPlayerTwoResourceId = resourceId
            playerTwoTextView.text = "Player 2: $imageName"
            playerTwoImageView.setImageResource(resourceId)
        } else {
            Toast.makeText(this, "Please pick two different players", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val button = findViewById<Button>(R.id.button2)

        playerOneTextView = findViewById(R.id.textView8)
        playerTwoTextView = findViewById(R.id.textView4)
        playerOneImageView = findViewById(R.id.playerOneImage)
        playerTwoImageView = findViewById(R.id.playerTwoImage)

        val gridView = findViewById<GridView>(R.id.gridView)
        gridView.adapter = ImageAdapter(this, imageNameList)

        button.setOnClickListener {
            if(selectedPlayerTwo != null && selectedPlayerOne != null){
                val intent = Intent(this, MainActivity3::class.java)
                val resourceId1 = resources.getIdentifier(selectedPlayerOne, "drawable", packageName)
                val resourceId2 = selectedPlayerTwoResourceId ?: 0
                intent.putExtra("resourceId1", resourceId1)
                intent.putExtra("resourceId2", resourceId2)
                intent.putExtra("imageName1", selectedPlayerOne)
                intent.putExtra("imageName2", selectedPlayerTwoImageName)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Need to pick two players", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private class ImageAdapter(
        private val context: Context,
        private val imageNameList: List<String>
    ) : BaseAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int = imageNameList.size

        override fun getItem(position: Int): Any = imageNameList[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view: View
            if (convertView == null) {
                view = inflater.inflate(R.layout.grid_item_layout, parent, false)
            } else {
                view = convertView
            }

            val imageView = view.findViewById<ImageView>(R.id.gridImageView)
            val textView = view.findViewById<TextView>(R.id.gridTextView)

            val imageName = imageNameList[position]

            val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            imageView.setImageResource(resourceId)

            textView.text = imageName

            imageView.setOnClickListener {
                (context as MainActivity2).handleImageClick(imageName)
            }

            return view
        }

        override fun hasStableIds(): Boolean = true

        override fun areAllItemsEnabled(): Boolean = true

        override fun isEnabled(position: Int): Boolean = true

        override fun getItemViewType(position: Int): Int = 0

        override fun getViewTypeCount(): Int = 1

        override fun isEmpty(): Boolean = imageNameList.isEmpty()
    }
}