package com.cpinta.pintamod.init;

import com.cpinta.pintamod.PintaMod;
import com.cpinta.pintamod.util.tiers.ModArmorMaterials;
import com.cpinta.pintamod.util.tiers.ModTiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            PintaMod.MODID);

    public static final RegistryObject<BlockItem> BINGUS_BLOCK_ITEM = ITEMS.register("bingusblock",
            () -> new BlockItem(BlockInit.BINGUS_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

//    public static final RegistryObject<SwordItem> EXAMPLE_SWORD = ITEMS.register("example_sword",
//            () -> new SwordItem(ModTiers.EXAMPLE, 5, 0.5f, new Item.Properties()));
//
//    public static final RegistryObject<PickaxeItem> EXAMPLE_PICKAXE = ITEMS.register("example_pickaxe",
//            () -> new PickaxeItem(ModTiers.EXAMPLE, 5, 0.5f, new Item.Properties()));
//
//    public static final RegistryObject<ShovelItem> EXAMPLE_SHOVEL = ITEMS.register("example_shovel",
//            () -> new ShovelItem(ModTiers.EXAMPLE, 5, 0.5f, new Item.Properties()));
//
//    public static final RegistryObject<AxeItem> EXAMPLE_AXE = ITEMS.register("example_axe",
//            () -> new AxeItem(ModTiers.EXAMPLE, 5, 0.5f, new Item.Properties()));
//
//    public static final RegistryObject<HoeItem> EXAMPLE_HOE = ITEMS.register("example_hoe",
//            () -> new HoeItem(ModTiers.EXAMPLE, 5, 0.5f, new Item.Properties()));
//
//    public static final RegistryObject<ArmorItem> EXAMPLE_HELMET = ITEMS.register("example_helmet",
//            () -> new ArmorItem(ModArmorMaterials.EXAMPLE, EquipmentSlot.HEAD, new Item.Properties()));
//
//    public static final RegistryObject<ArmorItem> EXAMPLE_CHESTPLATE = ITEMS.register("example_chestplate",
//            () -> new ArmorItem(ModArmorMaterials.EXAMPLE, EquipmentSlot.CHEST, new Item.Properties()));
//
//    public static final RegistryObject<ArmorItem> EXAMPLE_LEGGINGS = ITEMS.register("example_leggings",
//            () -> new ArmorItem(ModArmorMaterials.EXAMPLE, EquipmentSlot.LEGS, new Item.Properties()));
//
//    public static final RegistryObject<ArmorItem> EXAMPLE_BOOTS = ITEMS.register("example_boots",
//            () -> new ArmorItem(ModArmorMaterials.EXAMPLE, EquipmentSlot.FEET, new Item.Properties()));
//
//    public static final RegistryObject<Item> EXAMPLE_FOOD = ITEMS.register("example_food", () -> new Item(
//            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1.4f).build())));

//    public static final RegistryObject<ForgeSpawnEggItem> EXAMPLE_SPAWN_EGG = ITEMS.register("example_spawn_egg",
//            () -> new ForgeSpawnEggItem(EntityInit.EXAMPLE, 0xF0ABD1, 0xAE4C82, new Item.Properties()));
}
