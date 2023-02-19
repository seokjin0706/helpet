package com.helpet

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink


class WriteViewModel:ViewModel() {

    //image는 잠시 뒤로 미뤄두고 우선 string들 코드설명부터 적어볼게요
    fun multipart(
        vector: String,
        asymptomaticProbability: String,
        name: String,
        symptomProbability: String,
        image: List<Bitmap>
    ) {
//        val vectorRequestBody = RequestBody.create(MediaType.parse("text/plain"), vector)
//        val asymptomaticProbabilityRequestBody =
//            RequestBody.create(MediaType.parse("text/plain"), asymptomaticProbability)
//        val nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name)
//        val symptomProbabilityRequestBody =
//            RequestBody.create(MediaType.parse("text/plain"), symptomProbability)

        val vectorRequestBody =
            vector.toRequestBody("text/plain".toMediaTypeOrNull())
        val asymptomaticProbabilityRequestBody =
            asymptomaticProbability.toRequestBody("text/plain".toMediaTypeOrNull())
        val nameRequestBody =
            name.toRequestBody("text/plain".toMediaTypeOrNull())
        val symptomProbabilityRequestBody =
            symptomProbability.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestBodyHashMap = HashMap<String, RequestBody>()
        requestBodyHashMap["vector"] = vectorRequestBody
        requestBodyHashMap["asymptomaticProbability"] = asymptomaticProbabilityRequestBody
        requestBodyHashMap["name"] = nameRequestBody
        requestBodyHashMap["symptomProbability"] = symptomProbabilityRequestBody
        for (i in image.indices) {
            val imageRequestBody = BitmapRequestBody(image[i])
            val imageMultipartBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("image","and" + System.currentTimeMillis().toString() + i.toString(),imageRequestBody )
            val imageListMultipartBody: ArrayList<MultipartBody.Part> = ArrayList()
            imageListMultipartBody.add(imageMultipartBody)
        }

    }

    companion object {
        class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
            override fun contentType(): MediaType? {
                return "image/jpeg".toMediaTypeOrNull()

            }

            override fun writeTo(sink: BufferedSink) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 99, sink.outputStream()) //99프로 압축
            }
        }

    }

}

