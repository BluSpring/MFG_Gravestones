package xyz.bluspring.mfg.gravestones.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object LocateGravestoneCommand : CommandExecutor {
    override fun onCommand(
        commandSender: CommandSender,
        command: Command,
        string: String,
        strings: Array<out String>
    ): Boolean {
        return false
    }
}