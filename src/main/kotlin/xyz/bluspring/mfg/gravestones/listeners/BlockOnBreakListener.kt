package xyz.bluspring.mfg.gravestones.listeners

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import xyz.bluspring.mfg.gravestones.Init
import xyz.bluspring.mfg.gravestones.util.ItemSerializer

object BlockOnBreakListener : Listener {
    /*
    fun getOBCClass(className: String): Class<*>? {
        val fullName = "org.bukkit.craftbukkit.${Bukkit.getServer()::class.java.`package`.name.substring(Bukkit.getServer()::class.java.`package`.name.lastIndexOf('.') + 1)}.$className"

        var clazz: Class<*>? = null
        try {
            clazz = Class.forName(fullName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return clazz
    }

    private val ITEM_META_DESERIALIZATOR = getOBCClass("inventory.CraftMetaItem")!!.classes[0]
    private val DESERIALIZE = try {
        ITEM_META_DESERIALIZATOR.getMethod(
            "deserialize",
            MutableMap::class.java
        )
    } catch (_: Exception) {
        null
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        if (e.block.type != Material.BEACON) return
        if (Init.inventoryStorage.storage.players.any {
                it.value.stones.any {gs ->
                    gs.location[0] == e.block.location.x.toInt() && gs.location[1] == e.block.location.y.toInt() && gs.location[2] == e.block.location.z.toInt()
                }
            }) {

            val gStone = Init.inventoryStorage.storage.players.filter {
                it.value.stones.any { gs ->
                    gs.location[0] == e.block.location.x.toInt() && gs.location[1] == e.block.location.y.toInt() && gs.location[2] == e.block.location.z.toInt()
                }
            }.entries.first().value.stones.find { gs -> gs.location[0] == e.block.location.x.toInt() && gs.location[1] == e.block.location.y.toInt() && gs.location[2] == e.block.location.z.toInt() }!!

            if (e.player.world.name != gStone.world) return

            if (e.player.uniqueId.toString() != gStone.uuid) {
                e.isCancelled = true
                e.player.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}This is not your gravestone!")
                return
            } else {
                e.player.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}Gravestone broken!")
            }

            gStone.inventory.forEach {
                val item = ItemStack(Material.getMaterial(it.id)!!, it.count)

                try {
                    val meta = item.itemMeta
                    if (it.nbtData.displayName != null)
                        meta.setDisplayName(it.nbtData.displayName)
                    if (it.nbtData.enchants != null && it.nbtData.enchants.isNotEmpty())
                        it.nbtData.enchants.forEach { itc ->
                            meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(itc.key.removePrefix("minecraft:")))!!, itc.value, true)
                        }

                    if (it.nbtData.lore != null)
                        meta.lore = it.nbtData.lore

                    if (meta is Damageable) {
                        meta.damage = it.durability ?: meta.damage
                    }
                    
                    if (it.nbtData.storedEnchants != null && it.nbtData.storedEnchants.isNotEmpty()) {
                        val ita = (meta as EnchantmentStorageMeta)
                        
                        it.nbtData.storedEnchants.forEach { itc ->
                            ita.addStoredEnchant(Enchantment.getByKey(NamespacedKey.minecraft(itc.key.removePrefix("minecraft:")))!!, itc.value, true)
                        }
                    }

                    if (it.nbtData.storage != null) {
                        val blockState = ((item.itemMeta as BlockStateMeta).blockState as ShulkerBox)

                        it.nbtData.storage.forEach {ab ->
                            val itStack = ItemStack(Material.getMaterial(ab.id!!)!!, ab.amount ?: 1)
                            val itMeta = itStack.itemMeta
                            
                            if (ab.itemMeta != null) {
                                val altMeta = ab.itemMeta
                                if (altMeta.displayName != null) {
                                    itStack.itemMeta.setDisplayName(altMeta.displayName)
                                }
                                
                                if (altMeta.enchants != null && altMeta.enchants.isNotEmpty()) {
                                    altMeta.enchants.forEach { itc ->
                                        itMeta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(itc.key.removePrefix("minecraft:")))!!, itc.value, true)
                                    }
                                }
                                
                                if (altMeta.lore != null)
                                    itMeta.lore = altMeta.lore
                                
                                if (itMeta is Damageable) {
                                    itMeta.damage = ab.durability ?: itMeta.damage
                                }
                                
                                if (altMeta.storedEnchants != null && altMeta.storedEnchants.isNotEmpty()) {
                                    val ita = (itMeta as EnchantmentStorageMeta)

                                    altMeta.storedEnchants.forEach { itc ->
                                        ita.addStoredEnchant(Enchantment.getByKey(NamespacedKey.minecraft(itc.key.removePrefix("minecraft:")))!!, itc.value, true)
                                    }
                                }

                                itStack.itemMeta = itMeta
                            }

                            blockState.inventory.setItem(ab.position ?: 0, itStack)
                        }

                        val otherAltMeta = (item.itemMeta as BlockStateMeta)
                        otherAltMeta.blockState = blockState

                        item.itemMeta = otherAltMeta
                    }

                    item.itemMeta = meta
                } catch (e: Exception) {
                    Init.plugin.logger.severe("An error has occurred whilst trying to deserialize item meta!")

                    e.printStackTrace()
                }

                e.player.world.dropItem(e.player.location, item)
            }

            Init.inventoryStorage.storage.players[e.player.uniqueId.toString()]!!.stones.remove(gStone)

            Init.inventoryStorage.writeToFile()

            e.isDropItems = false
        }
    }*/

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBlockBreak(e: BlockBreakEvent) {
        if (e.block.type != Material.BEACON) return
        if (Init.inventoryStorage.storage.players.any {
                it.value.stones.any { gs ->
                    gs.location[0] == e.block.location.x.toInt() && gs.location[1] == e.block.location.y.toInt() && gs.location[2] == e.block.location.z.toInt()
                }
            }) {

            val gStone = Init.inventoryStorage.storage.players.filter {
                it.value.stones.any { gs ->
                    gs.location[0] == e.block.location.x.toInt() && gs.location[1] == e.block.location.y.toInt() && gs.location[2] == e.block.location.z.toInt()
                }
            }.entries.first().value.stones.find { gs -> gs.location[0] == e.block.location.x.toInt() && gs.location[1] == e.block.location.y.toInt() && gs.location[2] == e.block.location.z.toInt() }!!

            if (e.player.world.name != gStone.world) return

            if (e.player.uniqueId.toString() != gStone.uuid && !Init.plugin.config.getBoolean("allow-all-users-break")) {
                e.isCancelled = true
                e.player.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}This is not your gravestone!")
                return
            } else {
                e.player.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}Gravestone broken!")
            }

            gStone.inventory.forEach {
                val item = if (it.nbtData != null) {
                    try {
                        val itt = ItemSerializer.deserialize(it.nbtData)

                        itt!!.amount = it.count

                        itt
                    } catch (_: Exception) {
                        Init.plugin.logger.warning("Item ${it.id} (${it.count}) didn't deserialize! Returning a normal count...")
                        ItemStack(Material.getMaterial(it.id)!!, it.count)
                    }
                } else {
                    ItemStack(Material.getMaterial(it.id)!!, it.count)
                }

                e.player.world.dropItem(e.player.location, item)
            }

            if (gStone.experience > 0) {
                val xp = e.player.location.world.spawn(e.player.location, ExperienceOrb::class.java)

                xp.experience = gStone.experience
            }

            Init.inventoryStorage.storage.players[e.player.uniqueId.toString()]!!.stones.remove(gStone)

            Init.inventoryStorage.writeToFile()

            e.isDropItems = false
            
            e.block.type = Material.AIR
        }
    }
}