package de.slikey.effectlib.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.block.data.BlockData;

import de.slikey.effectlib.EffectManager;

public class ParticleDisplay {

    private EffectManager manager;

    public void display(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers) {
        try {
            if (targetPlayers == null) {
                double squared = range * range;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!manager.isVisiblePlayer(player, center, squared)) continue;

                    playerDisplay(particle, options, center, player);
                }
                return;
            }

            for (Player player : targetPlayers) {
                if (manager.isPlayerIgnored(player)) continue;

                playerDisplay(particle, options, center, player);
            }

        } catch (Exception ex) {
            if (manager != null) manager.onError(ex);
        }
    }

    private void playerDisplay(Particle particle, ParticleOptions options, Location center, Player player) {
        player.spawnParticle(particle, center, options.amount(), options.offsetX(), options.offsetY(), options.offsetZ(), options.speed(), options.resolve(particle), true);

        displayFakeBlock(player, center, options);
    }

    protected void displayFakeBlock(Player player, Location center, ParticleOptions options) {
        if (options.blockData() == null) return;
        if (!center.getBlock().isPassable() && !center.getBlock().isEmpty()) return;

        BlockData blockData = Bukkit.createBlockData(options.blockData().toLowerCase());
        Location b = center.getBlock().getLocation().clone();
        player.sendBlockChange(b, blockData);

        Bukkit.getScheduler().runTaskLaterAsynchronously(manager.getOwningPlugin(), () -> player.sendBlockChange(b, b.getBlock().getBlockData()), options.blockDuration());
    }

    public void setManager(EffectManager manager) {
        this.manager = manager;
    }

}
