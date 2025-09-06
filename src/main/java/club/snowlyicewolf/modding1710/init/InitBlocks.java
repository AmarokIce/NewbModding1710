package club.snowlyicewolf.modding1710.init;

import club.snowlyicewolf.modding1710.common.block.BlockExample;
import club.snowlyicewolf.modding1710.common.block.BlockExampleWithTile;
import club.snowlyicewolf.modding1710.common.tile.TileExample;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class InitBlocks {
    // 这里是我们的注册表。
    private static final Map<Block, String> BLOCKS = Maps.newLinkedHashMap();

    public static final Block EXAMPLE_BLOCK = new BlockExample();
    public static final Block EXAMPLE_TILE_BLOCK = new BlockExampleWithTile();

    // 在这里注册我们的方块。
    public static void registerBlock(String name, Block item) {
        if (BLOCKS.containsValue(name)) {
            throw new RuntimeException(String.format("The item %s is register twice!", name));
        }
        BLOCKS.put(item, name);
    }

    // 获取方块列表。
    public static Set<Block> getModBlocks() {
        // 数据不应该被外部修改，并且没必要以相同方式供应。
        return ImmutableSet.copyOf(BLOCKS.keySet());
    }

    // 注册全部方块。
    public static void init() {
        BLOCKS.forEach(GameRegistry::registerBlock);
        initTileEntity();
    }

    private static void initTileEntity() {
        GameRegistry.registerTileEntity(TileExample.class, "example_tile");
    }
}
