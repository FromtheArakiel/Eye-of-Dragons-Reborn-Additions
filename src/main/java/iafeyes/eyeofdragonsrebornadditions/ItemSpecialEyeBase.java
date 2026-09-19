package iafeyes.eyeofdragonsrebornadditions;

import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public abstract class ItemSpecialEyeBase extends Item {
    public ItemSpecialEyeBase(Properties properties) {
        super(properties);
    }

    protected abstract int getSearchRadiusForDimension(String dimId);

    protected abstract boolean isDimensionAllowed(String dimId);

    protected abstract String getNotFoundKey();

    protected abstract String getWrongDimensionKey();

    protected abstract SoundEvent getUseSound();

    protected abstract List<Entity> findNearbyEntities(Level level, Player player, int radius);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        if (!level.isClientSide()) {
            String dimId = level.dimension().location().toString();
            if (!isDimensionAllowed(dimId)) {
                player.displayClientMessage(Component.translatable(getWrongDimensionKey()), true);
                return InteractionResultHolder.success(itemstack);
            }
            findAndShoot(level, player, itemstack, dimId);
        }

        return InteractionResultHolder.success(itemstack);
    }

    private void findAndShoot(Level level, Player player, ItemStack itemstack, String dimId) {
        int radius = getSearchRadiusForDimension(dimId);
        List<Entity> entities = findNearbyEntities(level, player, radius);

        if (entities.isEmpty()) {
            player.displayClientMessage(Component.translatable(getNotFoundKey()), true);
            return;
        }

        double nearestDistance = Double.MAX_VALUE;
        Entity nearest = null;
        for (Entity entity : entities) {
            double distance = entity.distanceTo(player);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = entity;
            }
        }

        EyeOfEnder finder = new EyeOfEnder(level, player.getX(), player.getEyeY(), player.getZ());
        finder.setItem(itemstack);
        finder.signalTo(nearest.blockPosition());
        level.addFreshEntity(finder);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                getUseSound(), SoundSource.NEUTRAL,
                1F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
        level.levelEvent(null, 1003, player.blockPosition(), 0);

        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
    }
}