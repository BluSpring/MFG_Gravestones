package xyz.bluspring.mfg.gravestones.listeners

import xyz.bluspring.mfg.gravestones.Init
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityExplodeEvent

object EntityOnExplosionListener : Listener {
    @EventHandler
    fun onEntityExplosion(e: EntityExplodeEvent) {
        if (e.isCancelled) return

        val copy = mutableListOf<Block>()
        copy.addAll(e.blockList())

        copy.forEach {bl ->
            if (bl.type != Material.BEACON) return@forEach
            if (!Init.inventoryStorage.storage.players.any {
                it.value.stones.any {gs ->
                    gs.location[0] == bl.location.x.toInt() && gs.location[1] == bl.location.y.toInt() && gs.location[2] == bl.location.z.toInt()
                }
            }) return@forEach

            e.blockList().remove(bl)
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    fun onEntityChangeBlock(e: EntityChangeBlockEvent) {
        if (e.entityType == EntityType.WITHER_SKULL || e.entityType == EntityType.WITHER || e.entityType == EntityType.ENDER_DRAGON || e.entityType == EntityType.ENDERMAN) {
            if (e.block.type != Material.BEACON) return

            if (!Init.inventoryStorage.storage.players.any {
                it.value.stones.any {gs ->
                    gs.location[0] == e.block.location.x.toInt() && gs.location[1] == e.block.location.y.toInt() && gs.location[2] == e.block.location.z.toInt()
                }
            }) return

            e.isCancelled = true
        }
    }
}