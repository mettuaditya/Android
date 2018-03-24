package c.aditya.musicapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import c.aditya.musicapp.R
import c.aditya.musicapp.activities.MainActivity
import c.aditya.musicapp.fragments.AboutUsFragment
import c.aditya.musicapp.fragments.FavoriteFragment
import c.aditya.musicapp.fragments.MainScreenFragment
import c.aditya.musicapp.fragments.SettingsFragment

/**
 * Created by adithya on 14-12-2017.
 */
class NavigationDrawerAdapter  (_contentList: ArrayList<String>, _getImages: IntArray, _context : Context ): RecyclerView.Adapter<NavigationDrawerAdapter.NavViewHolder>(){

    var contentList: ArrayList<String>? = null
    var getImages: IntArray? = null
    var mContext: Context? = null

    init {
        this.contentList = _contentList
        this.getImages = _getImages
        this.mContext = _context
    }
    override fun onBindViewHolder(holder: NavViewHolder?, position: Int) {

        holder?.icon_GET?.setBackgroundResource(getImages?.get(position) as Int)
        holder?.text_GET?.setText(contentList?.get(position))
        holder?.contentHolder?.setOnClickListener({
            if (position == 0) {
                var mainScreenFragment = MainScreenFragment( )
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.detail_fragment, mainScreenFragment)
                        .commit()
            }
            else if (position == 1){
                var favoriteFragment = FavoriteFragment( )
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.detail_fragment, favoriteFragment)
                        .commit()
            }
            else if (position == 2){
                var settingsFragment = SettingsFragment( )
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.detail_fragment, settingsFragment)
                        .commit()
            }
            else {
                var aboutUsFragment = AboutUsFragment( )
                (mContext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.detail_fragment, aboutUsFragment)
                        .commit()
            }
            MainActivity.Statified.drawerLayout?.closeDrawers()
        })
    }

    override fun getItemCount(): Int {

        return contentList?.size as Int
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NavViewHolder {
        var itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_coustum_navigationdrawer , parent,false)
        val returnThis = NavViewHolder(itemView)
        return returnThis
    }

    class NavViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

         var icon_GET : ImageView? = null
        var text_GET : TextView? = null
        var contentHolder : RelativeLayout? = null

        init {
            icon_GET = itemView?.findViewById(R.id.icon_navdrawer)
            text_GET = itemView?.findViewById(R.id.text_navdrawer)
            contentHolder = itemView?.findViewById(R.id.navdrawer_item_content_holder)
        }
    }

}
