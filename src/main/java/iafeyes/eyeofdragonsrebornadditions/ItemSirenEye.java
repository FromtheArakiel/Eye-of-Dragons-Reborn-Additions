package iafeyes.eyeofdragonsrebornadditions;

import com.iafenvoy.iceandfire.entity.EntitySiren;
import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ItemSirenEye extends ItemSpecialEyeBase {
    public ItemSirenEye(Properties properties) {
        super(properties);
    }

    @Override
    protected int getSearchRadiusForDimension(String dimId) {
        return EyeOfDragonsRebornAdditionsConfig.SIREN_EYE.searchRadiusForDimension(dimId);
    }

    @Override
    protected boolean isDimensionAllowed(String dimId) {
        return EyeOfDragonsRebornAdditionsConfig.SIREN_EYE.isDimensionAllowed(dimId);
    }

    @Override
    protected String getNotFoundKey() {
        return "eyeofdragonsrebornadditions.siren_eye.nonfound";
    }

    @Override
    protected String getWrongDimensionKey() {
        return "eyeofdragonsrebornadditions.siren_eye.wrong_dimension";
    }

    @Override
    protected SoundEvent getUseSound() {
        return IafSounds.SIREN_SONG.get();
    }

    @Override
    protected List<Entity> findNearbyEntities(Level level, Player player, int radius) {
        AABB bb = new AABB(player.blockPosition()).inflate(radius);
        return level.getEntitiesOfClass(EntitySiren.class, bb)
                .stream()
                .filter(EntitySiren::isAlive)
                .map(e -> (Entity) e)
                .toList();
    }
}