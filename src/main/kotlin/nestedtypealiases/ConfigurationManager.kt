package org.kotlin.nestedtypealiases

class ConfigurationManager {
    typealias ConfigKey = String
    typealias ConfigValue = Any?
    typealias ConfigSection = Map<ConfigKey, ConfigValue>
    typealias ConfigValidator = (ConfigValue) -> Boolean
    typealias ConfigTransformer = (ConfigValue) -> ConfigValue

    private val configuration = mutableMapOf<ConfigKey, ConfigValue>()
    private val validators = mutableMapOf<ConfigKey, ConfigValidator>()
    private val transformers = mutableMapOf<ConfigKey, ConfigTransformer>()

    fun setValidator(key: ConfigKey, validator: ConfigValidator) {
        validators[key] = validator
    }

    fun setTransformer(key: ConfigKey, transformer: ConfigTransformer) {
        transformers[key] = transformer
    }

    fun set(key: ConfigKey, value: ConfigValue): Boolean {
        val transformedValue = transformers[key]?.invoke(value) ?: value

        val validator = validators[key]
        if (validator != null && !validator(transformedValue)) {
            println("Validation failed for key: $key")
            return false
        }

        configuration[key] = transformedValue
        return true
    }

    fun get(key: ConfigKey): ConfigValue = configuration[key]

    fun getSection(prefix: String): ConfigSection {
        return configuration.filterKeys { it.startsWith(prefix) }
    }

    fun loadSection(section: ConfigSection): Int {
        var loadedCount = 0
        section.forEach { (key, value) ->
            if (set(key, value)) {
                loadedCount++
            }
        }
        return loadedCount
    }
}