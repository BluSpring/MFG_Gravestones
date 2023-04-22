package xyz.bluspring.mfg.gravestones.listeners

import xyz.bluspring.mfg.gravestones.Init
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import xyz.bluspring.mfg.gravestones.util.*
import kotlin.math.ceil
import kotlin.math.floor

object PlayerOnDeathListener : Listener {
    /*@EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val player = e.entity

        player.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}Your gravestone is located at ${ChatColor.DARK_RED}${player.location.blockX}, ${player.location.blockY}, ${player.location.blockZ}${ChatColor.RED}, in world ${ChatColor.DARK_RED}${player.world.name}")

        val inventory = mutableListOf<GravestoneItem>()

        if (e.drops.size == 0) return

        e.drops.forEach {
            val meta = it.itemMeta

            val ench = mutableMapOf<String, Int>()

            meta.enchants.forEach { et ->
                ench[et.key.key.toString()] = et.value
            }

            val storageInventory = mutableListOf<GravestoneItemStorage>()

            if (meta is BlockStateMeta) {
                if (meta.hasBlockState() && meta.blockState is ShulkerBox) {
                    val shulker = (meta.blockState as ShulkerBox).inventory

                    shulker.contents.forEach { ev ->
                        val otherEnch = mutableMapOf<String, Int>()

                        ev.itemMeta.enchants.forEach { ea ->
                            otherEnch[ea.key.key.toString()] = ea.value
                        }

                        val itemSto = GravestoneItemStorage(
                            id = ev.type.name,
                            amount = ev.amount,
                            position = shulker.contents.indexOf(ev),
                            itemMeta = GravestoneItemMeta(
                                isUnbreakable = ev.itemMeta.isUnbreakable,
                                displayName = ev.itemMeta.displayName,
                                enchants = otherEnch,
                                itemFlags = ev.itemMeta.itemFlags,
                                lore = ev.itemMeta.lore
                            ),
                            durability = if (ev.itemMeta is Damageable) (ev.itemMeta as Damageable).damage else null
                        )

                        storageInventory.add(itemSto)
                    }
                }
            }

            val nbt = GravestoneItemMeta(
                isUnbreakable = meta.isUnbreakable,
                displayName = meta.displayName,
                enchants = ench,
                itemFlags = meta.itemFlags,
                lore = meta.lore,
                storage = if (storageInventory.isNotEmpty()) storageInventory else null
            )

            inventory.add(
                GravestoneItem(
                    id = it.type.name,
                    count = it.amount,
                    nbtData = nbt,
                    durability = if (it.itemMeta is Damageable) {
                        val dmg = it.itemMeta as Damageable
                        dmg.damage
                    } else {
                        null
                    }
                )
            )
        }

        e.drops.removeAll(e.drops)

        val gravestoneLoc = findPos(player.location, player)

        val gravestoneBlock = player.world.getBlockAt(gravestoneLoc)

        gravestoneBlock.breakNaturally()

        gravestoneBlock.type = Material.BEACON

        val gravestone = GravestoneStone(
            inventory = inventory,
            experience = player.totalExperience,
            deathTime = System.currentTimeMillis(),
            deathReason = e.deathMessage ?: "Unknown death reason.",
            location = listOf(gravestoneLoc.blockX, gravestoneLoc.blockY, gravestoneLoc.blockZ),
            uuid = player.uniqueId.toString(),
            username = player.name,
            world = player.world.name
        )

        val storage =  Init.inventoryStorage.storage.players[player.uniqueId.toString()] ?: GravestoneItemsPlayer(stones=mutableListOf())
        storage.stones.add(gravestone)

        Init.inventoryStorage.storage.players[player.uniqueId.toString()] = storage

        Init.inventoryStorage.writeToFile()
    }*/

    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val player = e.entity

        if (!player.hasPermission("gravestonesmfg.allow-graves"))
            return

        /*var pos = player.location

        if (!pos.block.type.isAir) {
            val newPos = pos.clone()

            pos = if (newPos.set(floor(pos.x), floor(pos.y), floor(pos.z)).block.type.isAir)
                newPos
            else if (newPos.set(ceil(pos.x), ceil(pos.y), ceil(pos.z)).block.type.isAir)
                newPos
            else
                newPos
        }

        player.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}Your gravestone is located at ${ChatColor.DARK_RED}${pos.blockX}, ${pos.blockY}, ${pos.blockZ}${ChatColor.RED}, in world ${ChatColor.DARK_RED}${player.world.name}")
*/
        val inventory = mutableListOf<GravestoneItem>()

        if (e.drops.size == 0) {
            e.droppedExp = if (Init.plugin.config.contains("use-old-experience") && Init.plugin.config.getBoolean("use-old-experience"))
                player.totalExperience
            else
                ExperienceUtil.getPlayerExp(player)

            return
        }

        e.drops.forEach {
            try {
                val nbt = ItemSerializer.serialize(it)

                inventory.add(
                    GravestoneItem(
                        id = it.type.name,
                        count = it.amount,
                        nbtData = nbt
                    )
                )
            } catch (e: Exception) {
                inventory.add(
                    GravestoneItem(
                        id = it.type.name,
                        count = it.amount
                    )
                )
            }
        }

        e.drops.removeAll(e.drops)

        val gravestoneLoc = findPos(player.location, player)

        player.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}Your gravestone is located at ${ChatColor.DARK_RED}${gravestoneLoc.blockX}, ${gravestoneLoc.blockY}, ${gravestoneLoc.blockZ}${ChatColor.RED}, in world ${ChatColor.DARK_RED}${player.world.name}")

        val gravestoneBlock = player.world.getBlockAt(gravestoneLoc)
        gravestoneBlock.breakNaturally()
        gravestoneBlock.type = Material.BEACON

        e.droppedExp = 0

        val gravestone = GravestoneStone(
            inventory = inventory,
            experience = if (Init.plugin.config.contains("use-old-experience") && Init.plugin.config.getBoolean("use-old-experience"))
                player.totalExperience
            else
                ExperienceUtil.getPlayerExp(player),
            deathTime = System.currentTimeMillis(),
            deathReason = LegacyComponentSerializer.legacySection().serialize(e.deathMessage() ?: Component.text("Unknown death reason.")),
            location = listOf(gravestoneLoc.blockX, gravestoneLoc.blockY, gravestoneLoc.blockZ),
            uuid = player.uniqueId.toString(),
            username = player.name,
            world = player.world.name
        )

        val storage =  Init.inventoryStorage.storage.players[player.uniqueId.toString()] ?: GravestoneItemsPlayer(stones=mutableListOf())
        storage.stones.add(gravestone)

        Init.inventoryStorage.storage.players[player.uniqueId.toString()] = storage

        Init.inventoryStorage.writeToFile()
    }

    private fun findPos(location: Location, player: Player): Location {
        return if (player.world.getBlockAt(location).type == Material.LAVA || !location.block.type.isAir) {
            findPos(Location(player.world, location.x, location.y + 1, location.z), player)
        } else {
            if (player.world.getBlockAt(Location(player.world, location.x, location.y - 1, location.z)).type == Material.LAVA)
                player.world.getBlockAt(Location(player.world, location.x, location.y - 1, location.z)).type = Material.PODZOL

            if (location.y <= location.world.minHeight) {
                location.y = location.world.minHeight + 1.0
                player.world.getBlockAt(Location(player.world, location.x, location.y - 1, location.z)).type = Material.PODZOL
            }
            if (location.y >= location.world.maxHeight) {
                location.y = location.world.maxHeight - 1.0
                player.world.getBlockAt(Location(player.world, location.x, location.y - 1, location.z)).type = Material.PODZOL
            }

            location
        }
    }
}