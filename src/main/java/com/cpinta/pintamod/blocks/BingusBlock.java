package com.cpinta.pintamod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.ChatMessageContent;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingPlayerChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
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
        List<String> items = Arrays.asList("item.minecraft.cocoa_beans", "item.minecraft.cookie");
        if(items.contains(itemstack.getItem().getDescriptionId().toString())){
            PlayerChatMessage chatMessage = PlayerChatMessage.system(new ChatMessageContent("its cocoa beans ong"));
            pPlayer.createCommandSourceStack().sendChatMessage(OutgoingPlayerChatMessage.create(chatMessage), true, ChatType.bind(ChatType.CHAT, pPlayer));

            pLevel.setBlockAndUpdate(pPos, pState.setValue(EATING, Integer.valueOf(1)));
            if(stateDefinition.any().getValue(EATING) == 0){
                stateDefinition.any().setValue(EATING, 1);
            }
        }
//        if(itemstack.getItem() instanceof ItemCocoaBlock){
//
//        } else if () {
//
//        }
//        else{
//
//        }
//        if (optional.isPresent()) {
//            if (!pLevel.isClientSide && campfireblockentity.placeFood(pPlayer, pPlayer.getAbilities().instabuild ? itemstack.copy() : itemstack, optional.get().getCookingTime())) {
//                pPlayer.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
//                return InteractionResult.SUCCESS;
//            }
//
//            return InteractionResult.CONSUME;
//        }

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
        pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
        pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        super.tick(state, worldIn, pos, rand);
        if(stateDefinition.any().getValue(EATING) == 1){
            if(chewingTimer > 0){
                chewingTimer--;
            }
            else{
                stateDefinition.any().setValue(EATING, 2);
                chewingTimer = chewingTime;
            }
        }
    }
}
