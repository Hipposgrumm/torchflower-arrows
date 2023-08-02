package gg.hipposgrumm.torchflower_arrows.items;

import gg.hipposgrumm.torchflower_arrows.entity.TorchflowerArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TorchflowerArrowItem extends ArrowItem {
    public TorchflowerArrowItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack item, LivingEntity owner) {
        return new TorchflowerArrowEntity(level, owner);
    }
}
