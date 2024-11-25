package com.example.ble_advertiser_native

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ble_advertiser_native.ui.theme.Ble_advertiser_nativeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val bluetoothService : BluetoothService? = try {
//            BluetoothService(this)
//        }catch (e : Exception){
//            Log.e("SHARIAR", e.message.toString())
//            Toast.makeText(this@MainActivity,e.message.toString(), Toast.LENGTH_SHORT).show()
//            null
//        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val bluetoothService = BluetoothService(this@MainActivity)

        setContent {
            Ble_advertiser_nativeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Button(onClick = {
                        bluetoothService?.startAdvertising()
                    }, modifier = Modifier.padding(innerPadding)) {

                        Text(text = "Start")

                    }
                }
            }
        }
    }
}