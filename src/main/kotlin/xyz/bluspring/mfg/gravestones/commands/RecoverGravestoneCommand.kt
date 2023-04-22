package xyz.bluspring.mfg.gravestones.commands

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import xyz.bluspring.mfg.gravestones.Init
import java.util.*

object RecoverGravestoneCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}Attempting to replace all gravestones to their original positions...")

        Init.inventoryStorage.storage.players.forEach {
            it.value.stones.forEach {abb ->
                val world = Init.plugin.server.getWorld(abb.world)
                val oldLoc = Location(world, abb.location[0].toDouble(), abb.location[1].toDouble(), abb.location[2].toDouble())
                val loc = findPos(Location(world,
                    abb.location[0].toDouble(), abb.location[1].toDouble(), abb.location[2].toDouble()
                ), world!!)

                if (world.getBlockAt(loc).type != Material.BEACON) {
                    world.getBlockAt(oldLoc).breakNaturally()

                    abb.location = listOf(loc.blockX, loc.blockY, loc.blockZ)
                    Init.inventoryStorage.writeToFile()

                    val gravestoneBlock = world.getBlockAt(loc)
                    gravestoneBlock.breakNaturally()
                    gravestoneBlock.type = Material.BEACON

                    sender.sendMessage("${ChatColor.RED}Gravestones ${ChatColor.GREEN}>> ${ChatColor.RED}Replaced gravestone for ${ChatColor.YELLOW}${
                        if (Init.plugin.server.onlinePlayers.any { itt ->
                                itt.uniqueId.toString() == it.key
                            }) Init.plugin.server.getPlayer(
                                UUID.fromString(it.key)
                            )!!.name 
                        else 
                            Init.plugin.server.getOfflinePlayer(UUID.fromString(it.key)).name
                    }${ChatColor.RED} at coordinates ${ChatColor.YELLOW}${gravestoneBlock.location.x} / ${gravestoneBlock.location.y} / ${gravestoneBlock.location.z}")
                }
            }
        }

        return true
    }

    private fun findPos(location: Location, world: World): Location {
        return if (world.getBlockAt(location).type == Material.LAVA) {
            findPos(Location(world, location.x, location.y + 1, location.z), world)
        } else {
            if (world.getBlockAt(Location(world, location.x, location.y - 1, location.z)).type == Material.LAVA)
                world.getBlockAt(Location(world, location.x, location.y - 1, location.z)).type = Material.PODZOL

            if (location.y <= world.minHeight) {
                location.y = world.minHeight + 1.0
                world.getBlockAt(Location(world, location.x, location.y - 1, location.z)).type = Material.PODZOL
            }
            if (location.y >= world.maxHeight) {
                location.y = world.maxHeight - 1.0
                world.getBlockAt(Location(world, location.x, location.y - 1, location.z)).type = Material.PODZOL
            }
            location
        }
    }
}