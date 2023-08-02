package gg.hipposgrumm.torchflower_arrows.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class Config {
    public static class As_Common {
        final ForgeConfigSpec.BooleanValue allowMultishotTorchflowers;

        private As_Common(ForgeConfigSpec.Builder builder) {
            builder.push("Features");
            allowMultishotTorchflowers = builder
                    .comment(" Allow all arrows from multishot crossbows to place torchflowers. This allows players to light up areas easier but also allows for easy \"duplication\" of torchflowers.")
                    .define("Allow Multishot Torchflowers", true);
            builder.pop();
        }
    }

    public static boolean allowMultishotTorchflowers() {
        return COMMON.allowMultishotTorchflowers.get();
    }

    public static final As_Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<As_Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(As_Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }
}