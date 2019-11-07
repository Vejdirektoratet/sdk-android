package dk.vejdirektoratet.vejdirektoratetsdkexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.vejdirektoratet.vejdirektoratetsdk.VejdirektoratetSDK
import dk.vejdirektoratet.vejdirektoratetsdk.requestTest

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        center_text.text = VejdirektoratetSDK.request("SDK", "test")
        below_text.text = requestTest()
    }
}
