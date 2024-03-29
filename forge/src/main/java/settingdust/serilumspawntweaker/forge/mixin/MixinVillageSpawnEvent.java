package settingdust.serilumspawntweaker.forge.mixin;

import com.mojang.datafixers.util.Pair;
import com.natamus.villagespawnpoint_common_forge.events.VillageSpawnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import settingdust.serilumspawntweaker.SerilumSpawnTweaker;

@Mixin(VillageSpawnEvent.class)
public class MixinVillageSpawnEvent {
    @Shadow(remap = false) @Final private static Logger logger;

    @Redirect(
            method = "onWorldLoad*",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lcom/natamus/collective_common_forge/functions/BlockPosFunctions;getCenterNearbyVillage(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/core/BlockPos;"),
            remap = false)
    private static BlockPos spawntweaker$filterBlacklist(ServerLevel serverLevel) {
        Pair<BlockPos, Holder<Structure>> structure = serverLevel
                .getChunkSource()
                .getGenerator()
                .findNearestMapStructure(
                        serverLevel,
                        HolderSet.direct(SerilumSpawnTweaker.spawnVillages(serverLevel)),
                        BlockPos.ZERO,
                        6400,
                        false);
        if (structure == null) {
            logger.info("[Village Spawn Point] Can't find any villages");
            return null;
        }
        final var structureStart = serverLevel
                .structureManager()
                .startsForStructure(
                        SectionPos.of(structure.getFirst()),
                        structure.getSecond().value())
                .stream()
                .findFirst()
                .orElseThrow();

        final var center = structureStart.getBoundingBox().getCenter();

        logger.info("[Village Spawn Point] Village found: "
                    + structure.getSecond().unwrapKey().orElseThrow().location() + " center at "
                    + center.toShortString());
        return center;
    }
}
