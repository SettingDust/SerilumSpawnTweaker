package settingdust.serilumspawntweaker;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SerilumSpawnTweaker {
    public static final String MOD_ID = "serilum_spawn_tweaker";

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> EXCLUDE_VILLAGE;

    public static void init() {}

    public static ForgeConfigSpec serverConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Village Spawn Point");
        EXCLUDE_VILLAGE = builder.defineList(
                "Exclude Village",
                () -> Lists.newArrayList(),
                it -> it instanceof String s && ResourceLocation.isValidResourceLocation(s));
        builder.pop();
        return builder.build();
    }

    @NotNull
    public static List<Holder<Structure>> spawnVillages(ServerLevel serverLevel) {
        return serverLevel
                .registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getOrCreateTag(StructureTags.VILLAGE)
                .stream()
                .filter(village -> !village.is(
                        it -> EXCLUDE_VILLAGE.get().contains(it.location().toString())))
                .toList();
    }
}
