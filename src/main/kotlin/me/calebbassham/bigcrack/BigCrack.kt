package me.calebbassham.bigcrack

import me.calebbassham.scenariomanager.ScenarioManagerUtils
import me.calebbassham.scenariomanager.api.SimpleScenario
import me.calebbassham.scenariomanager.api.WorldUpdater
import me.calebbassham.scenariomanager.api.settings.SimpleScenarioSetting
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable
import java.text.NumberFormat
import java.util.concurrent.CompletableFuture

class BigCrack : SimpleScenario(), WorldUpdater {

    val blocksPerTick = SimpleScenarioSetting("BlocksPerTick", "The number of blocks to place per ticks.", 400)

    override val settings = listOf(blocksPerTick)

    var runningWorlds: Array<World> = emptyArray()

    override fun updateWorld(world: World): CompletableFuture<Void> {
        val future = CompletableFuture<Void>()

        runningWorlds += world

        CreateCrackTask(future, world).runTaskTimer(plugin, 0, 1)

        return future
    }

    inner class CreateCrackTask(private val future: CompletableFuture<Void>, private val world: World): BukkitRunnable() {

        val mapRadius: Int
            get() = scenarioManager.gameWorldProvider.getMapRadius(world)!!

        val blocksPerTick: Int
            get() = this@BigCrack.blocksPerTick.value

        val center = scenarioManager.gameWorldProvider.getMapCenter(world)!!
        val centerX = center.blockX
        val centerZ = center.blockZ

        val widthRadius = 8

        var x = -mapRadius
        var z = -widthRadius

        var y = world.maxHeight

        val totalBlocks = ((mapRadius * 2) + 1) * 16 * world.maxHeight

        val percentFormat = NumberFormat.getPercentInstance().apply {
            minimumFractionDigits = 1
        }

        val approximateBlocksCompleted: Int
            get() {
                if (x == -mapRadius) return 0

                val totalXDone = mapRadius + (x - 1)

                return totalXDone * (widthRadius * 2) * world.maxHeight
            }

        val approximateBlocksRemaining: Int
            get() = totalBlocks - approximateBlocksCompleted

        val approximateBlocksCompletedPercentage: Double
            get() = approximateBlocksCompleted.toDouble() / totalBlocks

        val estimatedTotalTime
            get() = totalBlocks / blocksPerTick

        val estimatedRemainingTime
            get() = approximateBlocksRemaining / blocksPerTick

        var ticks = -1

        override fun run() {
            for (i in 1..blocksPerTick) {
                val block = world.getBlockAt(x + centerX, y, z + centerZ)
                block.type = Material.AIR

                y--

                if (y < 0) {
                    z++

                    if (z > widthRadius) {
                        z = -widthRadius
                        x++
                    }

                    if (x > mapRadius) {
                        cancel()
                        future.complete(null)
                        runningWorlds = runningWorlds.filter { it != world }.toTypedArray()
                        return
                    }

                    y = world.maxHeight
                }
            }

            if (ticks == - 1 || ticks >= 20 * 10) {
                broadcast("%s complete generating world ${world.name}. ~%s remaining.".format(percentFormat.format(approximateBlocksCompletedPercentage), ScenarioManagerUtils.formatTicks(estimatedRemainingTime.toLong())))
                ticks = 0
            }

            ticks++
        }

    }

}