package com.yourapp.econ.data.remote

import com.yourapp.econ.data.model.DailyDto
import com.yourapp.econ.data.model.IndicatorSeriesDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MindicadorService {
    @GET("api")
    suspend fun daily(): DailyDto

    @GET("api/{indicator}/{year}")
    suspend fun seriesByYear(
        @Path("indicator") indicator: String,
        @Path("year") year: Int
    ): IndicatorSeriesDto
}





