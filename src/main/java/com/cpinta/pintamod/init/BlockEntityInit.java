package com.cpinta.pintamod.init;

import com.cpinta.pintamod.PintaMod;
import com.cpinta.pintamod.blocks.BingusBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,
            PintaMod.MODID);

    public static final RegistryObject<BlockEntityType<BingusBlockEntity>> BINGUS_BLOCK
            = BLOCK_ENTITIES.register("bingus_block",
            () -> BlockEntityType.Builder.of(BingusBlockEntity::new, BlockInit.BINGUS_BLOCK.get()).build(null));
}
