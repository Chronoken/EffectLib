package de.slikey.effectlib.util;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.data.BlockData;

import de.slikey.effectlib.EffectManager;

public class ParticleDisplay {

    protected EffectManager manager;

    private static boolean hasColorTransition = true;

    protected void spawnParticle(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers) {
        try {
            if (targetPlayers == null) {
                double squared = range * range;
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (!manager.isVisiblePlayer(player, center, squared)) continue;

                    playerDisplay(particle, options, center, player);
                }
                return;
            }

            for (final Player player : targetPlayers) {
                if (manager.isPlayerIgnored(player)) continue;

                playerDisplay(particle, options, center, player);
            }

        } catch (Exception ex) {
            if (manager != null) manager.onError(ex);
        }
    }

    private void playerDisplay(Particle particle, ParticleOptions options, Location center, Player player) {
        if (particle == Particle.valueOf("ENTITY_EFFECT")) {
            player.spawnParticle(particle, center, options.amount, options.offsetX, options.offsetY, options.offsetZ, options.speed, options.color, true);
        } else {
            player.spawnParticle(particle, center, options.amount, options.offsetX, options.offsetY, options.offsetZ, options.speed, options.data, true);
        }

        displayFakeBlock(player, center, options);
    }

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

        if (particle == Particle.BLOCK
                || particle == Particle.FALLING_DUST
                || particle == Particle.DUST_PILLAR) {
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
            // color is required
            if (options.color == null) options.color = Color.RED;
            options.data = new Particle.DustOptions(options.color, options.size);
        }

        if (particle == Particle.DUST_COLOR_TRANSITION) {
            if (options.color == null) options.color = Color.RED;
            if (options.toColor == null) options.toColor = options.color;
            options.data = new Particle.DustTransition(options.color, options.toColor, options.size);
        }

        if (particle == Particle.VIBRATION) {
            if (options.target == null) return;

            Vibration.Destination destination;
            Entity targetEntity = options.target.getEntity();
            if (targetEntity != null) destination = new Vibration.Destination.EntityDestination(targetEntity);
            else {
                Location targetLocation = options.target.getLocation();
                if (targetLocation == null) return;

                destination = new Vibration.Destination.BlockDestination(targetLocation);
            }

            options.data = new Vibration(destination, options.arrivalTime);
        }

        if (particle == Particle.SHRIEK) {
            if (options.shriekDelay < 0) options.shriekDelay = 0;
            options.data = options.shriekDelay;
        }

        if (particle == Particle.SCULK_CHARGE) {
            options.data = options.sculkChargeRotation;
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

    @SuppressWarnings({"deprecation"})
    protected void displayItem(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers) {
        Material material = options.material;
        if (material == null || material.isAir()) return;

        ItemStack item = new ItemStack(material);
        item.setDurability(options.materialData);
        options.data = item;
        spawnParticle(particle, options, center, range, targetPlayers);
    }

    protected void displayLegacyColored(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers) {
        // Colored particles can't have a speed of 0.
        Color color = options.color;
        if (color == null) color = Color.RED;
        if (options.speed == 0) options.speed = 1;
        // Amount = 0 is a special flag that means use the offset as color
        options.amount = 0;

        float offsetX = (float) color.getRed() / 255;
        float offsetY = (float) color.getGreen() / 255;
        float offsetZ = (float) color.getBlue() / 255;

        // The redstone particle reverts to red if R is 0!
        if (offsetX < Float.MIN_NORMAL) offsetX = Float.MIN_NORMAL;

        options.offsetX = offsetX;
        options.offsetY = offsetY;
        options.offsetZ = offsetZ;

        spawnParticle(particle, options, center, range, targetPlayers);
    }

    public void setManager(EffectManager manager) {
        this.manager = manager;
    }

    public static boolean hasColorTransition() {
        return hasColorTransition;
    }

}
