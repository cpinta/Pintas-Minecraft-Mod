package com.cpinta.pintamod.init;

import com.cpinta.pintamod.PintaMod;
import com.cpinta.pintamod.blocks.BingusBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            PintaMod.MODID);

    public static final RegistryObject<BingusBlock> BINGUS_BLOCK = BLOCKS.register("bingusblock", () -> new BingusBlock(
            BlockBehaviour.Properties.of(Material.STONE)));

    private static <T extends Block> BlockWithItemHolder<T> registerBlock(String name, Supplier<T> block, Item.Properties properties) {
        RegistryObject<T> regBlock = BLOCKS.register(name, block);
        RegistryObject<BlockItem> regBlockItem = ItemInit.ITEMS.register(name,
                () -> new BlockItem(regBlock.get(), properties));
        return new BlockWithItemHolder<>(regBlock, regBlockItem);
    }

    private static <T extends Block> BlockWithItemHolder<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, new Item.Properties());
    }

    public record BlockWithItemHolder<T extends Block>(RegistryObject<T> block, RegistryObject<BlockItem> blockItem) {
    }
}
