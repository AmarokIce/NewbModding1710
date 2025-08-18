package club.snowlyicewolf.modding1710.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.function.Supplier;

public class BeanCreativeTab extends CreativeTabs {
    private final Supplier<Item> itemIcon;

    public BeanCreativeTab(final String name, final Supplier<Item> item) {
        super(name);
        this.itemIcon = item;
    }

    @Override
    public Item getTabIconItem() {
        return this.itemIcon.get();
    }
}
