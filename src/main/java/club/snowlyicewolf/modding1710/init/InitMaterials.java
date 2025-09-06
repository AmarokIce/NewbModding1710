package club.snowlyicewolf.modding1710.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class InitMaterials {
    public static final Item.ToolMaterial TOOL_SCRAP = EnumHelper.addToolMaterial("scrap", 2, 150, 4.0F, 2.0F, 18);

    public static final ItemArmor.ArmorMaterial ARMOR_SCRAP = EnumHelper.addArmorMaterial("scrap", 13, new int[]{2, 5, 4, 2}, 18);
}
