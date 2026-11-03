package dev.versatia.example.values

import dev.versatia.config.ValueMapper
import dev.versatia.util.config.getConfigurationSectionOrThrow
import dev.versatia.util.config.getIntOrThrow
import dev.versatia.util.config.getLongOrThrow
import dev.versatia.util.validation.validateAtLeast
import org.bukkit.configuration.ConfigurationSection

/** A mapper converts a value the runtime would read as one type into another: seconds in the file, ticks in the code. */
object TicksFromSeconds : ValueMapper<Long> {
    override fun map(section: ConfigurationSection, key: String) = section.getLongOrThrow(key).validateAtLeast(0) * 20
}

/** A mapper also reads a type of your own from a section, with the strict getters and their messages. */
data class PriceRange(val min: Int, val max: Int) {
    override fun toString() = "$min-$max"

    object Mapper : ValueMapper<PriceRange> {
        override fun map(section: ConfigurationSection, key: String) = section.getConfigurationSectionOrThrow(key).let {
            val min = it.getIntOrThrow("min")
            PriceRange(min, it.getIntOrThrow("max").validateAtLeast(min))
        }
    }
}
