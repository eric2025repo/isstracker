package com.cop4655.isstracker

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.squareup.picasso.Picasso

class CrewActivity : AppCompatActivity() {
    private lateinit var tv_iss_expedition: TextView
    private lateinit var tv_expedition_url: TextView
    private lateinit var tv_people: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crew)

//        var iss_expedition: Int
//        var expedition_url: String

        tv_iss_expedition = findViewById(R.id.tv_iss_expedition)
        tv_expedition_url = findViewById(R.id.tv_expedition_url)
        tv_people = findViewById(R.id.tv_people)
        val crewLayout: LinearLayout = findViewById(R.id.crew)


        ExpeditionCall().getExpedition(this) { expedition ->
            tv_iss_expedition.text = expedition.iss_expedition.toString()
            tv_expedition_url.text = expedition.expedition_url
            var listing = ""
            for (person in expedition.people) {
                if (person.iss) {
                    listing = listing + person.name + "\n"
                    // create dynamic imageview to add to layout
                    val cardView = CardView(this)
                    val imageView = ImageView(this)
//                    imageView.layoutParams = LinearLayout.LayoutParams(400, 400)
                    cardView.setCardElevation(20F)



                    // get image to load into dynamic imageview
                    Picasso.get()
                        .load(person.image)
                        .centerCrop()
                        .placeholder(R.drawable.iss_stroke_159x100_purple)
                        .resize(200,300)
                        .into(imageView)

                    // add image to cardView
                    cardView.addView(imageView)
                    // add cardView to layout
                    crewLayout.addView(cardView)
                }
            }
            tv_people.text = listing
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.crew)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}