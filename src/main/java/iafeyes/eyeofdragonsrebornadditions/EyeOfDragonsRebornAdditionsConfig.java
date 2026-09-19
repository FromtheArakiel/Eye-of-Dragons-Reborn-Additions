package iafeyes.eyeofdragonsrebornadditions;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class EyeOfDragonsRebornAdditionsConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue SEARCH_RADIUS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSION_RADIUS_OVERRIDES;
    public static final ForgeConfigSpec.BooleanValue USE_DIMENSION_WHITELIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSION_LIST;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Settings for Hydra Eye").push("hydra_eye");

        SEARCH_RADIUS = builder
                .comment("Default search radius (in blocks) when using the Hydra Eye.",
                        "Default: 1000")
                .defineInRange("searchRadius", 1000, 1, 10000);

        DIMENSION_RADIUS_OVERRIDES = builder
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

        USE_DIMENSION_WHITELIST = builder
                .comment("If true, dimensionList is a whitelist (only listed dimensions allow searching).",
                        "If false, it's a blacklist (listed dimensions are blocked).",
                        "Default: false (blacklist mode)")
                .define("useDimensionWhitelist", false);

        DIMENSION_LIST = builder
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

        SPEC = builder.build();
    }

    public static int getSearchRadiusForDimension(String dimensionId) {
        List<? extends String> overrides = DIMENSION_RADIUS_OVERRIDES.get();
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
        return SEARCH_RADIUS.get();
    }

    public static boolean isDimensionAllowed(String dimensionId) {
        List<? extends String> list = DIMENSION_LIST.get();
        boolean useWhitelist = USE_DIMENSION_WHITELIST.get();
        boolean inList = list.contains(dimensionId);
        return useWhitelist == inList;
    }
}