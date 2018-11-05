package me.calebbassham.bigcrack

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFormEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.block.BlockSpreadEvent

class StopPhysicsListener(private val bigCrack: BigCrack) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockSpread(e: BlockSpreadEvent) {
        if(!bigCrack.runningWorlds.contains(e.block.world)) return

        e.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockFromTo(e: BlockFromToEvent) {
        if(!bigCrack.runningWorlds.contains(e.block.world)) return

        e.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockForm(e: BlockFormEvent) {
        if(!bigCrack.runningWorlds.contains(e.block.world)) return

        e.isCancelled = true
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockPhysics(e: BlockPhysicsEvent) {
        if(!bigCrack.runningWorlds.contains(e.block.world)) return

        e.isCancelled = true
    }

}