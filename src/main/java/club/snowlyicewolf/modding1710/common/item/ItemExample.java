package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import net.minecraft.item.Item;

public class ItemExample extends Item {
    public ItemExample() {
        final String name = "example_item";
        // 设置本地化键。
        this.setUnlocalizedName(name);
        // 设置贴图资源。
        this.setTextureName(ModMain.ID + ":" + name);
        // 加入创造模式物品栏。
        this.setCreativeTab(ModMain.TAB);
        // 注册物品。
        InitItems.registerItem(name, this);
    }
}

