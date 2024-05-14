package com.cpinta.pintamod.blocks;

import com.cpinta.pintamod.PintaMod;
import com.cpinta.pintamod.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.cpinta.pintamod.blocks.BingusBlock.*;

public class BingusBlockEntity extends BlockEntity {


    public static final IntegerProperty EATING = IntegerProperty.create("eating", 0, 3);
    public static int spewingTime = 0;
    public static int spewingAmount = 0;
    public static int spewingAmountGoal = 0;
    public static int spewPerCookie = 5;

    public static int recipeIndex = 0;

    public static int EATING_EMPTY = 0;
    public static int EATING_CHEWING = 1;
    public static int EATING_FULL = 2;
    public static int EATING_SPEWING = 3;


    public boolean digestedFully = true;
    public int digestIndex = 0;
    public static Hashtable<Item, Integer> digestedItemsComplete;
    public List<Item> digestedItemsParts;

    public BingusBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        digestedItemsComplete = new Hashtable<>();
    }

    public BingusBlockEntity(BlockPos pos, BlockState state) {
        this(BlockEntityInit.BINGUS_BLOCK.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {

        BingusBlockEntity entity = (BingusBlockEntity) be;

        if(!level.isClientSide()){
            int eatingValue = state.getValue(EATING);

            if(eatingValue == EATING_CHEWING){

                spewingTime++;
                if(spewingTime>1){
                    if(spewingAmount > 0){
                        level.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(EATING_FULL)));
                    }
                    else{
                        level.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(EATING_EMPTY)));
                    }
                }
            }
            if(eatingValue == EATING_SPEWING){
                spewingTime++;
                if(spewingTime>1){
                    if(spewingAmount > 0){
                        level.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(EATING_FULL)));
                    }
                    else{
                        level.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(EATING_EMPTY)));
                    }
                }
                else{
                    if(spewingAmount < 1){
                        level.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(EATING_EMPTY)));
                        spewingAmountGoal = 0;
                    }
                }
            }
            else if(eatingValue == EATING_FULL){
                if(spewingAmount < 1){
                    level.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(EATING_EMPTY)));
                }
            }
        }
    }

    public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity){
        BlockPos blockPos = pEntity.blockPosition().below();
        BlockPos underBlockPos = blockPos;
        Direction dir = pLevel.getBlockState(underBlockPos).getValue(FACING);
        int eatingValue = pLevel.getBlockState(underBlockPos).getValue(EATING);
        if(eatingValue == EATING_EMPTY){
            return;
        }
        else{
            if(spewingAmount < 1){
                return;
            }
            else{
                if(spewingAmount < spewingAmountGoal){
                    spewingAmountGoal = spewingAmount;
                }
            }
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

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand){
        int eatingValue = state.getValue(EATING);
        if(eatingValue == EATING_CHEWING){
            if(spewingAmountGoal > spewingAmount){
                spewingAmount += spewPerCookie;
                if(spewingAmount > spewingAmountGoal){
                    spewingAmount = spewingAmountGoal;
                }
            }
            if(spewingAmount == spewingAmountGoal){
                worldIn.setBlockAndUpdate(pos, state.setValue(EATING, Integer.valueOf(EATING_FULL)));
            }
        }
    }

    public int FeedChocolate(BlockState pState, Level pLevel, BlockPos pPos, Item item){
        int eatingValue = pState.getValue(EATING);
        if(eatingValue != EATING_SPEWING){
            spewingAmountGoal += spewPerCookie;
            digestedFully = false;
            if(!digestedItemsComplete.containsKey(item)){
                digestedItemsComplete.put(item, 1);
            }
            else{
                digestedItemsComplete.put(item, digestedItemsComplete.get(item)+1);
            }
            digestIndex = 0;
            pLevel.setBlockAndUpdate(pPos, pState.setValue(EATING, Integer.valueOf(EATING_CHEWING)));
            return 1;
        }
        return 0;
    }
}
