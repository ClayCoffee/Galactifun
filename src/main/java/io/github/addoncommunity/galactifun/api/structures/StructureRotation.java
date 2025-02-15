package io.github.addoncommunity.galactifun.api.structures;

import javax.annotation.Nonnull;

import org.bukkit.block.BlockFace;

/**
 * Directions that a structure can be rotated in
 *
 * @author Mooy1
 */
public enum StructureRotation {

    DEFAULT, CLOCKWISE, OPPOSITE, COUNTER_CLOCKWISE;

    private static final StructureRotation[] ROTATIONS = values();
    private static final BlockFace[] FACES = {
            BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
            BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH
    };

    @Nonnull
    public StructureRotation rotationTo(@Nonnull StructureRotation rotation) {
        return ROTATIONS[Math.abs(rotation.ordinal() - this.ordinal())];
    }

    @Nonnull
    public BlockFace rotateFace(BlockFace face) {
        return switch (face) {
            case NORTH -> FACES[this.ordinal()];
            case EAST -> FACES[this.ordinal() + 1];
            case SOUTH -> FACES[this.ordinal() + 2];
            case WEST -> FACES[this.ordinal() + 3];
            default -> face;
        };
    }

    @Nonnull
    public static StructureRotation fromFace(BlockFace face) {
        return switch (face) {
            case NORTH -> DEFAULT;
            case EAST -> CLOCKWISE;
            case SOUTH -> OPPOSITE;
            case WEST -> COUNTER_CLOCKWISE;
            default -> throw new IllegalArgumentException("方块朝向 " + face + " 不能转换成结构朝向!");
        };
    }

}
