package com.github.marcoral.versatia.example.values

import com.github.marcoral.versatia.config.PreviousValueTracking
import com.github.marcoral.versatia.config.Value
import org.bukkit.Material

/** Values of an object need no bean; these come from a second file of the data folder. */
object Limits {
    /** A tracked value keeps the one it replaced, so a reload can tell what changed. */
    @Value("max-players", file = "limits.yml")
    val maxPlayers = PreviousValueTracking<Int>()

    @Value("blocked", file = "limits.yml")
    var blocked: List<Material> = emptyList()

    fun describe() = "${maxPlayers.value} players${if (maxPlayers.hasPrevious && maxPlayers.changed) " (was ${maxPlayers.previous})" else ""}, blocked: $blocked"
}
