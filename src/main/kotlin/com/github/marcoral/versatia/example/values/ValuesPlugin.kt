package com.github.marcoral.versatia.example.values

import com.github.marcoral.versatia.EventType
import com.github.marcoral.versatia.FriendlyName
import com.github.marcoral.versatia.VersatiaPlugin
import com.github.marcoral.versatia.config.Value
import com.github.marcoral.versatia.lifecycle.AutoInvoke

/** The values are set before the function runs, at start and at every `/versatia reload values`. */
@FriendlyName("values")
class ValuesPlugin : VersatiaPlugin() {
    @Value("motd")
    lateinit var motd: String

    @AutoInvoke(EventType.PLUGIN_RELOAD_OR_START)
    fun report(shop: Shop) {
        logger.info("motd: $motd")
        logger.info("Limits: ${Limits.describe()}")
        logger.info("Shop: ${shop.describe()}")
    }
}
