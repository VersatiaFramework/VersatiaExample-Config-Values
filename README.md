# VersatiaExample-Config-Values

Properties marked `@Value` are set from YAML files in the data folder when the plugin starts and at every reload, built-in types directly and your own through a mapper. Nothing to read, nothing to reload by hand.

Part of the [Versatia examples](https://github.com/VersatiaFramework/VersatiaExamples); documentation: [Configuration values](https://versatia.dev/docs/configuration).

**Start with** [`Shop.kt`](src/main/kotlin/dev/versatia/example/values/Shop.kt) and the mappers in [`Mappers.kt`](src/main/kotlin/dev/versatia/example/values/Mappers.kt), then [`Limits.kt`](src/main/kotlin/dev/versatia/example/values/Limits.kt) and [`ValuesPlugin.kt`](src/main/kotlin/dev/versatia/example/values/ValuesPlugin.kt); the files are under `src/main/resources/resources/`.

## What the log shows

```
[VersatiaExample-Config-Values] motd: Welcome to the values example
[VersatiaExample-Config-Values] API key: none, the API is off
[VersatiaExample-Config-Values] Limits: 20 players, blocked: [BEDROCK, BARRIER]
[VersatiaExample-Config-Values] Shop: tax 8.0%, open 9-17, discounts {diamond=10.0%, bread=50.0%}, closed on no day, restock every 1800 ticks, prices 1-64
```

## Things to try

- Edit `plugins/VersatiaExample-Config-Values/config.yml` on the server, uncomment `closed-on`
  and run `/versatia reload values`: the three lines are logged again with the new values.
- Change `max-players` in `limits.yml` to `25` and reload: the line reads `25 players (was 20)`,
  because the property is a `PreviousValueTracking` and keeps the value it replaced; a reload that
  leaves it unchanged prints no `(was ...)`.
- Set `tax: 8` (no percent sign) and reload: the command reports
  `VersatiaExample-Config-Values failed to reload: ...Shop.tax could not be read from config.yml: Value at "shop.tax" is not a percent string ...`
  and the plugin keeps the values it had.
- Remove `motd` and reload: a non-nullable property without a value is an error; `closed-on` may
  be absent because `closedOn` is nullable.
- Set `api-key: abc123` and reload: the log shows `API key: ****`. Set `api-key: 0123` and reload:
  the reload fails asking for quotes, since YAML reads `0123` as the number 83; `'0123'` works.
  Set `api-key: ${env:VALUES_API_KEY}` and reload without that environment variable: the reload
  fails with `...ValuesPlugin.apiKey could not be read from config.yml: Value at "api-key" uses ${env:VALUES_API_KEY}, which is not set; ...`.
  Write a key into `plugins/VersatiaExample-Config-Values/api-key.txt`, set
  `api-key: ${pluginfile:api-key.txt}` and reload: the log shows `API key: ****`, never the key.
  `${file:plugins/VersatiaExample-Config-Values/api-key.txt}` reads the same file from the server
  folder, and an absolute path works too (`${file:/etc/minecraft/api-key.txt}`, on Windows
  `${file:C:/secrets/api-key.txt}`). Rename the file and reload: the reload fails with
  `... uses ${pluginfile:api-key.txt}, but <path> does not exist`.
- Set `price-range.max` below `min` and reload: the mapper's own check fails the reload with its message.
- Make `hours` public: the generated descriptor switches from `Setter.Reflective` to a direct
  assignment.

## Building

Requires JDK 25 and VersatiaAPI in your
local Maven repository (`./gradlew publishToMavenLocal` in each checkout; see
[Building the framework](https://versatia.dev/docs/contributing/building));
running it requires a Paper 26.3 server with VersatiaCore installed, and the
[CommandAPI](https://commandapi.jorel.dev) 12.x plugin for `/versatia reload` (without it, a
plugin can still reload itself with `versatiaReload()`).

```bash
./gradlew build
```

To have the JAR copied to your server after every build, copy `local.properties.example` to
`local.properties` and set `pluginsDir`. The generated descriptor is in
`build/generated/ksp/main/kotlin`; open it to see exactly what the runtime executes.
