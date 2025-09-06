package club.snowlyicewolf.modding1710.common.block;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitBlocks;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockExample extends Block {
    public BlockExample() {
        super(new Material(MapColor.yellowColor));

        final String name = "example_block";

        this.setBlockName(name);
        this.setBlockTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);

        // 设定方块的硬度。
        this.setHardness(1.0F);
        // 设定方块的防爆抗性。
        this.setResistance(10.0F);
        // 设置光照登记，当前方块将会发出 15 级光照。
        this.setLightLevel(15.0F);

        InitBlocks.registerBlock(name, this);
    }

    @Override
    public String getHarvestTool(int metadata) {
        // 使用镐子采集。
        return "pickaxe";
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return Lists.newArrayList(new ItemStack(this));
    }
}
