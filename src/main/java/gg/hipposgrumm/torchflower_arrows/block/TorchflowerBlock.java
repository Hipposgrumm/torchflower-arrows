package gg.hipposgrumm.torchflower_arrows.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class TorchflowerBlock extends FlowerBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape UP_AABB = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
    private static final VoxelShape DOWN_AABB = Block.box(5.0D, 6.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape WEST_AABB = Block.box(6.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D);
    private static final VoxelShape EAST_AABB = Block.box(0.0D, 5.0D, 5.0D, 10.0D, 11.0D, 11.0D);
    private static final VoxelShape NORTH_AABB = Block.box(5.0D, 5.0D, 6.0D, 10.0D, 11.0D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(5.0D, 5.0D, 0.0D, 10.0D, 11.0D, 11.0D);

    public TorchflowerBlock(Supplier<MobEffect> effectSupplier, int duration, Properties properties) {
        super(effectSupplier, duration, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.FALSE).setValue(FACING, Direction.UP));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, direction);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        VoxelShape shape = switch(state.getValue(FACING)) {
                case UP -> UP_AABB;
                case DOWN -> DOWN_AABB;
                case NORTH -> NORTH_AABB;
                case SOUTH -> SOUTH_AABB;
                case EAST -> EAST_AABB;
                case WEST -> WEST_AABB;
        };
        Vec3 offset = state.getOffset(blockGetter, pos);
        return (shape.isEmpty() ? Shapes.block() : shape).move(offset.x, offset.y, offset.z);
    }

    public BlockState rotate(BlockState p_152033_, Rotation p_152034_) {
        return p_152033_.setValue(FACING, p_152034_.rotate(p_152033_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_152030_, Mirror p_152031_) {
        return p_152030_.rotate(p_152031_.getRotation(p_152030_.getValue(FACING)));
    }

    public FluidState getFluidState(BlockState p_152045_) {
        return p_152045_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_152045_);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_57882_) {
        p_57882_.add(WATERLOGGED, FACING);
    }
}
