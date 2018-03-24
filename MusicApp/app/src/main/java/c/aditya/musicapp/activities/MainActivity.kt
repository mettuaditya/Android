package c.aditya.musicapp.activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import c.aditya.musicapp.R
import c.aditya.musicapp.activities.MainActivity.Statified.drawerLayout
import c.aditya.musicapp.adapter.NavigationDrawerAdapter
import c.aditya.musicapp.fragments.MainScreenFragment
import c.aditya.musicapp.fragments.SongPlayingFragment

class MainActivity : AppCompatActivity(){

    var navigationDrawerIconslist: ArrayList<String> = arrayListOf()

    var images_for_nav_drawer = intArrayOf(R.drawable.navigation_allsongs,
            R.drawable.navigation_favorites,
            R.drawable.navigation_settings,
            R.drawable.navigation_aboutus)
    var trackNotificationBuilder : Notification? = null

    object Statified {
        var drawerLayout: DrawerLayout? = null
        var notificationManger:NotificationManager? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationDrawerIconslist.add("All Songs")
        navigationDrawerIconslist.add("Favorites")
        navigationDrawerIconslist.add("Settings")
        navigationDrawerIconslist.add("About Us")


        var toggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout?.setDrawerListener(toggle)
        toggle.syncState()

        var mainScreenFragment = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.detail_fragment, mainScreenFragment , "MainScreenFragment" )
                .commit()

        var _navigationAdapter = NavigationDrawerAdapter(navigationDrawerIconslist,images_for_nav_drawer,this)
        _navigationAdapter.notifyDataSetChanged()
        var navigation_recycler_view = findViewById<RecyclerView>(R.id.navigation_recycler_view)
        navigation_recycler_view.layoutManager  = LinearLayoutManager(this)
        navigation_recycler_view.itemAnimator = DefaultItemAnimator()
        navigation_recycler_view.adapter = _navigationAdapter
        navigation_recycler_view.setHasFixedSize(true)

        val intent = Intent(this@MainActivity,MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this@MainActivity,System.currentTimeMillis().toInt(),
                            intent, 0)
        trackNotificationBuilder = Notification.Builder(this)
                .setContentTitle("A track will play in BackGroung")
                .setSmallIcon(R.drawable.echo_logo)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()

        Statified.notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    override fun onStart() {
        super.onStart()
        try {
            Statified.notificationManger?.cancel(1978)

        }catch (e: Exception){
            e.printStackTrace()

        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if(SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                Statified.notificationManger?.notify(1978,trackNotificationBuilder)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Statified.notificationManger?.cancel(1978)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}
