package com.cpinta.pintamod.init;

import com.cpinta.pintamod.PintaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            PintaMod.MODID);

//    public static final RegistryObject<EntityType<ExampleEntity>> EXAMPLE = ENTITIES.register("example_entity",
//            () -> EntityType.Builder.<ExampleEntity>of(ExampleEntity::new, MobCategory.CREATURE).sized(1.0f, 1.0f)
//                    .build(new ResourceLocation(TutorialMod.MODID, "example_entity").toString()));
}
