package com.example.ble_advertiser_native

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertisingSet
import android.bluetooth.le.AdvertisingSetCallback
import android.bluetooth.le.AdvertisingSetParameters
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

class BluetoothService(private val context: Context) {

    val LOG_TAG: String = "Shariar"

    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothLeAdvertiser: BluetoothLeAdvertiser? = null

    init {
        // Check if Bluetooth is available on the device and if BLE is supported
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Toast.makeText(context, "Bluetooth is not supported or not enabled.", Toast.LENGTH_SHORT).show()
            Log.e(LOG_TAG, "Bluetooth is not supported or not enabled.")
        } else {
            // Check for BLE advertising capability (API level 21 and above)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser
                if (bluetoothLeAdvertiser == null) {
                    Toast.makeText(context, "Bluetooth LE advertising is not supported on this device.", Toast.LENGTH_SHORT).show()

                    Log.e(LOG_TAG, "Bluetooth LE advertising is not supported on this device.")
                }
            } else {
                Log.e(
                    LOG_TAG,
                    "Bluetooth LE advertising is not supported on devices below API level 21."
                )

                Toast.makeText(context, "Bluetooth LE advertising is not supported on devices below API level 21.", Toast.LENGTH_SHORT).show()

            }
        }

    }

    fun startAdvertising() {
        // Check if Bluetooth LE Advertiser is available
        if (bluetoothLeAdvertiser == null) {
            Log.e(LOG_TAG, "Bluetooth LE Advertiser is not available.")
            Toast.makeText(context, "Bluetooth LE Advertiser is not available.", Toast.LENGTH_SHORT).show()

            return
        }

        // Check if the required permissions are granted
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Permissions not granted for Bluetooth advertising or location.", Toast.LENGTH_SHORT).show()

            Log.e(LOG_TAG, "Permissions not granted for Bluetooth advertising or location.")
            return
        }

        // Create advertising set parameters
        val parameters = AdvertisingSetParameters.Builder()
//            .setLegacyMode(true)
            .setConnectable(true)
            .setInterval(AdvertisingSetParameters.INTERVAL_HIGH)
            .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MEDIUM)
            .build()

        // Create advertising data
        val advertiseData = AdvertiseData.Builder()
            .addServiceUuid(ParcelUuid.fromString("0000abcd-0000-1000-8000-00805f9b34fb"))
            .setIncludeDeviceName(true)
            .build()

        // Start advertising set
        bluetoothLeAdvertiser?.startAdvertisingSet(
            parameters,
            advertiseData,
            null, // Scan response data, can be null
            null, // Advertising set status callback, can be null
            null, // Advertising set timeout, can be null
            callback
        )
    }

    // Callback to handle advertising events
    private val callback: AdvertisingSetCallback = object : AdvertisingSetCallback() {
        override fun onAdvertisingSetStarted(
            advertisingSet: AdvertisingSet,
            txPower: Int,
            status: Int
        ) {
            Toast.makeText(context, "onAdvertisingSetStarted(): txPower: $txPower , status: $status", Toast.LENGTH_SHORT).show()

            Log.i(LOG_TAG, "onAdvertisingSetStarted(): txPower: $txPower , status: $status")
        }

        override fun onAdvertisingDataSet(advertisingSet: AdvertisingSet, status: Int) {
            Log.i(LOG_TAG, "onAdvertisingDataSet(): status: $status")
            Toast.makeText(context, "onAdvertisingDataSet(): status: $status", Toast.LENGTH_SHORT).show()
        }

        override fun onScanResponseDataSet(advertisingSet: AdvertisingSet, status: Int) {
            Log.i(LOG_TAG, "onScanResponseDataSet(): status: $status")
        }

        override fun onAdvertisingSetStopped(advertisingSet: AdvertisingSet) {
            Log.i(LOG_TAG, "onAdvertisingSetStopped()")
        }
    }

    fun stopAdvertising() {
        if (bluetoothLeAdvertiser != null) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_ADVERTISE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            bluetoothLeAdvertiser?.stopAdvertisingSet(callback)
        } else {
            Log.e(LOG_TAG, "Bluetooth LE Advertiser is not available.")
        }
    }
}
