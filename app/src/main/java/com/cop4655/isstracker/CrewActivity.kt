package com.cop4655.isstracker

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.core.net.toUri

class CrewActivity : AppCompatActivity() {
    private lateinit var iv_patch: ImageView
    private lateinit var iv_crew: ImageView
    private lateinit var tv_iss_expedition: TextView
    private lateinit var tv_start_date: TextView
    private lateinit var tv_end_date: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crew)

        iv_patch = findViewById(R.id.iv_patch)
        iv_crew = findViewById(R.id.iv_crew)
        tv_iss_expedition = findViewById(R.id.tv_iss_expedition)
        tv_start_date = findViewById(R.id.tv_start_date)
        tv_end_date = findViewById(R.id.tv_end_date)
        val crewLayout: LinearLayout = findViewById(R.id.crew)
        val topMargin: Float = 20F
        val leftMargin: Float = 305F

        ExpeditionCall().getExpedition(this) { expedition ->
            // set crew header content
            Picasso.get()
                .load(expedition.expedition_patch)
                .placeholder(R.drawable.iss_stroke_159x100_purple)
                .resize(260, 300)
                .into(iv_patch)

            iv_patch.setOnClickListener {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = expedition.expedition_url.toUri()
                startActivity(openURL)
            }

            Picasso.get()
                .load(expedition.expedition_image)
                .placeholder(R.drawable.iss_stroke_159x100_purple)
                .resize(375, 300)
                .into(iv_crew)

            iv_crew.setOnClickListener {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = expedition.expedition_url.toUri()
                startActivity(openURL)
            }

            tv_iss_expedition.text = buildString {
                append(getString(R.string.expedition))
                append(expedition.iss_expedition.toString())
            }
            tv_start_date.text = buildString {
                append(getString(R.string.start_date))
                append(epochToReadableDateWithTimeZone(expedition.expedition_start_date+(86400), "UTC").toString())
            }
            tv_end_date.text = buildString {
                append(getString(R.string.end_date))
                append(epochToReadableDateWithTimeZone(expedition.expedition_end_date+(86400), "UTC").toString())
            }

            // iterate through crew members
            for (person in expedition.people) {
                if (person.iss) {
                    // create dynamic imageview to add to layout
                    val personLayout = RelativeLayout(this)
                    val portraitImageView = ImageView(this)
                    val nameTextView = TextView(this)
                    val positionTextView = TextView(this)
                    val flagImageView = ImageView(this)
                    val agencyTextView = TextView(this)
                    val launchedTextView = TextView(this)
                    val portraitId: Int = View.generateViewId()
                    val nameId: Int = View.generateViewId()
                    val positionId: Int = View.generateViewId()
                    val flagId: Int = View.generateViewId()
                    val agencyId: Int = View.generateViewId()
                    val launchedId: Int = View.generateViewId()
                    portraitImageView.id = portraitId
                    nameTextView.id = nameId
                    positionTextView.id = positionId
                    flagImageView.id = flagId
                    agencyTextView.id = agencyId
                    launchedTextView.id = launchedId
//                    imageViewParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    //imageView.layoutParams = RelativeLayout.

                    //personLayout.setCardElevation(20F)
                    //personLayout.setCardBackgroundColor(Color.BLUE)
                    //radius = 10.toFloat()

                    val personLayoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.setMargins(5, 5, 5, 5)
                    }

                    personLayout.setBackgroundColor("#000080".toColorInt())
                    nameTextView.setTextColor("#FFFFFF".toColorInt())
                    nameTextView.setTypeface(null, Typeface.BOLD)
                    nameTextView.textSize = 20F

                    positionTextView.setTextColor("#FFFFFF".toColorInt())
                    positionTextView.setTypeface(null, Typeface.BOLD_ITALIC)
                    positionTextView.textSize = 17F

                    agencyTextView.setTextColor("#FFFFFF".toColorInt())
                    agencyTextView.setTypeface(null, Typeface.BOLD)
                    agencyTextView.textSize = 17F

                    launchedTextView.setTextColor("#FFFFFF".toColorInt())
                    launchedTextView.setTypeface(null, Typeface.BOLD)
                    launchedTextView.textSize = 17F

                    personLayout.apply {
                        this.layoutParams = personLayoutParams
                    }

                    // set x and y values for elements
                    nameTextView.x = leftMargin
                    nameTextView.y = topMargin

                    positionTextView.x = leftMargin
                    positionTextView.y = topMargin + 60F

                    flagImageView.x = leftMargin
                    flagImageView.y = topMargin + 111F

                    agencyTextView.x = leftMargin + 100F
                    agencyTextView.y = topMargin + 117F

                    launchedTextView.x = leftMargin
                    launchedTextView.y = topMargin + 177F

                    // populate images and values
                    Picasso.get()
                        .load(person.image)
                        .centerCrop()
                        .placeholder(R.drawable.astronaut)
                        .resize(290, 400)
                        .into(portraitImageView)

                    nameTextView.text = person.name
                    positionTextView.text = person.position
                    agencyTextView.text = person.agency
                    launchedTextView.text = buildString {
                        append(getString(R.string.launched))
                        append(epochToReadableDateWithTimeZone(person.launched+(86400), "UTC").toString())
                    }

                    Picasso.get()
                        .load("https://flagsapi.com/" + person.flag_code.uppercase() + "/flat/64.png")
                        .centerCrop()
                        .placeholder(R.drawable.iss_stroke_159x100_purple)
                        .resize(80, 80)
                        .into(flagImageView)

                    // add text and images to personLayout
                    personLayout.addView(portraitImageView)
                    personLayout.addView(nameTextView)
                    personLayout.addView(positionTextView)
                    personLayout.addView(flagImageView)
                    personLayout.addView(agencyTextView)
                    personLayout.addView(launchedTextView)
                    // add personLayout to crewLayout
                    crewLayout.addView(personLayout)
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.crew)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun epochToReadableDateWithTimeZone(epochSeconds: Long, timeZone: String): String {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.of(timeZone))
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return dateTime.format(formatter)
    }
}