package xyz.bluspring.mfg.gravestones

import xyz.bluspring.mfg.gravestones.commands.RecoverGravestoneCommand
import xyz.bluspring.mfg.gravestones.listeners.BlockOnBreakListener
import xyz.bluspring.mfg.gravestones.listeners.BlockOnInteractListener
import xyz.bluspring.mfg.gravestones.listeners.EntityOnExplosionListener
import xyz.bluspring.mfg.gravestones.listeners.PlayerOnDeathListener
import xyz.bluspring.mfg.gravestones.util.InventoryStorage
import org.bukkit.plugin.java.JavaPlugin
import xyz.bluspring.mfg.gravestones.commands.LocateGravestoneCommand

class Init : JavaPlugin() {
    private val log = this.server.logger

    override fun onEnable() {
        plugin = this
        log.info("MFG-Gravestones has been enabled.")

        //this.saveResource("inventoryStorage.json", false)
        this.saveDefaultConfig()
        this.saveResource("inventoryStorage.yml", false)

        inventoryStorage = InventoryStorage

        this.server.pluginManager.registerEvents(PlayerOnDeathListener, this)
        this.server.pluginManager.registerEvents(BlockOnBreakListener, this)
        this.server.pluginManager.registerEvents(BlockOnInteractListener, this)
        this.server.pluginManager.registerEvents(EntityOnExplosionListener, this)

        this.getCommand("recover-gravestone")?.setExecutor(RecoverGravestoneCommand)
        this.getCommand("locate-gravestone")?.setExecutor(LocateGravestoneCommand)
    }

    override fun onDisable() {
        log.info("MFG-Gravestones has been disabled.")
    }

    companion object {
        lateinit var plugin: JavaPlugin
        lateinit var inventoryStorage: InventoryStorage
    }
}