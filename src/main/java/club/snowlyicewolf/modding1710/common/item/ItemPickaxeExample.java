package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPickaxe;

public class ItemPickaxeExample extends ItemPickaxe {
    public ItemPickaxeExample() {
        super(ToolMaterial.IRON);

        this.setMaxDamage(114514);

        final String name = "example_pickaxe";
        this.setUnlocalizedName(name);
        this.setTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);
        InitItems.registerItem(name, this);
    }

    @Override
    public boolean func_150897_b(Block pBlock) {
        return true;
    }
}
