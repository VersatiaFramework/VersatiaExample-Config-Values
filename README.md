# VersatiaExample-Config-Values

Properties marked `@Value` are set from YAML files in the data folder when the plugin starts and at every reload, built-in types directly and your own through a mapper. Nothing to read, nothing to reload by hand.

Part of the [Versatia examples](https://github.com/VersatiaFramework/VersatiaExamples); documentation: [Configuration values](https://versatiaframework.github.io/VersatiaDocs/docs/configuration).

**Start with** [`Shop.kt`](src/main/kotlin/com/github/marcoral/versatia/example/values/Shop.kt) and the mappers in [`Mappers.kt`](src/main/kotlin/com/github/marcoral/versatia/example/values/Mappers.kt), then [`Limits.kt`](src/main/kotlin/com/github/marcoral/versatia/example/values/Limits.kt) and [`ValuesPlugin.kt`](src/main/kotlin/com/github/marcoral/versatia/example/values/ValuesPlugin.kt); the files are under `src/main/resources/resources/`.

## What the log shows

```
[VersatiaExample-Config-Values] motd: Welcome to the values example
[VersatiaExample-Config-Values] Limits: 20 players, blocked: [BEDROCK, BARRIER]
[VersatiaExample-Config-Values] Shop: tax 8.0%, open 9-17, discounts {diamond=10.0%, bread=50.0%}, closed on no day, restock every 1800 ticks, prices 1-64
```

## Things to try

- Edit `plugins/VersatiaExample-Config-Values/config.yml` on the server, uncomment `closed-on`
  and run `/versatia reload values`: the three lines are logged again with the new values.
- Change `max-players` in `limits.yml` and reload: the line reads `25 players (was 20)`, because
  the property is a `PreviousValueTracking` and keeps the value it replaced.
- Set `tax: 8` (no percent sign) and reload: the command reports
  `Plugin VersatiaExample-Config-Values: ...Shop.tax could not be read from config.yml: Value at "shop.tax" is not a percent string ...`
  and the plugin keeps the values it had.
- Remove `motd` and reload: a non-nullable property without a value is an error; `closed-on` may
  be absent because `closedOn` is nullable.
- Set `price-range.max` below `min` and reload: the mapper's own check fails the reload with its message.
- Make `hours` public: the generated descriptor switches from `Setter.Reflective` to a direct
  assignment.

## Building

Requires JDK 21 and VersatiaStarter with what it exports (VersatiaAPI, the module APIs) in your
local Maven repository (`./gradlew publishToMavenLocal` in each checkout; see
[Building the framework](https://versatiaframework.github.io/VersatiaDocs/docs/contributing/building));
running it requires a Paper 1.21.4 server with VersatiaCore installed.

```bash
./gradlew build
```

To have the JAR copied to your server after every build, copy `local.properties.example` to
`local.properties` and set `pluginsDir`. The generated descriptor is in
`build/generated/ksp/main/kotlin`; open it to see exactly what the runtime executes.
