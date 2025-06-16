package com.cop4655.isstracker

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import androidx.core.graphics.toColorInt

class CrewActivity : AppCompatActivity() {
    private lateinit var tv_iss_expedition: TextView
    private lateinit var tv_expedition_url: TextView
    private lateinit var tv_people: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crew)

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
                    val personLayout = RelativeLayout(this)
                    val portraitImageView = ImageView(this)
                    val nameTextView = TextView(this)
                    val flagImageView = ImageView(this)
                    val portraitId: Int = View.generateViewId()
                    val nameId: Int = View.generateViewId()
                    val flagId: Int = View.generateViewId()
                    portraitImageView.id = portraitId
                    nameTextView.id = nameId
                    flagImageView.id = flagId
//                    imageViewParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    //imageView.layoutParams = RelativeLayout.

                    //personLayout.setCardElevation(20F)
                    //personLayout.setCardBackgroundColor(Color.BLUE)
                    //radius = 10.toFloat()

                    val personLayoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT).also {
                        it.setMargins(5, 5, 5, 5)
                    }

                    personLayout.setBackgroundColor("#000080".toColorInt())
                    nameTextView.setTextColor("#FFFFFF".toColorInt())
                    nameTextView.setTypeface(null, Typeface.BOLD)
                    nameTextView.textSize = 20F

                    personLayout.apply {
                        this.layoutParams = personLayoutParams
                    }

                    nameTextView.x = 250F
                    nameTextView.y = 0F

                    flagImageView.x = 250F
                    flagImageView.y = 75F

                    // get image to load into dynamic imageview
                    Picasso.get()
                        .load(person.image)
                        .centerCrop()
                        .placeholder(R.drawable.iss_stroke_159x100_purple)
                        .resize(200,300)
                        .into(portraitImageView)

                    nameTextView.text = person.name

                    Picasso.get()
                        .load("https://flagsapi.com/" + person.flag_code.uppercase() + "/flat/64.png")
                        .centerCrop()
                        .placeholder(R.drawable.iss_stroke_159x100_purple)
                        .resize(64,64)
                        .into(flagImageView)

                    // add image to cardView
                    personLayout.addView(portraitImageView)
                    personLayout.addView(nameTextView)
                    personLayout.addView(flagImageView)
                    // add cardView to layout
                    crewLayout.addView(personLayout)
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