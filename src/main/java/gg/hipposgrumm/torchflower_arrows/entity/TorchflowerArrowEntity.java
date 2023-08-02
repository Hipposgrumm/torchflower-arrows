package gg.hipposgrumm.torchflower_arrows.entity;

import gg.hipposgrumm.torchflower_arrows.TorchflowerArrows;
import gg.hipposgrumm.torchflower_arrows.config.Config;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class TorchflowerArrowEntity extends Arrow {
    private final Level level;
    private final Entity owner;
    private boolean torchflowerPlaced = false;

    public TorchflowerArrowEntity(EntityType<? extends Arrow> type, Level level) {
        super(type, level);
        this.level = level;
        this.owner = null;
    }

    public TorchflowerArrowEntity(Level level, LivingEntity owner) {
        super(level, owner);
        this.level = level;
        this.owner = owner;
    }

    public boolean hasPlacedTorchflower() {
        return torchflowerPlaced;
    }

    @Override
    public void setSoundEvent(SoundEvent sound) {
        super.setSoundEvent(SoundEvents.GRASS_PLACE);
    }

    @Override
    public ItemStack getPickupItem() {
        return torchflowerPlaced?super.getPickupItem():new ItemStack(TorchflowerArrows.TORCHFLOWER_ARROW.get());
    }

    @Override
    public void tick() {
        if (!Config.allowMultishotTorchflowers() && this.shotFromCrossbow() && this.pickup == Pickup.CREATIVE_ONLY && !(owner instanceof Player playerOwner && playerOwner.getAbilities().instabuild) && (owner instanceof LivingEntity livingOwner && livingOwner.getMainHandItem().getEnchantmentLevel(Enchantments.MULTISHOT)>0)) torchflowerPlaced = true;
        super.tick();
        if (this.inGround && !torchflowerPlaced) doPlaceTorchflower();
    }

    private Direction calculateDirection(double posX, double posY, double posZ, boolean xNegative, boolean yNegative, boolean zNegative) {
        Direction estimatedDirection;
        boolean roundedPosX = posX >= 0.5;
        double estimastedPosX = Math.abs(posX-0.5);
        boolean roundedPosZ = posZ >= 0.5;
        double estimastedPosZ = Math.abs(posZ-0.5);
        if (!roundedPosX && !roundedPosZ) {
            estimatedDirection = (estimastedPosX<estimastedPosZ?Direction.SOUTH:Direction.EAST);
        } else if (!roundedPosX && roundedPosZ) {
            estimatedDirection = (estimastedPosX>estimastedPosZ?Direction.EAST:Direction.NORTH);
        } else if (roundedPosX && roundedPosZ) {
            estimatedDirection = (estimastedPosX>estimastedPosZ?Direction.WEST:Direction.NORTH);
        } else if (roundedPosX && !roundedPosZ) {
            estimatedDirection = (estimastedPosX<estimastedPosZ?Direction.SOUTH:Direction.WEST);
        } else {
            estimatedDirection = Direction.UP;
        }

        if (estimastedPosX<4.5 && estimastedPosZ<4.5) {
            if (posY>0.8) {
                estimatedDirection = Direction.DOWN;
            } else if (posY<0.2) {
                estimatedDirection = Direction.UP;
            }
        }

        /*
        Not sure why this happens, but if the coordinates are negative, the torchflower will place itself in the
        opposite facing direction, and it drove me insane until I figured out what was happening.
        */
        estimatedDirection = switch (estimatedDirection) {
            case WEST, EAST -> xNegative?estimatedDirection.getOpposite():estimatedDirection;
            case UP, DOWN -> yNegative?estimatedDirection.getOpposite():estimatedDirection;
            case NORTH, SOUTH -> zNegative?estimatedDirection.getOpposite():estimatedDirection;
        };
        return estimatedDirection;
    }

    private void doPlaceTorchflower() {
        //Arrow arrowReplacement = new Arrow(level, this.getX(), this.getY(), this.getZ());
        Direction facingDirection = calculateDirection(Math.abs(this.getX()%1), Math.abs(this.getY()%1), Math.abs(this.getZ()%1), this.getX()<0, this.getY()<0, this.getZ()<0);
        boolean doReplaceVanillaTorchflower = level.getBlockState(this.getOnPos()).is(Blocks.TORCHFLOWER);
        boolean waterlogged = level.getBlockState(this.getOnPos()).is(Blocks.WATER) || level.getBlockState(this.getOnPos()).getFluidState().is(Fluids.WATER);
        if ((level.getBlockState(this.getOnPos()).canBeReplaced() || doReplaceVanillaTorchflower) && TorchflowerArrows.TORCHFLOWER_CONTROLLED.get().defaultBlockState().setValue(BlockStateProperties.FACING, facingDirection).canSurvive(level, this.blockPosition())) {
            if (level.setBlock(this.blockPosition(), TorchflowerArrows.TORCHFLOWER_CONTROLLED.get().defaultBlockState().setValue(BlockStateProperties.FACING, facingDirection).setValue(BlockStateProperties.WATERLOGGED, waterlogged), 3) && !doReplaceVanillaTorchflower) {
                torchflowerPlaced = true;

                /*arrowReplacement.copyPosition(this);
                arrowReplacement.setDeltaMovement(this.getDeltaMovement());
                arrowReplacement.shakeTime = this.shakeTime;
                arrowReplacement.pickup = this.pickup;
                //arrowReplacement.setBaseDamage(this.getBaseDamage());
                //arrowReplacement.setCritArrow(this.isCritArrow());
                //arrowReplacement.setPierceLevel(this.getPierceLevel());
                //arrowReplacement.onHitBlock(result);
                arrowReplacement.setOwner(owner);
                this.remove(RemovalReason.DISCARDED);
                level.addFreshEntity(arrowReplacement);*/
            }
        }
    }
}
