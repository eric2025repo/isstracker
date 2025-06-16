package com.cop4655.isstracker

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        val crewLayout: RelativeLayout = findViewById(R.id.crew)


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
                    val flagImageView = ImageView(this)
                    val portraitId: Int = View.generateViewId()

//                    imageViewParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    //imageView.layoutParams = RelativeLayout.

                    cardView.setCardElevation(20F)
                    cardView.id = portraitId
                    cardView.setCardBackgroundColor(Color.BLUE)

                    val cardViewParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT).also {
                        it.setMargins(5, 5, 5, 5)
                    }

                    cardView.apply {
                        radius = 10.toFloat()
                        this.layoutParams = cardViewParams
                    }


                    // get image to load into dynamic imageview
                    Picasso.get()
                        .load(person.image)
                        .centerCrop()
                        .placeholder(R.drawable.iss_stroke_159x100_purple)
                        .resize(200,300)
                        .into(imageView)

                    Picasso.get()
                        .load("https://flagsapi.com/" + person.flag_code.uppercase() + "/flat/64.png")
                        .centerCrop()
                        .placeholder(R.drawable.iss_stroke_159x100_purple)
                        .resize(64,64)
                        .into(flagImageView)


                    // add image to cardView
                    cardView.addView(imageView)
                    cardView.addView(flagImageView)
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