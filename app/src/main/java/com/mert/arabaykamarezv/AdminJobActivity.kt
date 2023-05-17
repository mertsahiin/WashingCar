package com.mert.arabaykamarezv

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.mert.arabaykamarezv.databinding.ActivityAdminJobBinding
import com.mert.arabaykamarezv.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.lang.Exception

class AdminJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminJobBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedBitmap : Bitmap? = null
    private  lateinit var database : SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminJobBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        database = this.openOrCreateDatabase("Jobs", MODE_PRIVATE,null)
        registerLauncher()

        val intent = intent
        val info = intent.getStringExtra("info")
        if(info.equals("new")){
            binding.txtwhatisJob.setText("")
            binding.txtJobMoney.setText("")

            binding.btnSave.visibility = View.VISIBLE
            binding.imageView.setImageResource(R.drawable.indir)
            val selectedImageBackground = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.indir)
            binding.imageView.setImageBitmap(selectedImageBackground)
        }else{
            binding.btnSave.visibility = View.INVISIBLE
            val selectedId = intent.getIntExtra("id", 1)
            val cursor = database.rawQuery("SELECT * FROM jobs WHERE id = ?" , arrayOf(selectedId.toString()))
            val JobNameIx = cursor.getColumnIndex("JobInfo")
            val JobMoneyIx = cursor.getColumnIndex("JobMoney")

            val imageIx = cursor.getColumnIndex("image")
            while(cursor.moveToNext()){
            binding.txtwhatisJob.setText(cursor.getString(JobNameIx))
                binding.txtJobMoney.setText(cursor.getString(JobMoneyIx))


                val ByteArray = cursor.getBlob(imageIx)
                val bitMap = BitmapFactory.decodeByteArray(ByteArray,0,ByteArray.size)
                binding.imageView.setImageBitmap(bitMap)
            }
            cursor.close()
        }
    }

    private fun smallerBitmap(image : Bitmap ,maximumSize : Int) : Bitmap{
        var height = image.height
        var width = image.width
        val bitmapRatio : Double =  width.toDouble() / height.toDouble()
        if(bitmapRatio > 1){
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        }else{
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }
    fun changeImage(view : View){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galeriye Ulaşmak İçin İzin Vermelisiniz", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver ",
                    View.OnClickListener {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
            }else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }
    fun saveJob(view : View){
        val JobMoney = binding.txtJobMoney.text.toString()
        val JobInfo = binding.txtwhatisJob.text.toString()
        if(selectedBitmap != null){
            val smallBitmap = smallerBitmap(selectedBitmap!!,300)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            try {
                //val database = this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS jobs(id INTEGER PRIMARY KEY,JobMoney NVARCHAR,JobInfo NVARCHAR,image BLOB)")
                val sqlString = "INSERT INTO jobs (JobMoney,JobInfo,image) VALUES (?,?,?) "
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,JobMoney)
                statement.bindString(2,JobInfo)
                statement.bindBlob(3,byteArray)
                statement.execute()
            }catch (e : Exception){
                e.printStackTrace()
            }
            val intent = Intent(this,AdminInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }


    }


    fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intentResultData = result.data
                if(intentResultData != null){
                    val imageData = intentResultData.data

                    if(imageData != null) {
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(contentResolver, imageData)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(selectedBitmap)

                            } else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageData)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }
}