package com.yourapp.econ.data.repository

import com.yourapp.econ.data.remote.MindicadorService
import com.yourapp.econ.domain.entity.IndicatorPoint
import com.yourapp.econ.domain.entity.IndicatorValue
import com.yourapp.econ.domain.repository.RatesRepository
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(
    private val api: MindicadorService
) : RatesRepository {

    override suspend fun getLatest(base: String, symbols: List<String>): List<IndicatorValue> {
        require(base == "USD") { "Solo base USD soportada" }
        val day = api.daily()
        val dolarClp = day.dolar.valor ?: error("Dólar sin valor")
        val euroClp = day.euro.valor ?: error("Euro sin valor")

        val mapUsdBase = mapOf(
            "USD" to 1.0,
            "EUR" to (euroClp / dolarClp),
            "CLP" to (1.0 / dolarClp)
        )
        return symbols.mapNotNull { code -> mapUsdBase[code]?.let { IndicatorValue(code, it) } }
    }

    override suspend fun getTimeSeriesYear(
        base: String,
        symbols: List<String>,
        year: Int
    ): List<IndicatorPoint> {
        require(base == "USD") { "Solo base USD soportada" }
        require(year in 2000..LocalDate.now().year) { "Año inválido" }

        val dolarSeries = api.seriesByYear("dolar", year).serie
        val euroSeries = api.seriesByYear("euro", year).serie

        fun String.asLocalDate() = OffsetDateTime.parse(this).toLocalDate()
        val dByDate = dolarSeries.associate { it.fecha.asLocalDate() to it.valor }
        val eByDate = euroSeries.associate { it.fecha.asLocalDate() to it.valor }

        val start = LocalDate.of(year, 1, 1)
        val end = if (year == LocalDate.now().year) LocalDate.now() else LocalDate.of(year, 12, 31)

        val points = mutableListOf<IndicatorPoint>()
        var d = start
        while (!d.isAfter(end)) {
            val dClp = dByDate[d]
            val eClp = eByDate[d]
            if (dClp != null && eClp != null) {
                val map = buildMap {
                    if ("USD" in symbols) put("USD", 1.0)
                    if ("EUR" in symbols) put("EUR", eClp / dClp)
                    if ("CLP" in symbols) put("CLP", 1.0 / dClp)
                }
                points += IndicatorPoint(d, map)
            }
            d = d.plusDays(1)
        }
        return points.sortedBy { it.date }
    }

}
