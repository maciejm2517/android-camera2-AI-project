package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.FireBase.DataBaseRow
import com.example.myapplication.ml.SsdMobilenetV11Metadata1
import com.example.myapplication.room.Person
import com.example.myapplication.viewmodels.PeopleViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.util.Date
import java.util.Random

class MainActivity : AppCompatActivity() {

    lateinit var labels:List<String>
    var colors = listOf<Int>(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK,
        Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.RED)
    val paint = Paint()
    lateinit var imageProcessor: ImageProcessor
    lateinit var bitmap:Bitmap
    lateinit var imageView: ImageView
    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var model:SsdMobilenetV11Metadata1
    lateinit var button1:Button
    lateinit var button2:Button
    lateinit var button3:Button
    lateinit var button4:Button
    lateinit var button5:Button
    lateinit var button6:Button
    lateinit var text_label:TextView
    lateinit var text_label_name:TextView
    private lateinit var viewModel:PeopleViewModel
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        get_permission()

        viewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(PeopleViewModel::class.java)

        button1=findViewById(R.id.button1)
        button2=findViewById(R.id.button2)
        button3=findViewById(R.id.button3)
        button4=findViewById(R.id.button4)
        button5=findViewById(R.id.button5)
        button6=findViewById(R.id.button6)
        text_label_name=findViewById(R.id.labels_name)
        text_label=findViewById(R.id.labels)

        labels = FileUtil.loadLabels(this, "labels.txt")
        imageProcessor = ImageProcessor.Builder().add(ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR)).build()
        model = SsdMobilenetV11Metadata1.newInstance(this)

        val firebase=FirebaseDatabase.getInstance()
        myRef=firebase.getReference("ArrayData")

        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        imageView = findViewById(R.id.imageView)

        textureView = findViewById(R.id.textureView)
        button1.setOnClickListener(){
            textureView.visibility=View.INVISIBLE
            imageView.visibility=View.INVISIBLE
            text_label.visibility=View.VISIBLE
            text_label_name.visibility=View.VISIBLE
        }

        button2.setOnClickListener(){
            textureView.visibility=View.VISIBLE
            imageView.visibility=View.VISIBLE
            text_label.visibility=View.INVISIBLE
            text_label_name.visibility=View.INVISIBLE
        }
        button3.setOnClickListener(){
            val i = Intent(this,activity_2::class.java)
            startActivity(i)
        }
        var imie=""
        val sharedPreferences= this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        var language_pl=sharedPreferences.getBoolean("BOOLEAN_KEY",false)

        if (!language_pl) {
            labels = FileUtil.loadLabels(this, "labels_pl.txt")
            text_label.setText(R.string.labels_pl)
            button1.setText(R.string.close_camera_eng_pl)
            button3.setText(R.string.sql_pl)
            button2.setText(R.string.show_camera_eng_pl)
            button6.setText(R.string.settings_eng_pl)
            button4.setText(R.string.sql_del_pl)
            button5.setText(R.string.language_eng_pl)
            text_label_name.setText(R.string.labels_eng_pl)
            Toast.makeText(this, "Polski ustawiony", Toast.LENGTH_LONG).show()
        }
        else{
            labels = FileUtil.loadLabels(this, "labels.txt")
            text_label.setText(R.string.labels)
            button1.setText(R.string.close_camera_eng)
            button3.setText(R.string.sql)
            button2.setText(R.string.show_camera_eng)
            button6.setText(R.string.settings_eng)
            button4.setText(R.string.sql_del)
            button5.setText(R.string.language_eng)
            text_label_name.setText(R.string.labels_eng)
            Toast.makeText(this, "English set", Toast.LENGTH_LONG).show()
        }

        button4.setOnClickListener(){
            viewModel.deleteAllRows()
        }

        button5.setOnClickListener(){
            if (!language_pl) {
                labels = FileUtil.loadLabels(this, "labels_pl.txt")
                text_label.setText(R.string.labels_pl)
                button1.setText(R.string.close_camera_eng_pl)
                button3.setText(R.string.sql_pl)
                button2.setText(R.string.show_camera_eng_pl)
                button6.setText(R.string.settings_eng_pl)
                button4.setText(R.string.sql_del_pl)
                button5.setText(R.string.language_eng_pl)
                text_label_name.setText(R.string.labels_eng_pl)
                Toast.makeText(this, "Polski ustawiony", Toast.LENGTH_LONG).show()
            }
            else{
                labels = FileUtil.loadLabels(this, "labels.txt")
                text_label.setText(R.string.labels)
                button1.setText(R.string.close_camera_eng)
                button3.setText(R.string.sql)
                button2.setText(R.string.show_camera_eng)
                button6.setText(R.string.settings_eng)
                button4.setText(R.string.sql_del)
                button5.setText(R.string.language_eng)
                text_label_name.setText(R.string.labels_eng)
                Toast.makeText(this, "English set", Toast.LENGTH_LONG).show()
            }
            language_pl=!language_pl

            val sharedPreferences=getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply{
                putBoolean("BOOLEAN_KEY",language_pl)
            }.apply()

        }

        button6.setOnClickListener(){
            val sharedPreferences=getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply{
                putBoolean("BOOLEAN_KEY",language_pl)
            }.apply()

            val intent = Intent(this,second_activity::class.java).also {
                it.putExtra("EXTRA",language_pl)
                startActivity(it)
            }
        }

        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                open_camera()
            }
            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                bitmap = textureView.bitmap!!
                var image = TensorImage.fromBitmap(bitmap)
                image = imageProcessor.process(image)

                val outputs = model.process(image)
                val locations = outputs.locationsAsTensorBuffer.floatArray
                val classes = outputs.classesAsTensorBuffer.floatArray
                val scores = outputs.scoresAsTensorBuffer.floatArray
                val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer.floatArray

                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(mutable)

                val h = mutable.height
                val w = mutable.width
                paint.textSize = h/15f
                paint.strokeWidth = h/85f
                var x = 0
                scores.forEachIndexed { index, fl ->
                    x = index
                    x *= 4
                    if(fl > 0.6){
                        paint.setColor(colors.get(index))
                        paint.style = Paint.Style.STROKE
                        canvas.drawRect(RectF(locations.get(x+1)*w, locations.get(x)*h, locations.get(x+3)*w, locations.get(x+2)*h), paint)
                        paint.style = Paint.Style.FILL
                        var imie_new=labels.get(classes.get(index).toInt())
                        canvas.drawText(imie_new+" "+fl.toString(), locations.get(x+1)*w, locations.get(x)*h, paint)


                        //database
                        if (imie!=imie_new) {
                            imie = imie_new
                            val nazwisko = fl.toString()
                            val location =
                                "loc left: "+(locations.get(x+3)*w).toString() + " loc top:" + (locations.get(x)*h).toString()
                            val person = Person(imie, nazwisko, location)
                            viewModel.insertPerson(person)
                            val firebaseInput = DataBaseRow(imie, nazwisko, location)
                            myRef.child("${Date().time}__${Random().nextInt(10000)}")
                                .setValue(firebaseInput)

                        }


                    }
                }

                imageView.setImageBitmap(mutable)


            }
        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

    }

    override fun onDestroy() {
        super.onDestroy()
        model.close()
    }

    @SuppressLint("MissingPermission")
    fun open_camera(){
        cameraManager.openCamera(cameraManager.cameraIdList[0], object:CameraDevice.StateCallback(){
            override fun onOpened(p0: CameraDevice) {
                cameraDevice = p0

                var surfaceTexture = textureView.surfaceTexture
                var surface = Surface(surfaceTexture)

                var captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)

                cameraDevice.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(captureRequest.build(), null, null)
                    }
                    override fun onConfigureFailed(p0: CameraCaptureSession) {
                    }
                }, handler)
            }

            override fun onDisconnected(p0: CameraDevice) {
                cameraDevice = p0

                var surfaceTexture = textureView.surfaceTexture
                var surface = Surface(surfaceTexture)

                var captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)

                cameraDevice.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(captureRequest.build(), null, null)
                    }
                    override fun onConfigureFailed(p0: CameraCaptureSession) {
                    }
                }, handler)
            }

            override fun onError(p0: CameraDevice, p1: Int) {

            }
        }, handler)
    }

    fun get_permission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
            get_permission()
        }
    }

}