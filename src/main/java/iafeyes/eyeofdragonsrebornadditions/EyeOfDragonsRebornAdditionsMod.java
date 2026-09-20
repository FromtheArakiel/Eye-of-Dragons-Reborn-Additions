package iafeyes.eyeofdragonsrebornadditions;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(EyeOfDragonsRebornAdditionsMod.MODID)
public class EyeOfDragonsRebornAdditionsMod {
    public static final String MODID = "eyeofdragonsrebornadditions";

    private static final ResourceKey<CreativeModeTab> TARGET_TAB = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath("eyeofdragonsreborn", "eyeofdragonsreborn_tab"));

    @SuppressWarnings("removal")
    public EyeOfDragonsRebornAdditionsMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(bus);

        Path configDir = FMLPaths.CONFIGDIR.get().resolve("eyeofdragonsreborn");
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config directory for " + MODID, e);
        }

        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                EyeOfDragonsRebornAdditionsConfig.SPEC,
                "eyeofdragonsreborn/" + MODID + "-common.toml");

        bus.addListener(this::onBuildTabContents);
    }

    private void onBuildTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(TARGET_TAB)) {
            event.accept(ItemInit.EYE_OF_HYDRA.get());
            event.accept(ItemInit.EYE_OF_SIREN.get());
            event.accept(ItemInit.EYE_OF_CYCLOPS.get());
        }
    }
}