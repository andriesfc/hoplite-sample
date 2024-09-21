package demo.hoplite

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.ExperimentalHoplite
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.sources.ConfigFilePropertySource
import java.io.File

data class AppConfig(
    val elasticsearch: ElasticSearch,
) {
    data class ElasticSearch(
        val host: String,
        val port: Int,
        val clusterName: String,
    )

    enum class Env(val envId: String) {
        PROD("prod"),
        DEV("dev"),
        DEMO("demo"),
        ;

        companion object {
            @JvmStatic
            fun named(envString: String): Env? = entries.firstOrNull { it.envId.equals(envString, ignoreCase = true) }
        }
    }


    companion object {

        val userDir: File by lazy { File(System.getProperty("user.dir")).canonicalFile }
        val defaultConfigSource: ConfigFilePropertySource = PropertySource.resource("/conf/defaults.yaml")

        @JvmStatic
        fun envConfigSource(
            env: String?,
            workDir: File = userDir,
        ): ConfigFilePropertySource? =
            env?.let { requireNotNull(Env.named(it)) { "unsupported env name: $it. (following are supported: ${Env.entries.joinToString { it.envId }})" } }
                ?.let { workDir.resolve("conf/application-${it.envId}.yaml") }
                ?.let { PropertySource.file(it, optional = true, allowEmpty = true) }

        @OptIn(ExperimentalHoplite::class)
        @JvmStatic
        fun get(env: String?, workDir: File = userDir) = ConfigLoaderBuilder.default()
            .withExplicitSealedTypes()
            .addPropertySource(defaultConfigSource)
            .let { loader -> envConfigSource(env, workDir)?.let { loader.addSource(it) } ?: loader }
            .build()
            .loadConfigOrThrow<AppConfig>()
    }
}
