package de.slikey.effectlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.meta.Damageable;

public record ParticleOptions(
        DynamicLocation target,
        float offsetX,
        float offsetY,
        float offsetZ,
        float speed,
        int amount,
        float size,
        Color color,
        Color toColor,
        int arrivalTime,
        Material material,
        byte materialData,
        String blockData,
        long blockDuration,
        int shriekDelay,
        int trailDuration,
        float sculkChargeRotation,
        float dragonBreathPower,
        float spellPower
) {

    public Object resolve(@NotNull Particle particle) {
        Class<?> type = particle.getDataType();

        if (type == Color.class) {
            return Objects.requireNonNullElse(this.color, Color.RED);
        }

        if (type == ItemStack.class) {
            if (material == null || material.isAir()) return null;

            ItemStack item = new ItemStack(material);
            item.editMeta(Damageable.class, damageable -> damageable.setDamage(materialData));
            return item;
        }

        if (type == BlockData.class) {
            if (material == null || material.isAir()) return null;
            return material.createBlockData();
        }

        if (type == Particle.DustOptions.class) {
            Color color = Objects.requireNonNullElse(this.color, Color.RED);
            return new Particle.DustOptions(color, size);
        }

        if (type == Particle.DustTransition.class) {
            Color color = Objects.requireNonNullElse(this.color, Color.RED);
            Color toColor = Objects.requireNonNullElse(this.toColor, color);
            return new Particle.DustTransition(color, toColor, size);
        }

        if (type == Vibration.class) {
            if (target == null) return null;

            Vibration.Destination destination;
            Entity targetEntity = target.getEntity();
            if (targetEntity != null) destination = new Vibration.Destination.EntityDestination(targetEntity);
            else {
                Location targetLocation = target.getLocation();
                if (targetLocation == null) return null;

                destination = new Vibration.Destination.BlockDestination(targetLocation);
            }

            return new Vibration(destination, arrivalTime);
        }

        if (type == Particle.Trail.class) {
            if (color == null) return null;
            Location location = target == null ? null : target.getLocation();
            if (location == null) return null;

            return new Particle.Trail(location, color, trailDuration);
        }

        if (type == Particle.Spell.class) {
            if (color == null) return null;
            return new Particle.Spell(color, spellPower);
        }

        return switch (particle) {
            case SHRIEK -> shriekDelay;
            case SCULK_CHARGE -> sculkChargeRotation;
            case DRAGON_BREATH -> dragonBreathPower;
            default -> null;
        };
    }

}
