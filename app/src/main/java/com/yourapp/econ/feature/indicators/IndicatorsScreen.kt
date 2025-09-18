package com.yourapp.econ.feature.indicators

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourapp.econ.app.ui.theme.EconTheme
import com.yourapp.econ.domain.entity.IndicatorPoint
import com.yourapp.econ.domain.entity.IndicatorValue
import com.yourapp.econ.feature.indicators.state.IndState
import java.time.LocalDate
import kotlin.math.max

@Composable
fun IndicatorsScreen(
    onLogout: () -> Unit,
    vm: IndicatorsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    LaunchedEffect(Unit) { vm.onLoad() }

    IndicatorsScaffold(
        state = state,
        onRefresh = vm::onRefresh,
        onLogout = {
            vm.onLogout()
            onLogout()
        },
        onYearChange = vm::onChangeYear
    )
}

/* ------------ Scaffold ------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndicatorsScaffold(
    state: IndState,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onYearChange: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📈 Indicadores – base USD") },
                actions = { TextButton(onClick = onLogout) { Text("Cerrar sesión") } }
            )
        }
    ) { padding ->
        IndicatorsContent(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            state = state,
            onRefresh = onRefresh,
            onYearChange = onYearChange
        )
    }
}

/* ------------ Content ------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IndicatorsContent(
    modifier: Modifier,
    state: IndState,
    onRefresh: () -> Unit,
    onYearChange: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Selector de Año (combo)
        YearSelector(
            selectedYear = state.selectedYear,
            onYearChange = onYearChange,
            years = remember { buildYearList() }
        )

        Spacer(Modifier.height(12.dp))

        state.error?.let { msg ->
            AssistChip(onClick = onRefresh, label = { Text("Ups, no pudimos actualizar: $msg") })
            Spacer(Modifier.height(8.dp))
        }

        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
        }

        val headerDate = state.selectedYear
        Text("Valores al $headerDate", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (state.todayValues.isEmpty()) {
            Text(
                "No hay datos para el año seleccionado. Toca “Reintentar” para intentarlo nuevamente.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRefresh) { Text("Reintentar") }
        } else {
            state.todayValues.forEach { Text("• ${it.code}: ${"%.4f".format(it.value)}") }
        }

        Spacer(Modifier.height(20.dp))
        Text("Fluctuación en ${state.selectedYear}", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        IndicatorsLegend(symbols = state.symbols)
        Spacer(Modifier.height(8.dp))
        IndicatorsChart(points = state.series, symbols = state.symbols)

        Spacer(Modifier.height(12.dp))
        OutlinedButton(onClick = onRefresh) { Text("Actualizar ahora") }
    }
}

/* ------------ Year Selector (combo) ------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearSelector(
    selectedYear: Int,
    onYearChange: (Int) -> Unit,
    years: List<Int>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = selectedYear.toString(),
            onValueChange = {},
            label = { Text("Año") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            years.forEach { year ->
                DropdownMenuItem(
                    text = { Text(year.toString()) },
                    onClick = {
                        expanded = false
                        if (year != selectedYear) onYearChange(year)
                    }
                )
            }
        }
    }
}

private fun buildYearList(
    countBack: Int = 30,
    includeNext: Boolean = false
): List<Int> {
    val current = LocalDate.now().year
    val start = if (includeNext) current + 1 else current
    return (start downTo (current - countBack)).toList()
}

/* ------------ Leyenda y Chart ------------ */

@Composable
private fun IndicatorsLegend(symbols: List<String>) {
    if (symbols.isEmpty()) return
    val palette = defaultPalette()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        symbols.forEachIndexed { index, code ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color = palette[index % palette.size], shape = CircleShape)
                )
                Spacer(Modifier.width(6.dp))
                Text(code, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun IndicatorsChart(
    points: List<IndicatorPoint>,
    symbols: List<String>
) {
    if (points.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) { Text("Sin datos de serie temporal.") }
        return
    }

    val palette = defaultPalette()

    Canvas(
        Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        val xCount = max(1, points.size - 1)
        val xStep = size.width / xCount

        fun drawSeries(code: String, color: Color) {
            val ys = points.map { it.values[code] ?: 0.0 }
            val minY = ys.minOrNull() ?: 0.0
            val maxY = ys.maxOrNull() ?: 1.0
            val denom = (maxY - minY).let { if (it == 0.0) 1.0 else it }
            val norm = { v: Double -> ((v - minY) / denom).toFloat() }

            var prev: Offset? = null
            ys.forEachIndexed { i, v ->
                val x = i * xStep
                val y = size.height * (1f - norm(v))
                val p = Offset(x, y)
                prev?.let { pr ->
                    drawLine(color = color, start = pr, end = p, strokeWidth = 3f)
                }
                prev = p
            }
        }
        symbols.forEachIndexed { idx, code -> drawSeries(code, palette[idx % palette.size]) }
    }
}

@Composable
private fun defaultPalette() = listOf(
    Color(0xFF1E88E5), // azul
    Color(0xFFD81B60), // rosa
    Color(0xFF43A047), // verde
    Color(0xFFF4511E), // naranja
    Color(0xFF8E24AA), // púrpura
    Color(0xFF00897B)  // teal
)

/* ------------ Previews ------------ */

@Preview(name = "Indicadores - Light", showBackground = true, widthDp = 380)
@Composable
private fun Preview_Indicators_Light() {
    EconTheme { IndicatorsScaffoldPreviewHost() }
}

@Preview(
    name = "Indicadores - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 380
)
@Composable
private fun Preview_Indicators_Dark() {
    EconTheme { IndicatorsScaffoldPreviewHost() }
}

@Composable
private fun IndicatorsScaffoldPreviewHost() {
    val symbols = listOf("USD", "EUR", "CLP")
    val start = LocalDate.of(LocalDate.now().year, 1, 1)
    val series = buildList {
        repeat(60) { i ->
            val d = start.plusDays(i.toLong())
            add(
                IndicatorPoint(
                    date = d,
                    values = mapOf(
                        "USD" to 1.0,
                        "EUR" to (0.9 + (i % 10) * 0.003),
                        "CLP" to (900.0 + (i % 15) * 3.5)
                    )
                )
            )
        }
    }

    val today = listOf(
        IndicatorValue("USD", 1.0000),
        IndicatorValue("EUR", 0.9250),
        IndicatorValue("CLP", 930.50)
    )

    val previewState = IndState(
        isLoading = false,
        base = "USD",
        symbols = symbols,
        selectedYear = LocalDate.now().year,
        todayValues = today,
        series = series,
        error = null
    )

    IndicatorsScaffold(
        state = previewState,
        onRefresh = {},
        onLogout = {},
        onYearChange = {}
    )
}
