package com.helpet


import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("result")
    val result: Result? = null
){
    data class Result(
        @SerializedName("asymptomaticProbability")
        val asymptomaticProbability: Double,
        @SerializedName("name")
        val name: String,
        @SerializedName("symptomProbability")
        val symptomProbability: Double
    )
}