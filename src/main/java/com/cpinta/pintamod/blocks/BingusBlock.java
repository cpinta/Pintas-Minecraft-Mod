package com.cpinta.pintamod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;


public class BingusBlock extends DirectionalBlock {
    /**
     * EATING = 0 means empty
     *          1 means chewing
     *          2 means full
     */

    public static final IntegerProperty EATING = IntegerProperty.create("eating", 0, 3);
    public int spewingTime = 0;
    public int spewingAmount = 0;
    public int spewPerCookie = 5;

    int EATING_EMPTY = 0;
    int EATING_CHEWING = 1;
    int EATING_FULL = 2;
    int EATING_SPEWING = 3;

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
            itemstack.shrink(1);
            FeedChocolate(pState, pLevel, pPos, pPlayer);
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

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {

        int eatingValue = pState.getValue(EATING);
        if(eatingValue == EATING_SPEWING){
            spewingTime++;
            if(spewingTime>1){
                if(spewingAmount > 0){
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(EATING, Integer.valueOf(EATING_FULL)));
                }
                else{
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(EATING, Integer.valueOf(EATING_EMPTY)));
                }
            }
        }
    }

    public void FeedChocolate(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer){
        if(pState.getValue(EATING) != EATING_SPEWING){
            pLevel.setBlockAndUpdate(pPos, pState.setValue(EATING, Integer.valueOf(EATING_CHEWING)));
            spewingAmount += spewPerCookie;
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



        BlockPos blockPos = pEntity.blockPosition().below();
        BlockPos underBlockPos = blockPos;
        Direction dir = pLevel.getBlockState(underBlockPos).getValue(FACING);
        int eatingValue = pLevel.getBlockState(underBlockPos).getValue(EATING);
        if(eatingValue != 2 || eatingValue != 3){

        }

        Vec3i vec3iDir = dir.getNormal();

        Vec3 underBlockVec = new Vec3(underBlockPos.getX(), underBlockPos.getY(), underBlockPos.getZ());

        underBlockVec = underBlockVec.add(0.5f + (vec3iDir.getX() * 0.75f), 0, 0.5f + (vec3iDir.getZ() * 0.75f));

        Snowball snowball = new Snowball(pEntity.level, underBlockVec.x, underBlockVec.y, underBlockVec.z);
        double d0 = underBlockPos.getY();
        double d1 = vec3iDir.getX();
        double d2 = d0 - snowball.getY();
        double d3 = vec3iDir.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 1.6F, 6.0F);
        pEntity.level.addFreshEntity(snowball);
        pEntity.level.setBlockAndUpdate(blockPos, pLevel.getBlockState(blockPos).setValue(EATING, Integer.valueOf(EATING_SPEWING)));
        spewingTime = 0;
        spewingAmount--;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        super.tick(state, worldIn, pos, rand);
        int eatingValue = state.getValue(EATING);
        if(eatingValue == EATING_CHEWING){
            worldIn.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(EATING_FULL)));
        }
    }
}
