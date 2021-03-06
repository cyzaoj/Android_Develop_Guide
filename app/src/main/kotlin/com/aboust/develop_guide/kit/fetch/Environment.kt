package com.aboust.develop_guide.kit.fetch

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

data class Environment(val name: String, val hosts: MutableMap<String, String> = HashMap()) {

    internal fun encode(): String = "$name$SPLIT${JSONObject(hosts.toMap())}"

    fun create(context: Context, vararg environments: Environment) {
        val filteredEnvironments = environments.distinctBy { it.name }.sortedBy { it.name }

        //已保存选择
        val savedPreferences = getSharedPreferences(context)
        val selectedEnvironment = savedPreferences.getSelectedEnvironment()
        savedPreferences.edit().clear().apply()

        if (selectedEnvironment in filteredEnvironments) {
            savedPreferences.saveSelectedEnvironment(selectedEnvironment)
        }
        for (environment in filteredEnvironments) {
            savedPreferences.saveEnvironment(environment)
        }
    }


    companion object {
        private const val PREFERENCES_KEY = "Environment"
        private const val PREFERENCES_KEY_SELECTED = "Selected_Environment"
        private const val SPLIT = '|'
        internal val None = Environment("None")

        private fun decode(value: String): Environment? {
            val name = value.substringBeforeLast(SPLIT)
            val valueString = value.substringAfterLast(SPLIT)
            val hosts: MutableMap<String, String> = HashMap()
            val jsonObject = JSONObject(valueString)
            jsonObject.keys().forEach {
                hosts[it] = jsonObject.getString(it)
            }
            return if (name == value) null else Environment(name, hosts)
        }

        internal fun SharedPreferences.getAllEnvironments(): List<Environment> {
            return listOf(None) + all.entries
                    .asSequence()
                    .map { (_, value) -> value }
                    .filterIsInstance<String>()
                    .mapNotNull(Companion::decode)
                    .filter { it != None }
                    .distinct()
                    .sortedBy { it.name }
                    .toList()
        }

        internal fun SharedPreferences.saveEnvironment(environment: Environment) {
            val encoded = environment.encode()
            edit().putString(environment.name, encoded).apply()
        }

        fun getSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)

        internal fun SharedPreferences.saveSelectedEnvironment(environment: Environment) = edit().putString(PREFERENCES_KEY_SELECTED, environment.encode()).apply()

        internal fun SharedPreferences.getSelectedEnvironment(): Environment = getString(PREFERENCES_KEY_SELECTED, null)?.run(Companion::decode)
                ?: None
    }

}



