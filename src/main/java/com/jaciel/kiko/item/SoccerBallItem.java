package com.jaciel.kiko.item;

import com.jaciel.kiko.entity.SoccerBallEntity;
import com.jaciel.kiko.registry.ModEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/** La pelota de futbol en la mano. Clic derecho en un bloque la pone ahí, clic derecho al aire la avientas. */
public class SoccerBallItem extends Item {
    public SoccerBallItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;
        BlockPos pos = ctx.getBlockPos().offset(ctx.getSide());
        Vec3d at = ctx.getHitPos();
        SoccerBallEntity ball = ModEntities.SOCCER_BALL.create(world);
        if (ball == null) return ActionResult.FAIL;
        double y = ctx.getSide() == net.minecraft.util.math.Direction.UP ? at.y : pos.getY();
        ball.refreshPositionAndAngles(at.x, y, at.z, 0f, 0f);
        if (!world.isSpaceEmpty(ball)) ball.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0f, 0f);
        world.spawnEntity(ball);
        world.playSound(null, pos, SoundEvents.ENTITY_SLIME_JUMP_SMALL, SoundCategory.NEUTRAL, 0.5f, 1.5f);
        PlayerEntity p = ctx.getPlayer();
        if (p == null || !p.getAbilities().creativeMode) ctx.getStack().decrement(1);
        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.success(stack);
        SoccerBallEntity ball = ModEntities.SOCCER_BALL.create(world);
        if (ball == null) return TypedActionResult.fail(stack);
        ball.refreshPositionAndAngles(user.getX(), user.getEyeY() - 0.4, user.getZ(), 0f, 0f);
        world.spawnEntity(ball);
        ball.kick(user.getRotationVec(1f), 0.6, 0.35 - user.getRotationVec(1f).y * -0.3, user);
        if (!user.getAbilities().creativeMode) stack.decrement(1);
        return TypedActionResult.consume(stack);
    }
}
