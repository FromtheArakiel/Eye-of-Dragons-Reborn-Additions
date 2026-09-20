package iafeyes.eyeofdragonsrebornadditions;

import com.iafenvoy.iceandfire.entity.EntityCyclops;
import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ItemCyclopsEye extends ItemSpecialEyeBase {
    public ItemCyclopsEye(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSearchRadiusForDimension(String dimId) {
        return EyeOfDragonsRebornAdditionsConfig.CYCLOPS_EYE.searchRadiusForDimension(dimId);
    }

    @Override
    protected boolean isDimensionAllowed(String dimId) {
        return EyeOfDragonsRebornAdditionsConfig.CYCLOPS_EYE.isDimensionAllowed(dimId);
    }

    @Override
    protected String getNotFoundKey() {
        return "eyeofdragonsrebornadditions.cyclops_eye.nonfound";
    }

    @Override
    protected String getWrongDimensionKey() {
        return "eyeofdragonsrebornadditions.cyclops_eye.wrong_dimension";
    }

    @Override
    protected SoundEvent getUseSound() {
        return IafSounds.CYCLOPS_IDLE.get();
    }

    @Override
    protected List<Entity> findNearbyEntities(Level level, Player player, int radius) {
        AABB bb = new AABB(player.blockPosition()).inflate(radius);
        return level.getEntitiesOfClass(EntityCyclops.class, bb)
                .stream()
                .filter(EntityCyclops::isAlive)
                .map(e -> (Entity) e)
                .toList();
    }
}