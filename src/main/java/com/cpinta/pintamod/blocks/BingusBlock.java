package com.cpinta.pintamod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.ChatMessageContent;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingPlayerChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BingusBlock extends DirectionalBlock {
    /**
     * EATING = 0 means empty
     *          1 means chewing
     *          2 means full
     */
    public static final IntegerProperty EATING = IntegerProperty.create("eating", 0, 2);
    public int chewingTime = 10;
    public static int chewingTimer = 0;

    public BingusBlock(Properties properties){
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
        );

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
        builder.add(EATING);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(stateDefinition.any().getValue(EATING) == 1){
            return InteractionResult.PASS;
        }

        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        List<String> items = Arrays.asList(
                "item.minecraft.cocoa_beans",
                "item.minecraft.cookie");

        if(items.contains(itemstack.getItem().getDescriptionId().toString())){
            if(pState.getValue(EATING) == 0){
                pLevel.setBlockAndUpdate(pPos, pState.setValue(EATING, Integer.valueOf(1)));
                itemstack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }

    /**
     * Spawns the given amount of experience into the Level as experience orb entities.
     */
    @Override
    public void popExperience(ServerLevel pLevel, BlockPos pPos, int pAmount) {
        if (pLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !pLevel.restoringBlockSnapshots) {
            ExperienceOrb.award(pLevel, Vec3.atCenterOf(pPos), pAmount);
        }

    }

    /**
     * Called when an Entity lands on this Block.
     * This method is responsible for doing any modification on the motion of the entity that should result from the
     * landing.
     */
    @Override
    public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
        super.updateEntityAfterFallOn(pLevel, pEntity);


        BlockPos blockPos = pEntity.blockPosition();
        BlockPos underBlockPos = blockPos;
        underBlockPos = underBlockPos.below();
        Direction dir = pLevel.getBlockState(underBlockPos).getValue(FACING);
        Vec3i vec3iDir = dir.getNormal();

        Vec3 blockOffset = new Vec3(0, 0, 0);

        if(vec3iDir.getX()<0){
            blockPos = blockPos.east();
            blockOffset = new Vec3(0, 0, 0.5f);
            blockOffset = new Vec3(0, 0, 1f);
        }
        if(vec3iDir.getX()>0){
            blockOffset = new Vec3(0, 0, 0.5f);
            blockOffset = new Vec3(0, 0, 1f);
        }
        if(vec3iDir.getZ()<0){
            blockPos = blockPos.south();
            blockOffset = new Vec3(0.5f, 0, 0);
            blockOffset = new Vec3(1, 0, 0);
        }
        if(vec3iDir.getZ()>0){
            blockOffset = new Vec3(0.5f, 0, 0);
            blockOffset = new Vec3(1, 0, 0);
        }

        Snowball snowball = new Snowball(pEntity.level, (LivingEntity) pEntity);
        snowball.setPos(new Vec3(blockPos.getX() +blockOffset.x(), blockPos.getY()-1, blockPos.getZ() + blockOffset.z()));
        double d0 = blockPos.getY() - (double)1.1F;
        double d1 = vec3iDir.getX();
        double d2 = d0 - snowball.getY();
        double d3 = vec3iDir.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 1.6F, 6.0F);
        pEntity.level.addFreshEntity(snowball);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        super.tick(state, worldIn, pos, rand);
        int eatingValue = state.getValue(EATING);
        if(eatingValue == 1){
            worldIn.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(2)));
//            if(chewingTimer > 0){
//                chewingTimer--;
//            }
//            else{
//                chewingTimer = chewingTime;
//            }
        }
    }
}
