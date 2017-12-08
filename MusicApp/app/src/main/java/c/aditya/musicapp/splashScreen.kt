package c.aditya.musicapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import java.security.KeyStore

class splashScreen : AppCompatActivity() {

    var hasAllPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (!checkPermissions(this@splashScreen, *hasAllPermissions)){

            ActivityCompat.requestPermissions(this@splashScreen, hasAllPermissions,131)

        }else{
            Handler().postDelayed({
                var start = Intent(this@splashScreen, MainActivity::class.java)
                startActivity(start)
                this.finish()
            },1000)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            131 ->{if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED){

                Handler().postDelayed({
                    var start = Intent(this@splashScreen, MainActivity::class.java)
                    startActivity(start)
                    this.finish()
                },1000)
            }
                else{
                Toast.makeText(this@splashScreen, "please grant all the permissions",Toast.LENGTH_SHORT).show()
                this.finish()
            }

                return
            }
            else->{
                Toast.makeText(this@splashScreen,"something went wrong",Toast.LENGTH_SHORT).show()
                this.finish()
                return
            }
        }

    }

    fun checkPermissions (context: Context, vararg permissions : String): Boolean {

        var hasPermissions = true
        for (permission in permissions){
            val ref = context.checkCallingOrSelfPermission(permission)
            if (ref != PackageManager.PERMISSION_GRANTED){
                hasPermissions = false
            }
        }
        return hasPermissions

    }

}
