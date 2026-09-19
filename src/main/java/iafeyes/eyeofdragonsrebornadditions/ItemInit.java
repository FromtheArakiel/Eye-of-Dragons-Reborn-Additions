package iafeyes.eyeofdragonsrebornadditions;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EyeOfDragonsRebornAdditionsMod.MODID);

    public static final RegistryObject<Item> EYE_OF_HYDRA = ITEMS.register("eye_of_hydra", () ->
            new ItemHydraEye(
                    new Item.Properties()
                            .stacksTo(16)));
}