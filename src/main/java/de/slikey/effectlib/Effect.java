package de.slikey.effectlib;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleOptions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Effect implements Runnable {

    protected final EffectManager effectManager;
    protected String subEffectClass = null;
    protected DynamicLocation origin = null;
    protected DynamicLocation target = null;
    protected int maxIterations;
    private boolean done = false;
    private boolean playing = false;
    private long startTime;

    public EffectType type = EffectType.INSTANT;
    public Particle particle;
    public Color color = null;
    public List<Color> colorList = null;
    public String colors = null;
    public Color toColor = null;
    public List<Color> toColorList = null;
    public String toColors = null;
    public int shriekDelay;
    public float sculkChargeRotation;
    public int arrivalTime;
    public ConfigurationSection subEffect = null;
    public float speed = 0;
    public float particleData = 0;
    public int delay = 0;
    public int period = 1;
    public int iterations = 0;
    public Integer duration = null;
    public double probability = 1;
    public Runnable callback = null;
    public float visibleRange = 32;
    public boolean autoOrient = false;
    public Vector offset = null;
    public Vector relativeOffset = null;
    public Vector targetOffset = null;
    public float yawOffset = 0;
    public float pitchOffset = 0;
    public Float yaw = null;
    public Float pitch = null;
    public boolean updateLocations = true;
    public boolean updateDirections = true;
    public List<Player> targetPlayers;
    public Material material;
    public byte materialData;
    public String blockData;
    public long blockDuration;
    public int particleCount = 1;
    public float particleOffsetX = 0;
    public float particleOffsetY = 0;
    public float particleOffsetZ = 0;
    public float particleSize = 1;
    public boolean asynchronous = true;
    public boolean disappearWithOriginEntity = false;
    public boolean disappearWithTargetEntity = false;

    public EffectManager getEffectManager() {
        return effectManager;
    }

    /**
     * Type of effect being played.
     *
     * @see de.slikey.effectlib.EffectType
     */
    @Nonnull
    public EffectType getType() {
        return type;
    }

    /**
     * ParticleType of spawned particle.
     */
    @Nullable
    public Particle getParticle() {
        return particle;
    }

    /**
     * Can be used to colorize certain particles. As of 1.8, those
     * include SPELL_MOB_AMBIENT, SPELL_MOB and REDSTONE.
     */
    @Nullable
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Color> getColorList() {
        return colorList;
    }

    public String getColors() {
        return colors;
    }

    /**
     * Used for dust particles in 1.17 and up, to make a color transition.
     */
    @Nullable
    public Color getToColor() {
        return toColor;
    }

    public List<Color> getToColorList() {
        return toColorList;
    }

    public String getToColors() {
        return toColors;
    }

    /**
     * Used only by the shriek particle in 1.19 and up.
     */
    public int getShriekDelay() {
        return shriekDelay;
    }

    /**
     * Used only by the sculk_charge particle in 1.19 and up.
     */
    public float getSculkChargeRotation() {
        return sculkChargeRotation;
    }

    /**
     * Used only by the vibration particle in 1.17 and up.
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Class of subeffect to play at effect location.
     */
    @Nullable
    public String getSubEffectClass() {
        return subEffectClass;
    }

    /**
     * Config section of subeffect to play at effect location.
     */
    @Nullable
    public ConfigurationSection getSubEffect() {
        return subEffect;
    }

    /**
     * This can be used to give particles a set speed when spawned.
     * This will not work with colored particles.
     */
    @Deprecated
    public float getSpeed() {
        return speed;
    }

    /**
     * This can be used to give particles a set speed when spawned.
     * This will not work with colored particles.
     *
     * This is a replacement for "speed"
     */
    public float getParticleData() {
        return particleData;
    }

    /**
     * Interval to wait for repeating effects.
     *
     * @see de.slikey.effectlib.EffectType
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Amount of repetitions to do.
     * Set this to -1 for an infinite effect
     *
     * @see de.slikey.effectlib.EffectType
     */
    public int getPeriod() {
        return period;
    }

    /**
     * Amount of repetitions to do.
     * Set this to -1 for an infinite effect
     *
     * @see de.slikey.effectlib.EffectType
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Total duration of this effect in milliseconds.
     *
     * If set, this will adjust iterations to match
     * the defined delay such that the effect lasts
     * a specific duration.
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Probability that this effect will play on each iteration.
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Callback to run, after effect is done.
     *
     * @see java.lang.Runnable
     */
    public Runnable getCallback() {
        return callback;
    }

    /**
     * Display particles to players within this radius.
     */
    public float getVisibleRange() {
        return visibleRange;
    }

    /**
     * If true, and a "target" Location or Entity is set, the two Locations
     * will orient to face one another.
     */
    public boolean isAutoOrient() {
        return autoOrient;
    }

    /**
     * If set, will offset the origin location.
     */
    public Vector getOffset() {
        return offset;
    }

    /**
     * If set, will offset the origin location, relative to the origin direction.
     */
    public Vector getRelativeOffset() {
        return relativeOffset;
    }

    /**
     * If set, will offset the target location.
     */
    public Vector getTargetOffset() {
        return targetOffset;
    }

    /**
     * These are used to modify the direction of the origin Location.
     */
    public float getYawOffset() {
        return yawOffset;
    }

    public float getPitchOffset() {
        return pitchOffset;
    }

    public Float getYaw() {
        return yaw;
    }

    public Float getPitch() {
        return pitch;
    }

    /**
     * If set to false, Entity-bound locations will not update during the Effect
     */
    public boolean isUpdateLocations() {
        return updateLocations;
    }

    /**
     * If set to false, Entity-bound directions will not update during the Effect
     */
    public boolean canUpdateDirections() {
        return updateDirections;
    }

    /**
     * A specific player who should see this effect.
     */
    public Player getTargetPlayer() {
        if (targetPlayers != null && !targetPlayers.isEmpty()) {
            return targetPlayers.get(0);
        }
        return null;
    }

    public void setTargetPlayer(Player targetPlayer) {
        if (targetPlayer == null) {
            this.targetPlayers = null;
        } else {
            List<Player> list = new ArrayList<>();
            list.add(targetPlayer);
            setTargetPlayers(list);
        }
    }

    /**
     * A group of players who should see this effect.
     */
    public List<Player> getTargetPlayers() {
        return targetPlayers;
    }

    public void setTargetPlayers(List<Player> targetPlayers) {
        this.targetPlayers = targetPlayers;
    }

    /**
     * The Material and data to use for block and item break particles
     */
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setType(final EffectType type) {
        this.type = type;
    }

    public byte getMaterialData() {
        return materialData;
    }

    public void setMaterialData(byte materialData) {
        this.materialData = materialData;
    }

    public String getBlockData() {
        return blockData;
    }

    public void setBlockData(String blockData) {
        this.blockData = blockData;
    }

    public long getBlockDuration() {
        return blockDuration;
    }

    public void setBlockDuration(long blockDuration) {
        this.blockDuration = blockDuration;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public void setParticleCount(int particleCount) {
        this.particleCount = particleCount;
    }

    public float getParticleOffsetX() {
        return particleOffsetX;
    }

    public void setParticleOffsetX(float particleOffsetX) {
        this.particleOffsetX = particleOffsetX;
    }

    public float getParticleOffsetY() {
        return particleOffsetY;
    }

    public void setParticleOffsetY(float particleOffsetY) {
        this.particleOffsetY = particleOffsetY;
    }

    public float getParticleOffsetZ() {
        return particleOffsetZ;
    }

    public void setParticleOffsetZ(float particleOffsetZ) {
        this.particleOffsetZ = particleOffsetZ;
    }

    /**
     * Used to scale up or down a particle's size.
     *
     * This currently only works with the REDSTONE particle in 1.13 and up.
     */
    public float getParticleSize() {
        return particleSize;
    }

    public void setParticleSize(float particleSize) {
        this.particleSize = particleSize;
    }

    /**
     * If set, will run asynchronously.
     * Some effects don't support this (TurnEffect, JumpEffect)
     *
     * Generally this shouldn't be changed, unless you want to
     * make an async effect synchronous.
     */
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Extending Effect classes can use this to determine the Entity this
     * Effect is centered upon.
     *
     * This may return null, even for an Effect that was set with an Entity,
     * if the Entity gets GC'd.
     */
    public Entity getEntity() {
        return origin == null ? null : origin.getEntity();
    }
    public void setEntity(Entity entity) {
        setDynamicOrigin(new DynamicLocation(entity));
    }

    /**
     * Extending Effect classes can use this to determine the Entity this
     * Effect is targeted upon. This is probably a very rare case, such as
     * an Effect that "links" two Entities together somehow. (Idea!)
     *
     * This may return null, even for an Effect that was set with a target Entity,
     * if the Entity gets GC'd.
     */
    public Entity getTargetEntity() {
        return target == null ? null : target.getEntity();
    }

    public void setTargetEntity(Entity entity) {
        target = new DynamicLocation(entity);
    }

    /**
     * Extending Effect classes should use this method to obtain the
     * current "root" Location of the effect.
     *
     * This method will not return null when called from onRun. Effects
     * with invalid locations will be cancelled.
     */
    public final Location getLocation() {
        return origin == null ? null : origin.getLocation();
    }

    public void setLocation(Location location) {
        setDynamicOrigin(new DynamicLocation(location));
    }

    /**
     * Extending Effect classes should use this method to obtain the
     * current "target" Location of the effect.
     *
     * Unlike getLocation, this may return null.
     */
    public final Location getTarget() {
        return target == null ? null : target.getLocation();
    }

    public void setTarget(Location location) {
        target = new DynamicLocation(location);
    }

    /**
     * @deprecated Use {@link #setTarget(Location)}
     */
    public void setTargetLocation(Location location) {
        target = new DynamicLocation(location);
    }

    public DynamicLocation getDynamicOrigin() {
        return origin;
    }

    /**
     * Set the Location this Effect is centered on.
     */
    public void setDynamicOrigin(DynamicLocation location) {
        if (location == null) throw new IllegalArgumentException("Origin Location cannot be null!");
        origin = location;

        if (offset != null) origin.addOffset(offset);
        if (relativeOffset != null) origin.addRelativeOffset(relativeOffset);

        origin.setDirectionOffset(yawOffset, pitchOffset);
        origin.setYaw(yaw);
        origin.setPitch(pitch);
        origin.setUpdateLocation(updateLocations);
        origin.setUpdateDirection(updateDirections);
        origin.updateDirection();
    }

    public DynamicLocation getDynamicTarget() {
        return target;
    }

    /**
     * Set the Location this Effect is targeting.
     */
    public void setDynamicTarget(DynamicLocation location) {
        target = location;
        if (target != null && targetOffset != null) target.addOffset(targetOffset);
        if (target == null) return;
        target.setUpdateLocation(updateLocations);
        target.setUpdateDirection(updateDirections);
    }

    public DynamicLocation getOrigin() {
        return origin;
    }

    public void setOrigin(DynamicLocation origin) {
        this.origin = origin;
    }

    /**
     * This will store the base number of iterations
     */
    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    /**
     * Should this effect stop playing if the origin entity becomes invalid?
     */
    public boolean canDisappearWithOriginEntity() {
        return disappearWithOriginEntity;
    }

    /**
     * Should this effect stop playing if the target entity becomes invalid?
     */
    public boolean canDisappearWithTargetEntity() {
        return disappearWithTargetEntity;
    }

    public final boolean isDone() {
        return done;
    }

    public boolean isPlaying() {
        return playing;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Effect(EffectManager effectManager) {
        if (effectManager == null) throw new IllegalArgumentException("EffectManager cannot be null!");

        this.effectManager = effectManager;
        visibleRange = effectManager.getParticleRange();
    }

    protected List<Color> parseColorList(String colors) {
        List<Color> colorList = new ArrayList<>();
        String[] args = colors.split(",");
        if (args.length >= 1) {
            for (String str : args) {
                try {
                    int rgb = Integer.parseInt(str.trim().replace("#", ""), 16);
                    colorList.add(Color.fromRGB(rgb));
                } catch (NumberFormatException ignored) {}
            }
        }
        return colorList;
    }

    protected void initialize() {
        if (period < 1) period = 1;

        if (colors != null) {
            colorList = parseColorList(colors);
        }
        if (toColors != null) {
            toColorList = parseColorList(toColors);
        }

        if (subEffect != null) {
            subEffectClass = subEffect.getString("subEffectClass");
        }
    }

    public final void cancel() {
        cancel(true);
    }

    public final void cancel(boolean callback) {
        if (callback) done();
        else done = true;
    }

    public abstract void onRun();

    /**
     * Called when this effect is done playing (when {@link #done()} is called).
     */
    public void onDone() { }

    @Override
    public final void run() {
        if (!validate()) {
            cancel();
            return;
        }

        if (done) {
            effectManager.removeEffect(this);
            return;
        }

        try {
            if (RandomUtils.checkProbability(probability)) {
                onRun();
            }
        } catch (Exception ex) {
            done();
            effectManager.onError(ex);
        }

        if (type == EffectType.REPEATING) {
            if (iterations == -1) return;
            iterations--;
            if (iterations < 1) done();
        } else {
            done();
        }
    }

    /**
     * Effects should override this if they want to be reusable, this is called prior to starting so
     * state can be reset.
     */
    protected void reset() {
        done = false;
    }

    public void prepare() {
        reset();
        updateDuration();
    }

    public final void start() {
        prepare();
        effectManager.start(this);
        playing = true;
    }

    public final void infinite() {
        type = EffectType.REPEATING;
        iterations = -1;
    }

    protected final boolean validate() {
        // Check if the origin and target entities are present
        if (disappearWithOriginEntity && (origin != null && !origin.hasValidEntity())) return false;
        if (disappearWithTargetEntity && (target != null && !target.hasValidEntity())) return false;

        // Check for a valid Location
        updateLocation();
        updateTarget();
        Location location = getLocation();
        if (location == null) return false;
        if (autoOrient) {
            Location targetLocation = target == null ? null : target.getLocation();
            if (targetLocation != null) {
                Vector direction = targetLocation.toVector().subtract(location.toVector());
                location.setDirection(direction);
                targetLocation.setDirection(direction.multiply(-1));
            }
        }

        return true;
    }

    protected void updateDuration() {
        if (duration != null) {
            if (period < 1) period = 1;
            iterations = duration / period / 50;
        }
        maxIterations = iterations;
    }

    protected void updateLocation() {
        if (origin != null) origin.update();
    }

    protected void updateTarget() {
        if (target != null) target.update();
    }

    protected void display(Particle effect, Location location) {
        display(effect, location, color);
    }

    protected void display(Particle particle, Location location, Color color) {
        display(particle, location, color, particleData != 0 ? particleData : speed, particleCount);
    }

    protected void display(Particle particle, Location location, float speed, int amount) {
        display(particle, location, color, speed, amount);
    }

    protected void display(Particle particle, Location location, Color color, float speed, int amount) {
        display(particle, location, color, toColor, speed, amount);
    }

    protected void display(Particle particle, Location location, Color color, Color toColor, float speed, int amount) {
        // display particles only when particleCount is equal or more than 0
        if (particleCount >= 0) {
            Color currentColor = color;
            if (colorList != null && !colorList.isEmpty()) {
                currentColor = colorList.get(ThreadLocalRandom.current().nextInt(colorList.size()));
            }

            Color currentToColor = toColor;
            if (toColorList != null && !toColorList.isEmpty()) {
                currentToColor = toColorList.get(ThreadLocalRandom.current().nextInt(colorList.size()));
            }

            ParticleOptions options = new ParticleOptions(particleOffsetX, particleOffsetY, particleOffsetZ, speed, amount, particleSize, currentColor, currentToColor, arrivalTime, material, materialData, blockData, blockDuration, shriekDelay, sculkChargeRotation);
            options.target = target;

            effectManager.display(particle, options, location, visibleRange, targetPlayers);
        }

        if (subEffectClass != null) effectManager.start(subEffectClass, subEffect, location);
    }

    private void done() {
        playing = false;
        done = true;
        effectManager.done(this);
        targetPlayers = null;
        onDone();
    }

    public void reloadParameters() {
        // This can be implemented by any effect that needs to reset state when in an inner loop of a Modified effect
    }

}
