package gg.hipposgrumm.torchflower_arrows.entity.renderer;

import gg.hipposgrumm.torchflower_arrows.TorchflowerArrows;
import gg.hipposgrumm.torchflower_arrows.entity.TorchflowerArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;

public class TorchflowerArrowEntityRenderer extends ArrowRenderer<TorchflowerArrowEntity> {

    public TorchflowerArrowEntityRenderer(EntityRendererProvider.Context renderContext) {
        super(renderContext);
    }

    @Override
    public ResourceLocation getTextureLocation(TorchflowerArrowEntity arrowEntity) {
        if (arrowEntity.hasPlacedTorchflower()) {
            return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
        } else {
            return new ResourceLocation(TorchflowerArrows.MODID, "textures/entity/projectile/torchflower_arrow.png");
        }
    }
}
