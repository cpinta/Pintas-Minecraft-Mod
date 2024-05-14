package com.cpinta.pintamod;

import com.cpinta.pintamod.init.BlockEntityInit;
import com.cpinta.pintamod.init.BlockInit;
import com.cpinta.pintamod.init.EntityInit;
import com.cpinta.pintamod.init.ItemInit;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PintaMod.MODID)
public class PintaMod
{
    private static CreativeModeTab tab;
    // Define mod id in a common place for everything to reference
    public static final String MODID = "pintamod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> BINGUS_BLOCK = BLOCKS.register("bingusblock", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> BINGUS_BLOCK_ITEM = ITEMS.register("bingusblock", () ->
            new BlockItem(BINGUS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

    public PintaMod()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        EntityInit.ENTITIES.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
    }


    public class BingusBlock extends Block {
        public BingusBlock(Block.Properties properties) {
            super(properties);
        }
    }


    public static void setTab(CreativeModeTab tab) {
        if(PintaMod.tab != null && tab != null) {
            PintaMod.tab = tab;
        }
    }

    public static CreativeModeTab getTab() {
        return PintaMod.tab;
    }
}
