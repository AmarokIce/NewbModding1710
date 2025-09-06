package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import club.snowlyicewolf.modding1710.init.InitMaterials;
import net.minecraft.block.Block;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemPickaxeExample extends ItemPickaxe {
    public ItemPickaxeExample() {
        super(InitMaterials.TOOL_SCRAP);

        // 设置耐久。
        this.setMaxDamage(114514);

        final String name = "example_pickaxe";
        this.setUnlocalizedName(name);
        this.setTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);
        InitItems.registerItem(name, this);
    }

    // 是否可以高效率的挖掘当前方块，返回 true 时为可以。
    @Override
    public boolean func_150897_b(final Block pBlock) {
        return true;
    }

    // 设置挖掘速度。
    @Override
    public float func_150893_a(final ItemStack p_150893_1_, final Block p_150893_2_) {
        return 10.0f;
    }
}
