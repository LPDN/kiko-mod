package com.jaciel.kiko.command;

import com.jaciel.kiko.animation.KikoAnimation;
import com.jaciel.kiko.entity.KikoEntity;
import com.jaciel.kiko.interaction.KikoInteractionHandler;
import com.jaciel.kiko.outfit.KikoOutfit;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Comandos (actúan sobre la Kiko más cercana en 16 bloques).
 *
 *  /kiko outfit <school|casual1|casual2>
 *  /kiko status
 *  /kiko follow
 *  /kiko affection <valor>      (solo operadores, para pruebas)
 */
public final class KikoCommands {
    private KikoCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(CommandManager.literal("kiko")
                        .then(CommandManager.literal("outfit")
                                .then(CommandManager.argument("outfit", StringArgumentType.word())
                                        .suggests((ctx, builder) -> CommandSource.suggestMatching(
                                                Arrays.stream(KikoOutfit.values()).map(KikoOutfit::id), builder))
                                        .executes(KikoCommands::setOutfit)))
                        .then(CommandManager.literal("status").executes(KikoCommands::status))
                        .then(CommandManager.literal("follow").executes(KikoCommands::toggleFollow))
                        .then(CommandManager.literal("date")
                                .then(CommandManager.argument("place", StringArgumentType.word())
                                        .suggests((ctx, builder) -> CommandSource.suggestMatching(
                                                java.util.List.of("feria", "mirador", "muelle", "cena", "cancel"), builder))
                                        .executes(ctx -> {
                                            var p = ctx.getSource().getPlayerOrThrow();
                                            Text t = com.jaciel.kiko.romance.Dates.start(p, StringArgumentType.getString(ctx, "place"));
                                            ctx.getSource().sendFeedback(() -> t, false);
                                            return 1;
                                        })))
                        .then(CommandManager.literal("birthday")
                                .executes(ctx -> {
                                    KikoEntity k = requireAny(ctx);
                                    if (k == null) return 0;
                                    int days = com.jaciel.kiko.town.Birthdays.daysUntil(k);
                                    ctx.getSource().sendFeedback(() -> days == 0
                                            ? Text.translatable("command.kiko.birthday_today", k.getDisplayName())
                                            : Text.translatable("command.kiko.birthday_in", k.getDisplayName(), days), false);
                                    return 1;
                                })
                                .then(CommandManager.literal("force")
                                        .requires(src -> src.hasPermissionLevel(2))
                                        .executes(ctx -> {
                                            KikoEntity k = requireAny(ctx);
                                            if (k == null) return 0;
                                            com.jaciel.kiko.town.Birthdays.force(k);
                                            ctx.getSource().sendFeedback(() -> Text.translatable("command.kiko.birthday_today", k.getDisplayName()), false);
                                            return 1;
                                        })))
                        .then(CommandManager.literal("town")
                                .requires(src -> src.hasPermissionLevel(2))
                                .then(CommandManager.literal("grow").executes(ctx -> {
                                    var src = ctx.getSource();
                                    src.sendFeedback(() -> com.jaciel.kiko.town.TownGrowth.cmdGrow(src.getWorld(), BlockPos.ofFloored(src.getPosition())), false);
                                    return 1;
                                }))
                                .then(CommandManager.literal("finish").executes(ctx -> {
                                    var src = ctx.getSource();
                                    src.sendFeedback(() -> com.jaciel.kiko.town.TownGrowth.cmdFinish(src.getWorld()), false);
                                    return 1;
                                }))
                                .then(CommandManager.literal("guards").executes(ctx -> {
                                    var src = ctx.getSource();
                                    src.sendFeedback(() -> com.jaciel.kiko.town.TownRaid.cmdGuards(src.getWorld(), BlockPos.ofFloored(src.getPosition())), false);
                                    return 1;
                                }))
                                .then(CommandManager.literal("fiesta").executes(ctx -> {
                                    var src = ctx.getSource();
                                    src.sendFeedback(() -> com.jaciel.kiko.town.TownFestival.cmdToggle(src.getWorld(), BlockPos.ofFloored(src.getPosition())), false);
                                    return 1;
                                }))
                                .then(CommandManager.literal("school").executes(ctx -> {
                                    var src = ctx.getSource();
                                    src.sendFeedback(() -> com.jaciel.kiko.town.TownGrowth.cmdSchool(src.getWorld(), BlockPos.ofFloored(src.getPosition())), false);
                                    return 1;
                                }))
                                .then(CommandManager.literal("raid")
                                        .executes(ctx -> {
                                            var src = ctx.getSource();
                                            src.sendFeedback(() -> com.jaciel.kiko.town.TownRaid.cmdStart(src.getWorld(), BlockPos.ofFloored(src.getPosition())), false);
                                            return 1;
                                        })
                                        .then(CommandManager.literal("stop").executes(ctx -> {
                                            var src = ctx.getSource();
                                            src.sendFeedback(() -> com.jaciel.kiko.town.TownRaid.cmdStop(src.getWorld()), false);
                                            return 1;
                                        })))
                                .then(CommandManager.literal("status").executes(ctx -> {
                                    var src = ctx.getSource();
                                    src.sendFeedback(() -> com.jaciel.kiko.town.TownGrowth.cmdStatus(src.getWorld(), BlockPos.ofFloored(src.getPosition())), false);
                                    return 1;
                                })))
                        .then(CommandManager.literal("affection")
                                .requires(src -> src.hasPermissionLevel(2))
                                .then(CommandManager.argument("value", FloatArgumentType.floatArg(-100, 100))
                                        .executes(KikoCommands::setAffection)))));
    }

    private static KikoEntity nearest(ServerPlayerEntity player) {
        List<KikoEntity> list = player.getWorld().getEntitiesByClass(KikoEntity.class,
                player.getBoundingBox().expand(16), KikoEntity::isAlive);
        return list.stream().min(Comparator.comparingDouble(k -> k.squaredDistanceTo(player))).orElse(null);
    }

    /** El más cercano de cualquier tipo (compañera, hijo o vecino). */
    private static KikoEntity requireAny(CommandContext<ServerCommandSource> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        KikoEntity k = nearest(player);
        if (k == null) ctx.getSource().sendError(Text.translatable("command.kiko.none"));
        return k;
    }

    private static KikoEntity requireKiko(CommandContext<ServerCommandSource> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        KikoEntity kiko = nearest(player);
        if (kiko == null) ctx.getSource().sendError(Text.translatable("command.kiko.none"));
        return kiko;
    }

    private static int setOutfit(CommandContext<ServerCommandSource> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        KikoOutfit outfit = KikoOutfit.byId(StringArgumentType.getString(ctx, "outfit"));
        if (outfit == null) {
            ctx.getSource().sendError(Text.translatable("command.kiko.bad_outfit"));
            return 0;
        }
        KikoEntity kiko = requireKiko(ctx);
        if (kiko == null) return 0;
        kiko.setChosenOutfit(outfit);
        kiko.playAnimation(KikoAnimation.HAPPY);
        ctx.getSource().sendFeedback(() -> Text.translatable("command.kiko.outfit_set", Text.translatable(outfit.translationKey())), false);
        return 1;
    }

    private static int status(CommandContext<ServerCommandSource> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        KikoEntity kiko = requireKiko(ctx);
        if (kiko == null) return 0;
        KikoInteractionHandler.showStatus(ctx.getSource().getPlayerOrThrow(), kiko);
        return 1;
    }

    private static int toggleFollow(CommandContext<ServerCommandSource> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        KikoEntity kiko = requireKiko(ctx);
        if (kiko == null) return 0;
        kiko.setFollowEnabled(!kiko.isFollowEnabled());
        KikoInteractionHandler.say(ctx.getSource().getPlayerOrThrow(), kiko,
                kiko.isFollowEnabled() ? "dialogue.kiko.follow_on" : "dialogue.kiko.follow_off", 1);
        return 1;
    }

    private static int setAffection(CommandContext<ServerCommandSource> ctx) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        KikoEntity kiko = requireKiko(ctx);
        if (kiko == null) return 0;
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        float value = FloatArgumentType.getFloat(ctx, "value");
        kiko.getAffection().set(player.getUuid(), value, kiko.getWorld().getTime());
        KikoInteractionHandler.showStatus(player, kiko);
        return 1;
    }
}
