package club.snowlyicewolf.modding1710.common.block;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockExample extends Block {
    public BlockExample() {
        super(new Material(MapColor.yellowColor));

        final String name = "example_block";

        this.setBlockName(name);
        this.setBlockTextureName(ModMain.ID + ":" + name);

        InitBlocks.registerBlock(name, this);
    }
}
