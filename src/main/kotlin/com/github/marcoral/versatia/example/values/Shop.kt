package com.github.marcoral.versatia.example.values

import com.github.marcoral.versatia.config.Value
import com.github.marcoral.versatia.di.LocalBean
import com.github.marcoral.versatia.util.type.Percent
import java.time.DayOfWeek

/** Values of a bean, from `config.yml`: a scalar, a private list, a map, an optional enum and two mapped values. */
@LocalBean
class Shop {
    @Value("shop.tax")
    var tax = Percent.ZERO

    @Value("shop.hours")
    private var hours: List<Int> = emptyList()

    @Value("shop.discounts")
    var discounts: Map<String, Percent> = emptyMap()

    @Value("shop.closed-on")
    var closedOn: DayOfWeek? = null

    @Value("shop.restock-seconds", mapper = TicksFromSeconds::class)
    var restockTicks = 0L

    @Value("shop.price-range", mapper = PriceRange.Mapper::class)
    var priceRange = PriceRange(0, 0)

    fun describe() =
        "tax $tax, open ${hours.joinToString("-")}, discounts $discounts, closed on ${closedOn ?: "no day"}, restock every $restockTicks ticks, prices $priceRange"
}
