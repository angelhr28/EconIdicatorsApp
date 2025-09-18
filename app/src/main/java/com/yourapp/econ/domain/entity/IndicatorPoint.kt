package com.yourapp.econ.domain.entity

import java.time.LocalDate

data class IndicatorPoint(val date: LocalDate, val values: Map<String, Double>)
