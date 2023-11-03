package settingdust.serilumspawntweaker.forge.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.datafixers.util.Pair;
import com.natamus.biomespawnpoint_common_forge.data.Constants;
import com.natamus.biomespawnpoint_common_forge.events.BiomeSpawnEvent;
import com.natamus.biomespawnpoint_common_forge.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import settingdust.serilumspawntweaker.SerilumSpawnTweaker;

@Mixin(BiomeSpawnEvent.class)
public class MixinBiomeSpawnEvent {
    @Redirect(
            method = "onWorldLoad",
            remap = false,
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcom/natamus/collective_common_forge/functions/BlockPosFunctions;getCenterNearbyBiome(Lnet/minecraft/server/level/ServerLevel;Ljava/lang/String;)Lnet/minecraft/core/BlockPos;"))
    private static BlockPos serilumspawntweaker$findMoreBiome(
            ServerLevel serverLevel,
            String ignored,
            @Share("foundedBiome") LocalRef<Pair<BlockPos, Holder<Biome>>> foundedBiome) {
        foundedBiome.set(serverLevel.findClosestBiome3d(
                biome -> biome.is(
                        it -> Util.getSpawnBiomes().contains(it.location().toString())),
                new BlockPos(0, 0, 0),
                6400,
                32,
                64));
        if (foundedBiome.get() == null) return null;
        return foundedBiome.get().getFirst();
    }

    @ModifyArg(
            method = "onWorldLoad",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", ordinal = 2),
            remap = false)
    private static String serilumspawntweaker$correctLog(String msg) {
        return "[Biome Spawn Point] Finding the nearest biome. This might take a few seconds.";
    }

    @ModifyArg(
            method = "onWorldLoad",
            at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", ordinal = 3),
            remap = false)
    private static String serilumspawntweaker$logBiome(
            String msg, @Share("foundedBiome") LocalRef<Pair<BlockPos, Holder<Biome>>> foundedBiome) {
        return "[Biome Spawn Point] Biome found: "
                + foundedBiome.get().getSecond().unwrapKey().orElseThrow().location();
    }

    @Redirect(
            method = "onWorldLoad",
            remap = false,
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcom/natamus/collective_common_forge/functions/BlockPosFunctions;getNearbyVillage(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/BlockPos;"))
    private static BlockPos spawntweaker$filterBlacklist(ServerLevel serverLevel, BlockPos nearPos) {
        Pair<BlockPos, Holder<Structure>> structure = serverLevel
                .getChunkSource()
                .getGenerator()
                .findNearestMapStructure(
                        serverLevel,
                        HolderSet.direct(SerilumSpawnTweaker.spawnVillages(serverLevel)),
                        nearPos,
                        6400,
                        false);
        if (structure == null) return null;
        Constants.logger.info("[Biome Spawn Point] Village found: "
                + structure.getSecond().unwrapKey().orElseThrow().location());
        return structure.getFirst();
    }
}
