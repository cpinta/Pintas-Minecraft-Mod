package com.cpinta.pintamod.util.tags;

import com.cpinta.pintamod.PintaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> EXAMPLE_TOOL = BlockTags.create(
            new ResourceLocation(PintaMod.MODID, "needs_example_tool"));
}
