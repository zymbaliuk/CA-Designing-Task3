package com.zymbaliuk.hc05datasender

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zymbaliuk.hc05datasender.ui.theme.Hc05datasenderTheme
import java.io.InputStream
import java.io.OutputStream

private var outputStream: OutputStream? = null
private var inStream: InputStream? = null

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hc05datasenderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MessageInput()
                }
            }
        }
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
           1)
        val bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = bluetoothManager.adapter
        if (btAdapter != null) {
            if (btAdapter.isEnabled) {
                val bondedDevices = btAdapter.bondedDevices
                if (bondedDevices.size > 0) {
                    val devices = bondedDevices.toTypedArray() as Array<*>
                    val device = devices[0] as BluetoothDevice
                    val uuids = device.uuids
                    val socket = device.createRfcommSocketToServiceRecord(uuids[0].uuid)
                    socket.connect()
                    outputStream = socket.outputStream
                    inStream = socket.inputStream
                }
            }
        }
    }

    fun write(s: String) {
        outputStream?.write(s.toByteArray())
    }

    @Composable
    fun MessageInput() {
        var text by remember { mutableStateOf(TextFieldValue("")) }
        Column {
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                }
            )
            Button(onClick = {
                write(text.toString(),)
            }, ) {
                Text("Send")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Hc05datasenderTheme {
            MessageInput()
        }
    }
}