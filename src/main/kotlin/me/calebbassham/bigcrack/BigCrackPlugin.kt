package me.calebbassham.bigcrack

import me.calebbassham.scenariomanager.api.scenarioManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class BigCrackPlugin : JavaPlugin() {

    private var bigCrackInstance: BigCrack? = null

    override fun onEnable() {
        val bc = BigCrack()
        bigCrackInstance = bc
        scenarioManager.register(bc, this)

        Bukkit.getPluginManager().registerEvents(StopPhysicsListener(bc), this)
    }

}