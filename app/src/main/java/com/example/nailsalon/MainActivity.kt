package com.example.nailsalon

import android.content.Intent
import kotlinx.coroutines.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.viewpager.widget.*
import android.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private val imageList = listOf(R.drawable.purple_french, R.drawable.french, R.drawable.green_french)
    private var currentIndex = 0
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val booking: Button = findViewById(R.id.booking)

        booking.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java)
            startActivity(intent)
        }

        viewPager = findViewById(R.id.viewPager)
        val adapter = ImagePagerAdapter(imageList)
        viewPager.adapter = adapter

        startImageSlideshow()
    }

    private fun startImageSlideshow() {
        coroutineScope.launch {
            while (true) {
                val currentImage = imageList[currentIndex]
                viewPager.currentItem = currentIndex
                currentIndex = (currentIndex + 1) % imageList.size
                delay(3000)
            }
        }
    }

    private inner class ImagePagerAdapter(private val images: List<Int>) : PagerAdapter() {
        override fun getCount(): Int {
            return images.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(container.context)
            imageView.setImageResource(images[position])
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}