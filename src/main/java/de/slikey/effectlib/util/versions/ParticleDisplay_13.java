package de.slikey.effectlib.util.versions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import de.slikey.effectlib.util.ParticleDisplay;
import de.slikey.effectlib.util.ParticleOptions;

public class ParticleDisplay_13 extends ParticleDisplay {

    @Override
    public void display(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers) {
        // Legacy colorizeable particles
        if (options.color != null && particle == Particle.ENTITY_EFFECT) {
            displayLegacyColored(particle, options, center, range, targetPlayers);
            return;
        }

        if (particle == Particle.ITEM) {
            displayItem(particle, options, center, range, targetPlayers);
            return;
        }

        if (particle == Particle.BLOCK || particle == Particle.FALLING_DUST) {
            Material material = options.material;
            if (material == null || material.name().contains("AIR")) return;
            try {
                options.data = material.createBlockData();
            } catch (Exception ex) {
                manager.onError("Error creating block data for " + material, ex);
            }
            if (options.data == null) return;
        }

        if (particle == Particle.DUST) {
            // color is required for 1.13
            if (options.color == null) options.color = Color.RED;
            options.data = new Particle.DustOptions(options.color, options.size);
        }

        spawnParticle(particle, options, center, range, targetPlayers);
    }

    protected void displayFakeBlock(final Player player, Location center, ParticleOptions options) {
        if (options.blockData == null) return;
        if (!center.getBlock().isPassable() && !center.getBlock().isEmpty()) return;

        BlockData blockData = Bukkit.createBlockData(options.blockData.toLowerCase());
        final Location b = center.getBlock().getLocation().clone();
        player.sendBlockChange(b, blockData);

        Bukkit.getScheduler().runTaskLaterAsynchronously(manager.getOwningPlugin(), new Runnable() {
            @Override
            public void run() {
                player.sendBlockChange(b, b.getBlock().getBlockData());
            }
        }, options.blockDuration);
    }

}
