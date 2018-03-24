package c.aditya.musicapp.fragments


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import c.aditya.musicapp.R
import c.aditya.musicapp.Songs
import c.aditya.musicapp.adapter.FavoriteAdaptor
import c.aditya.musicapp.databases.EchoDatabase


/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {

    var  myActivity:Activity? = null
    var getSongsList: ArrayList<Songs>? = null

    var noFavorites:TextView? = null
    var nowPlayingButton: RelativeLayout? = null
    var playPauseButton: ImageButton? = null
    var songTilte: TextView? =null
    var recyclerView: RecyclerView? = null
    var trackPosition:Int = 0
    var favoriteContent : EchoDatabase? = null

    var refreshList : ArrayList<Songs>? = null
    var getListFromDatabase:ArrayList<Songs>? = null
    object  Statified{
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_favorite, container, false)
        activity.title = "Favorites"
        noFavorites = view?.findViewById(R.id.noFavorites)
        nowPlayingButton = view.findViewById(R.id.hiddenBarFavScreen)
        songTilte = view.findViewById(R.id.songTitle)
        playPauseButton = view.findViewById(R.id.playPauseButton)
        recyclerView = view.findViewById(R.id.favoriteRecycler)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteContent = EchoDatabase(myActivity)
        getSongsList = getSongsFromPhone()
        if (getSongsList == null){
            recyclerView?.visibility = View.INVISIBLE
            noFavorites?.visibility = View.VISIBLE
        }else{
            var favoriteAdaptor = FavoriteAdaptor(getSongsList as ArrayList<Songs>,myActivity as Context)
            var mLayoutManager = LinearLayoutManager(activity)
            recyclerView?.layoutManager = mLayoutManager
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.adapter = favoriteAdaptor
            recyclerView?.setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        var item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
    }

    fun getSongsFromPhone(): ArrayList<Songs> {
        var arrayList = ArrayList<Songs>()
        var contentResolver = myActivity?.contentResolver
        var songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCurser = contentResolver?.query(songUri, null, null, null, null)
        if (songCurser != null && songCurser.moveToFirst()) {
            var songId = songCurser.getColumnIndex(MediaStore.Audio.Media._ID)
            var songTitle = songCurser.getColumnIndex(MediaStore.Audio.Media.TITLE)
            var songArtist = songCurser.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            var songData = songCurser.getColumnIndex(MediaStore.Audio.Media.DATA)
            var dateIndex = songCurser.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songCurser.moveToNext()) {
                var currentId = songCurser.getLong(songId)
                var currentTitle = songCurser.getString((songTitle))
                var currentArtist = songCurser.getString(songArtist)
                var currentData = songCurser.getString(songData)
                var currentDateIndex = songCurser.getLong(dateIndex)
                arrayList.add(Songs(currentId, currentTitle, currentArtist, currentData, currentDateIndex))
            }


        }
        return arrayList
    }

    fun bottomBarSetup(){
        try {
            bottomBarClickHandler()
            songTilte?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            SongPlayingFragment.Statified.mediaPlayer?.setOnCompletionListener ({
                songTilte?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
                SongPlayingFragment.Staticated.onSongComplete()
            })
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                nowPlayingButton?.visibility = View.VISIBLE
            }else{
                nowPlayingButton?.visibility = View.INVISIBLE
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    fun bottomBarClickHandler(){
        nowPlayingButton?.setOnClickListener({
            Statified.mediaPlayer = SongPlayingFragment.Statified.mediaPlayer
            var songPlayingFragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist", SongPlayingFragment.Statified.currentSongHelper?.songArtist)
            args.putString("path", SongPlayingFragment.Statified.currentSongHelper?.songPath)
            args.putString("songTitle", SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            args.putInt("songID", SongPlayingFragment.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("songPosition", SongPlayingFragment.Statified.currentSongHelper?.currentPosition?.toInt() as Int)
            args.putParcelableArrayList("songData", SongPlayingFragment.Statified.fetchSongs)
            args.putString("FavBottomBar","success")
            songPlayingFragment.arguments = args
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_fragment,songPlayingFragment)
                    .addToBackStack("SongPlayingFragment")
                    .commit()
        })
        playPauseButton?.setOnClickListener({
            if (SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean ){
                SongPlayingFragment.Statified.mediaPlayer?.pause()
                trackPosition = SongPlayingFragment.Statified.mediaPlayer?.getCurrentPosition() as Int
                playPauseButton?.setBackgroundResource((R.drawable.play_icon))
            }else{
                SongPlayingFragment.Statified.mediaPlayer?.seekTo(trackPosition)
                SongPlayingFragment.Statified.mediaPlayer?.start()
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }

    fun display_favorites_by_searching(){
        if(favoriteContent?.checkSize() as Int > 0){
            refreshList = ArrayList<Songs>()
            getListFromDatabase = favoriteContent?.queryDBList()
            var fetchListfromDevice = getSongsFromPhone()
            if (fetchListfromDevice != null){
                for (i in 0..fetchListfromDevice?.size - 1){
                    for (j in 0..getListFromDatabase?.size as Int - 1){
                        if ((getListFromDatabase?.get(j)?.songID) === (fetchListfromDevice?.get(i)?.songID)){
                            refreshList?.add((getListFromDatabase as ArrayList<Songs>)[j])
                        }
                    }
                }
            }else{

            }

        }
    }
}// Required empty public constructor
