package xyz.bluspring.mfg.gravestones.listeners

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import xyz.bluspring.mfg.gravestones.Init
import java.util.*

object BlockOnInteractListener : Listener {
    @EventHandler
    fun onBlockInteract(e: PlayerInteractEvent) {
        if (e.action == Action.RIGHT_CLICK_BLOCK) {
            val block = e.clickedBlock!!
            if (block.type != Material.BEACON) return
            if (Init.inventoryStorage.storage.players.any {
                    it.value.stones.any {gs ->
                        gs.location[0] == block.location.x.toInt() && gs.location[1] == block.location.y.toInt() && gs.location[2] == block.location.z.toInt()
                    }
                }) {

                val gStone = Init.inventoryStorage.storage.players.filter {
                    it.value.stones.any { gs ->
                        gs.location[0] == block.location.x.toInt() && gs.location[1] == block.location.y.toInt() && gs.location[2] == block.location.z.toInt()
                    }
                }.entries.first().value.stones.find { gs -> gs.location[0] == block.location.x.toInt() && gs.location[1] == block.location.y.toInt() && gs.location[2] == block.location.z.toInt() }!!

                if (e.player.world.name != gStone.world) return

                e.isCancelled = true

                e.player.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}This gravestone is owned by ${ChatColor.DARK_RED}${gStone.username}\n${ChatColor.RED}Created on ${ChatColor.DARK_RED}${Date(gStone.deathTime)}${ChatColor.RED}.\nDeath reason: ${ChatColor.DARK_RED}${gStone.deathReason}")
            }
        }
    }
}