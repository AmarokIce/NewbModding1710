package club.snowlyicewolf.modding1710.init;

import club.snowlyicewolf.modding1710.common.item.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public final class InitItems {
    public static final Item ITEM_EXAMPLE = new ItemExample();
    public static final Item ITEM_EXAMPLE_WITH_SUBITEM = new ItemExampleWithSubItem();
    public static final Item ITEM_EXAMPLE_FOOD = new ItemFoodExample(
        1, 0.1f, false, false, true,
        new PotionEffect(Potion.nightVision.getId(), 10, 0)
    );
    public static final Item ITEM_EXAMPLE_PICKAXE = new ItemPickaxeExample();
    public static final Item ITEM_EXAMPLE_SWORD = new ItemSwordExample();
    public static final Item ITEM_EXAMPLE_TOOL = new ItemToolExample();
    public static final Item ITEM_EXAMPLE_ARMOR_HELMET = new ItemArmorsExample(0);
    public static final Item ITEM_EXAMPLE_ARMOR_CHEST = new ItemArmorsExample(1);
    public static final Item ITEM_EXAMPLE_ARMOR_LEGGING = new ItemArmorsExample(2);
    public static final Item ITEM_EXAMPLE_ARMOR_BOOTS = new ItemArmorsExample(3);
    // 这里是我们的注册表。
    private static final Map<Item, String> ITEMS = Maps.newLinkedHashMap();

    private InitItems() {
    }

    // 在这里注册我们的物品。
    public static void registerItem(String name, Item item) {
        if (ITEMS.containsValue(name)) {
            throw new RuntimeException(String.format("The item %s is register twice!", name));
        }
        ITEMS.put(item, name);
    }

    // 获取物品列表。
    public static Set<Item> getModItems() {
        // 数据不应该被外部修改，并且没必要以相同方式供应。
        return ImmutableSet.copyOf(ITEMS.keySet());
    }

    // 注册全部物品。
    public static void init() {
        ITEMS.forEach(GameRegistry::registerItem);
    }
}
