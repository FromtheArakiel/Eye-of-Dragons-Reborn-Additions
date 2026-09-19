package iafeyes.eyeofdragonsrebornadditions;

import com.iafenvoy.iceandfire.entity.EntityHydra;
import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ItemHydraEye extends ItemSpecialEyeBase {
    public ItemHydraEye(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSearchRadiusForDimension(String dimId) {
        return EyeOfDragonsRebornAdditionsConfig.getSearchRadiusForDimension(dimId);
    }

    @Override
    protected boolean isDimensionAllowed(String dimId) {
        return EyeOfDragonsRebornAdditionsConfig.isDimensionAllowed(dimId);
    }

    @Override
    protected String getNotFoundKey() {
        return "eyeofdragonsrebornadditions.hydra_eye.nonfound";
    }

    @Override
    protected String getWrongDimensionKey() {
        return "eyeofdragonsrebornadditions.hydra_eye.wrong_dimension";
    }

    @Override
    protected SoundEvent getUseSound() {
        return IafSounds.HYDRA_IDLE.get();
    }

    @Override
    protected List<Entity> findNearbyEntities(Level level, Player player, int radius) {
        AABB bb = new AABB(player.blockPosition()).inflate(radius);
        return level.getEntitiesOfClass(EntityHydra.class, bb)
                .stream()
                .filter(EntityHydra::isAlive)
                .map(e -> (Entity) e)
                .toList();
    }
}