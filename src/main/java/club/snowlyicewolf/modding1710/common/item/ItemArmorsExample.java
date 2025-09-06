package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import club.snowlyicewolf.modding1710.init.InitMaterials;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemArmorsExample extends ItemArmor {
    public ItemArmorsExample(final int armorType) {
        super(InitMaterials.ARMOR_SCRAP, 0, armorType);
        String name = "example_armor_";
        switch(armorType) {
            case 0:
                name += "helmet";
                break;
            case 1:
                name += "chestplate";
                break;
            case 2:
                name += "leggings";
                break;
            case 3:
                name += "boots";
                break;
        }

        this.setUnlocalizedName(name);
        this.setTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);

        InitItems.registerItem(name, this);
    }

    @Override
    public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
        return slot != 2
            ? ModMain.ID + ":textures/armors/example_armor_layer_1.png"
            : ModMain.ID + ":textures/armors/example_armor_layer_2.png";
    }
}
