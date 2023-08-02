package gg.hipposgrumm.torchflower_arrows;

import com.mojang.logging.LogUtils;
import gg.hipposgrumm.torchflower_arrows.block.TorchflowerBlock;
import gg.hipposgrumm.torchflower_arrows.config.Config;
import gg.hipposgrumm.torchflower_arrows.entity.TorchflowerArrowEntity;
import gg.hipposgrumm.torchflower_arrows.entity.renderer.TorchflowerArrowEntityRenderer;
import gg.hipposgrumm.torchflower_arrows.items.TorchflowerArrowItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
@Mod(TorchflowerArrows.MODID)
public class TorchflowerArrows {
    public static final String MODID = "torchflower_arrows";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<Block> TORCHFLOWER_CONTROLLED = BLOCKS.register("torchflower", () -> new TorchflowerBlock(()->((FlowerBlock)Blocks.TORCHFLOWER).getSuspiciousEffect(), ((FlowerBlock)Blocks.TORCHFLOWER).getEffectDuration(), BlockBehaviour.Properties.copy(Blocks.TORCHFLOWER).offsetType(BlockBehaviour.OffsetType.NONE).lightLevel(l->14)));

    public static final RegistryObject<Item> TORCHFLOWER_ARROW = ITEMS.register("torchflower_arrow", () -> new TorchflowerArrowItem(new Item.Properties()));

    public static final RegistryObject<EntityType<TorchflowerArrowEntity>> TORCHFLOWER_ARROW_ENTITY = ENTITIES.register("torchflower_arrow", () -> EntityType.Builder.<TorchflowerArrowEntity>of(TorchflowerArrowEntity::new, MobCategory.MISC).sized(0.5f,0.5f).clientTrackingRange(4).updateInterval(20).build(new ResourceLocation(MODID, "torchflower_arrow").toString()));

    public TorchflowerArrows() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC, "torchflower_arrows.toml");

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(TORCHFLOWER_CONTROLLED.get(), RenderType.cutout());
        }

        @SubscribeEvent
        public static void entitySetup(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(TORCHFLOWER_ARROW_ENTITY.get(), TorchflowerArrowEntityRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            for (BlockState state : Blocks.TORCHFLOWER.getStateDefinition().getPossibleStates()) {
                ObfuscationReflectionHelper.setPrivateValue(BlockBehaviour.BlockStateBase.class, state, 14, "f_60594_");
            }
            for (BlockState state : Blocks.POTTED_TORCHFLOWER.getStateDefinition().getPossibleStates()) {
                ObfuscationReflectionHelper.setPrivateValue(BlockBehaviour.BlockStateBase.class, state, 14, "f_60594_");
            }
            for (BlockState state : Blocks.TORCHFLOWER_CROP.getStateDefinition().getPossibleStates()) {
                if (state.getValue(BlockStateProperties.AGE_1) == 1) ObfuscationReflectionHelper.setPrivateValue(BlockBehaviour.BlockStateBase.class, state, 7, "f_60594_");
            }
        }

        @SubscribeEvent
        public static void addCreative(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.COMBAT) {
                event.accept(TORCHFLOWER_ARROW);
            }
        }
    }
}
