package iafeyes.eyeofdragonsrebornadditions;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class EyeOfDragonsRebornAdditionsConfig {
    public static final ForgeConfigSpec SPEC;

    public static final EyeConfig HYDRA_EYE;
    public static final EyeConfig SIREN_EYE;
    public static final EyeConfig CYCLOPS_EYE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        HYDRA_EYE = new EyeConfig(builder, "hydra_eye", "Hydra Eye");
        SIREN_EYE = new EyeConfig(builder, "siren_eye", "Siren Eye");
        CYCLOPS_EYE = new EyeConfig(builder, "cyclops_eye", "Cyclops Eye");

        SPEC = builder.build();
    }

    public static class EyeConfig {
        public final ForgeConfigSpec.IntValue searchRadius;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> dimensionRadiusOverrides;
        public final ForgeConfigSpec.BooleanValue useDimensionWhitelist;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> dimensionList;

        public EyeConfig(ForgeConfigSpec.Builder builder, String path, String name) {
            builder.comment("Settings for " + name).push(path);

            searchRadius = builder
                    .comment("Default search radius (in blocks) when using this eye.",
                            "Default: 1000")
                    .defineInRange("searchRadius", 1000, 1, 10000);

            dimensionRadiusOverrides = builder
                    .comment("Per-dimension search radius overrides.",
                            "Format: \"<dimension_id>:<radius>\"",
                            "Example: [\"minecraft:the_nether:500\", \"minecraft:the_end:200\"]",
                            "Note: Dimension-specific radius takes priority over the default search radius.",
                            "Default: []")
                    .defineListAllowEmpty(
                            "dimensionRadiusOverrides",
                            List.of(),
                            o -> o instanceof String s && s.contains(":")
                    );

            useDimensionWhitelist = builder
                    .comment("If true, dimensionList is a whitelist (only listed dimensions allow searching).",
                            "If false, it's a blacklist (listed dimensions are blocked).",
                            "Default: false (blacklist mode)")
                    .define("useDimensionWhitelist", false);

            dimensionList = builder
                    .comment("List of dimension IDs, e.g. \"minecraft:overworld\", \"minecraft:the_nether\", \"minecraft:the_end\".",
                            "Behavior depends on useDimensionWhitelist.",
                            "Default: [] (empty)",
                            "With blacklist mode + empty list, all dimensions are allowed.",
                            "With whitelist mode + empty list, all dimensions are blocked.")
                    .defineListAllowEmpty(
                            "dimensionList",
                            List.of(),
                            o -> o instanceof String s && !s.isEmpty()
                    );

            builder.pop();
        }

        public int searchRadiusForDimension(String dimensionId) {
            List<? extends String> overrides = dimensionRadiusOverrides.get();
            for (String entry : overrides) {
                int idx = entry.lastIndexOf(':');
                if (idx > 0) {
                    String dim = entry.substring(0, idx);
                    if (dim.equals(dimensionId)) {
                        try {
                            return Integer.parseInt(entry.substring(idx + 1));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
            return searchRadius.get();
        }

        public boolean isDimensionAllowed(String dimensionId) {
            List<? extends String> list = dimensionList.get();
            boolean useWhitelist = useDimensionWhitelist.get();
            boolean inList = list.contains(dimensionId);
            return useWhitelist == inList;
        }
    }
}