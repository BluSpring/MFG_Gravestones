package xyz.bluspring.mfg.gravestones.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import xyz.bluspring.mfg.gravestones.Init
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable

object InventoryStorage {
    val storage: GravestoneItems = try {
        Yaml.default.decodeFromString(GravestoneItems.serializer(), File(Init.plugin.dataFolder, "inventoryStorage.yml").readText())
        /*val items = this.load()
        this.writeToFile()

        items*/
    } catch (_: Exception) {
        GravestoneItems(players = mutableMapOf())
    }

    fun getPlayer(uuid: UUID): GravestoneItemsPlayer? {
        return if (!storage.players.containsKey(uuid.toString())) {
            storage.players[uuid.toString()]
        } else {
            storage.players[uuid.toString()] = GravestoneItemsPlayer(stones=mutableListOf())

            storage.players[uuid.toString()]
        }
    }

    fun writeToFile() {
        try {
            val file = File(Init.plugin.dataFolder, "inventoryStorage.yml")

            /*val baseJsonData = JsonObject()
            val actualJsonData = JsonObject()

            storage.players.forEach {
                val itemsPlayer = JsonObject()
                val stones = JsonArray()

                it.value.stones.forEach { itt ->
                    val stone = JsonObject()

                    stone.addProperty("username", itt.username)
                    stone.addProperty("deathReason", GsonComponentSerializer.gson().serialize(itt.deathReason))
                    stone.addProperty("deathTime", itt.deathTime)
                    stone.addProperty("experience", itt.experience)

                    val inventory = JsonArray()
                    itt.inventory.forEach { grItem ->
                        val item = JsonObject()

                        item.addProperty("id", grItem.id)
                        item.addProperty("count", grItem.count)

                        val byteArray = JsonArray()
                        grItem.nbtData?.forEach { bite ->
                            byteArray.add(bite)
                        }
                        if (grItem.nbtData != null)
                            item.add("nbtData", byteArray)

                        inventory.add(item)
                    }

                    stone.add("inventory", inventory)

                    val loc = JsonArray()
                    itt.location.forEach { locNum ->
                        loc.add(locNum)
                    }
                    stone.add("location", loc)

                    stone.addProperty("username", itt.username)
                    stone.addProperty("uuid", itt.uuid)
                    stone.addProperty("world", itt.world)

                    stones.add(stone)
                }

                itemsPlayer.add("stones", stones)

                actualJsonData.add(it.key, itemsPlayer)
            }

            baseJsonData.add("players", actualJsonData)*/

            //file.writeText(baseJsonData.toString())
            file.writeText(Yaml.default.encodeToString(GravestoneItems.serializer(), storage))
        } catch (e: Exception) {
            Init.plugin.logger.severe("An error occurred whilst writing storage to file!")
            e.printStackTrace()
        }
    }

    private fun load(): GravestoneItems {
        /*if (File(Init.plugin.dataFolder, "inventoryStorage.yml").exists()) {
            Init.plugin.logger.info("Detected old version YML file. Importing...")
            val oldInventoryStorage = File(Init.plugin.dataFolder, "inventoryStorage.yml")
            val oldConfig = YamlConfiguration.loadConfiguration(oldInventoryStorage)
            val configPlayers = oldConfig.getConfigurationSection("players")!!

            val newPlayers = mutableMapOf<String, GravestoneItemsPlayer>()
            configPlayers.getValues(false).forEach {
                val thePlayer = configPlayers.getConfigurationSection(it.key)!!
                val stones = mutableListOf<GravestoneStone>()

                thePlayer.getMapList("stones").forEach { itt ->
                    println(itt)
                    val stone = GravestoneStone(
                        (itt["inventory"] as List<MutableMap<String, Any>>).map { ittt ->
                            println(ittt)
                            GravestoneItem(
                                ittt["id"] as String,
                                ittt["count"] as Int,
                                if (ittt["nbtData"] == null) null else ittt["nbtData"] as ByteArray
                            )
                        },
                        itt["experience"] as Int,
                        itt["deathTime"] as Long,
                        LegacyComponentSerializer.legacySection().deserialize(itt["deathReason"] as String),
                        itt["location"] as List<Int>,
                        itt["uuid"] as String,
                        itt["username"] as String,
                        itt["world"] as String
                    )

                    println(stone)
                    stones.add(stone)
                }

                val newPlayer = GravestoneItemsPlayer(stones)

                newPlayers[it.key] = newPlayer
            }

            File(Init.plugin.dataFolder, "inventoryStorage.yml").renameTo(File(Init.plugin.dataFolder, "inventoryStorage-old.yml"))

            return GravestoneItems(newPlayers)
        }
        val json = JsonParser().parse(File(Init.plugin.dataFolder, "inventoryStorage.json").readText()).asJsonObject*/

        return GravestoneItems(players = mutableMapOf())
    }
}

@Serializable
data class GravestoneItems(
    val players: MutableMap<String, GravestoneItemsPlayer>
)

@Serializable
data class GravestoneItemsPlayer(
    val stones: MutableList<GravestoneStone>
)

@Serializable
data class GravestoneStone(
    val inventory: List<GravestoneItem>,
    val experience: Int,
    val deathTime: Long,
    val deathReason: String,
    var location: List<Int>,
    val uuid: String,
    val username: String,
    val world: String
)

@Serializable
data class GravestoneItem(
    val id: String,
    val count: Int,
    val nbtData: ByteArray? = null
)