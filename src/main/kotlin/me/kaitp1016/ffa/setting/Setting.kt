package me.kaitp1016.ffa.setting

abstract class Setting<T> {
    abstract val name: String
    abstract val description: String
    abstract val default: T

    abstract fun getValue():T
    abstract fun setValue(value:T)
    abstract fun parse(text:String):T?

    fun parseAndSet(text: String): String? {
        val value = parse(text) ?: return "その方式は対応していません!"
        try {
            setValue(value)
        }
        catch (exception: IllegalArgumentException) {
            return exception.message
        }

        return null
    }

    fun reset() {
        this.setValue(default)
    }
}