package com.helpet

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.helpet.databinding.ActivityVectorCameraBinding
import kotlinx.android.synthetic.main.activity_vector_camera.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class VectorCamera : BaseActivity() {

    val PERM_STORAGE= 9
    val PERM_CAMERA= 10
    val REQ_CAMERA=11

    val binding by lazy { ActivityVectorCameraBinding.inflate(LayoutInflater.from(applicationContext)) }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)


    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun initViews(){
        binding.cameraBtn.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),PERM_CAMERA)
        }

    }
    //이미지의 실제 주소 가져올 변수 선언
    var realUri: Uri? = null
     fun openCamera() {
        //카메라 권한이 있는지 확인
        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri(newFileName(),"image/jpeg")?.let{uri->
            realUri=uri
            Log.d(realUri.toString(), "openCamera: opencamera")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            getRealPathFromURI(realUri!!)

        }

        startActivityForResult(intent, REQ_CAMERA)
    }
    private fun getRealPathFromURI(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")) {
            return uri.path.toString()
        }

        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var cursor = contentResolver.query(uri, proj, null, null, null)

        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        var result= cursor.getString(columnIndex)

        Log.d(cursor.getString(columnIndex), "getRealPathFromURI")
        return result!!

    }



    fun createImageUri(filename: String, mimeType: String): Uri?{
        val values=ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,mimeType)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
    }

    fun newFileName():String{
        val sdf=SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename=sdf.format(System.currentTimeMillis())
        Log.d(filename, "newFileName: filename")
        return "${filename}.jpeg"
    }

    fun loadBitmap(photoUri:Uri):Bitmap?{
        try {
            return if (Build.VERSION.SDK_INT>Build.VERSION_CODES.O_MR1){
                val source= ImageDecoder.createSource(contentResolver,photoUri)
                ImageDecoder.decodeBitmap(source)
            }
            else{
                MediaStore.Images.Media.getBitmap(contentResolver,photoUri)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    return null
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun permissionGranted(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE -> initViews()
            PERM_CAMERA->openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE->{
                Toast.makeText(this,"공용저장소 권한을 승인해야 사용할 수 있습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA-> Toast.makeText(this,"카메라 권한을 승인해야 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
            when(requestCode){
                REQ_CAMERA->{
//                    val bitmap=data?.extras?.get("data") as Bitmap
//                    binding.cameraImg.setImageBitmap(bitmap)
                    realUri?.let { uri ->
                        val bitmap=loadBitmap(uri)
                        binding.cameraBtn.setImageBitmap(bitmap)
                    }
                    var filePath = getRealPathFromURI(realUri!!)
                    Log.d(filePath, "onActivityResult")
                    //아래 두줄이 중요하다!!!
                    var requestBody : RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(),filePath)
                    var body : MultipartBody.Part = createFormData("uploaded_file",filePath,requestBody)

                    camGuide2.setOnClickListener {
                        UpdatePhoto(body)
                    }

                }
            }
        }
    }

     private val server: VectorService by lazy {
        RetrofitApi.retrofit.create(VectorService::class.java)
    }


     fun UpdatePhoto(body: MultipartBody.Part) {
//        val file = getFileStreamPath(realUri!!.toString())
//
//
//            //아래 두줄이 중요하다!!!
//        var requestBody : RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(),file)
//        var body : MultipartBody.Part = createFormData("uploaded_file",file.name,requestBody)


        server.vectorResult(body).enqueue(object : Callback<ResponseDto?> {
            override fun onResponse(call: Call<ResponseDto?>?, response: Response<ResponseDto?>) {
                Toast.makeText(applicationContext, "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                Log.d("레트로핏 결과2",""+response?.body().toString())
            }

            override fun onFailure(call: Call<ResponseDto?>?, t: Throwable) {
                Toast.makeText(applicationContext, "통신 실패", Toast.LENGTH_SHORT).show()
                Log.d("에러", t.message!!)
            }
        })
    }
}




