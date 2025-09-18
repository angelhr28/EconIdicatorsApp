package com.yourapp.econ.data.model

data class IndicatorSeriesDto(
    val version: String?,
    val autor: String?,
    val codigo: String,
    val nombre: String,
    val unidad_medida: String,
    val serie: List<SerieItemDto>
)