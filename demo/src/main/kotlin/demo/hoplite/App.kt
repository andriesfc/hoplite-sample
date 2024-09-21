@file:JvmName("App")

package demo.hoplite

import com.github.ajalt.mordant.rendering.*
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.TextStyles.italic
import com.github.ajalt.mordant.table.Borders
import com.github.ajalt.mordant.table.grid
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import com.sksamuel.hoplite.*
import com.sksamuel.hoplite.sources.ConfigFilePropertySource
import java.io.File


@ExperimentalHoplite
fun main(args: Array<String>) {
    Terminal(AnsiLevel.TRUECOLOR).apply {
        val workDir =
            System.getenv("DEMO_CONF_DIR")
                ?.let(::File)
                ?: AppConfig.userDir.resolve("conf")
        val env = args.firstOrNull()
        val envOverrides = AppConfig.envConfigSource(env)
        val config = AppConfig.get(env, workDir)
        displaySources(envOverrides, env)
        displayConfig(config)
    }
}

private fun Terminal.displayConfig(config: AppConfig) {
    println(table {
        borderType = BorderType.ROUNDED
        borderStyle = rgb("#4b25b9")
        align = TextAlign.RIGHT
        header {
            style = rgb("#a32e81") + bold + italic
            align = TextAlign.CENTER
            row { cells("Setting", "Value") }
        }
        body {
            style = brightMagenta
            column(0) { align = TextAlign.RIGHT }
            column(1) { align = TextAlign.LEFT }
            listOf(
                "elasticsearch.host" to config.elasticsearch.host,
                "elasticsearch.port" to config.elasticsearch.port,
                "elasticsearch.clusterName" to config.elasticsearch.clusterName
            ).forEach { (key, value) -> row(key, value) }
        }
    })
}

private fun Terminal.displaySources(
    overrides: PropertySource?,
    env: String?,
) {

    val darkBlue = rgb("#3c577d")
    val veryWhite = rgb("#ffffff")
    val warningColor = rgb("#17a589")
    val leftColumn = (veryWhite + bold + italic) on darkBlue
    val rightColumn = veryWhite on darkBlue
    val rightColumnWarning = veryWhite on warningColor

    val info = listOf(
        leftColumn(" default: ") to AppConfig.defaultConfigSource.source().let { rightColumn(it) },
        leftColumn(" ${env ?: "emv"} (optional): ") to overrides?.source().let { overrideSource ->
            when (overrideSource) {
                null -> rightColumnWarning(" **NOT SET** ")
                else -> rightColumn(File(overrideSource).run {
                    val fileName = name
                    buildString {
                        append(fileName)
                        if (exists()) append(" ", (bold + italic + brightYellow)("(exists)  "))
                    }
                })
            }
        },
    )

    println(
        grid {
            cellBorders = Borders.NONE
            info.forEach { (note, noteText) ->
                row {
                    cell(note) { align = TextAlign.RIGHT }
                    cell(noteText) { align = TextAlign.LEFT }
                }
            }
        }
    )
}