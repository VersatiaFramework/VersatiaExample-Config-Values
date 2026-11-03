package dev.versatia.example.values

import dev.versatia.Phase
import dev.versatia.FriendlyName
import dev.versatia.VersatiaPlugin
import dev.versatia.config.Value
import dev.versatia.lifecycle.AutoInvoke
import dev.versatia.util.type.Secret

/** The values are set before the function runs, at start and at every `/versatia reload values`. */
@FriendlyName("values")
class ValuesPlugin : VersatiaPlugin() {
    @Value("motd")
    lateinit var motd: String

    /** Prints as ****; `apiKey.reveal()` hands the key to the call that needs it, and an empty key switches the API off. */
    @Value("api-key")
    var apiKey = Secret.EMPTY

    @AutoInvoke(Phase.PLUGIN_RELOAD_OR_START)
    fun report(shop: Shop) {
        logger.info("motd: $motd")
        logger.info(if (apiKey.isEmpty()) "API key: none, the API is off" else "API key: $apiKey")
        logger.info("Limits: ${Limits.describe()}")
        logger.info("Shop: ${shop.describe()}")
    }
}
