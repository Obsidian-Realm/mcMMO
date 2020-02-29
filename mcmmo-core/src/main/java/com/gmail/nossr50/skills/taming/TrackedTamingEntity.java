package com.gmail.nossr50.skills.taming;

import com.gmail.nossr50.datatypes.skills.subskills.taming.CallOfTheWildType;
import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.mcmmo.api.platform.scheduler.Task;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.function.Consumer;

public class TrackedTamingEntity implements Consumer<Task> {
    private LivingEntity livingEntity;
    private final CallOfTheWildType callOfTheWildType;
    private UUID id;
    private final TamingManager tamingManagerRef;
    private final mcMMO pluginRef;

    protected TrackedTamingEntity(mcMMO pluginRef, LivingEntity livingEntity, CallOfTheWildType callOfTheWildType, TamingManager tamingManagerRef) {
        this.pluginRef = pluginRef;
        this.tamingManagerRef = tamingManagerRef;
        this.callOfTheWildType = callOfTheWildType;
        this.livingEntity = livingEntity;
        this.id = livingEntity.getUniqueId();

        int tamingCOTWLength = pluginRef.getConfigManager().getConfigTaming().getSubSkills().getCallOfTheWild().getCOTWSummon(callOfTheWildType).getSummonLifespan();

        if (tamingCOTWLength > 0) {
            pluginRef.getPlatformProvider().getScheduler().getTaskBuilder()
                    .setDelay(tamingCOTWLength * pluginRef.getMiscTools().TICK_CONVERSION_FACTOR)
                    .setTask(this)
                    .schedule();
        }
    }

    @Override
    public void accept(Task task) {
        if (livingEntity.isValid()) {
            Location location = livingEntity.getLocation();
            location.getWorld().playSound(location, Sound.BLOCK_FIRE_EXTINGUISH, 0.8F, 0.8F);
            pluginRef.getParticleEffectUtils().playCallOfTheWildEffect(livingEntity);
            pluginRef.getCombatTools().dealDamage(livingEntity, livingEntity.getMaxHealth(), EntityDamageEvent.DamageCause.SUICIDE, livingEntity);

            if(tamingManagerRef != null)
                tamingManagerRef.removeFromTracker(this);

            livingEntity.setHealth(0);
            livingEntity.remove();
        }

        task.cancel();
    }

    public CallOfTheWildType getCallOfTheWildType() {
        return callOfTheWildType;
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

    public UUID getID() {
        return id;
    }
}