package demo.hoplite

import com.sksamuel.hoplite.sources.ConfigFilePropertySource
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AppConfigTest {

    val testConfDir = File("..").canonicalFile

    @Test
    fun demoEnvOverrides() {
        val expected = AppConfig(
            AppConfig.ElasticSearch(
                host = "demo.elasticsearch.scv",
                port = 8200,
                clusterName = "product-search"
            )
        )
        val actual = AppConfig.get("demo", testConfDir)
        assertEquals(expected, actual)
    }

    @Test
    fun demoEnvConfigExists() {
        val demo: ConfigFilePropertySource? = AppConfig.envConfigSource("demo", testConfDir)
        assertNotNull(demo)
        val path = Paths.get(demo.source()).toFile()
        println(path)
        assertTrue { path.exists() }
    }
}
