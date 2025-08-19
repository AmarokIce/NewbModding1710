package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.Arrays;

public class ItemFoodExample extends ItemFood {
    // 总是可以食用。
    private final boolean fastEat;
    // 食用后获得的药水效果。
    private final PotionEffect[] potionEffects;

    public ItemFoodExample(int amount, float saturation, boolean isWolfFood,
                           boolean fastEat, boolean alwaysEatable, PotionEffect ... effects) {
        super(amount, saturation, isWolfFood);

        this.setMaxStackSize(1);

        this.fastEat = fastEat;
        this.potionEffects = effects;
        if (alwaysEatable) {
            this.setAlwaysEdible();
        }

        // 这里与一般的物品类是一样的。
        final String name = "example_item_food";
        this.setUnlocalizedName(name);
        this.setTextureName(ModMain.ID + ":" + name);
        this.setCreativeTab(ModMain.TAB);
        InitItems.registerItem(name, this);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack pStack) {
        return EnumAction.drink;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack pStack) {
        // 使用刻，当我们设定了快速食用，那么就缩短一倍时间 —— 就像是在啃干海带那样快。
        return this.fastEat ? 16 : 32;
    }

    @Override
    public ItemStack onEaten(ItemStack pStack, World pWorld, EntityPlayer pPlayer) {
        // 如果为空，那么直接返回父类执行。
        if (this.potionEffects.length < 1) {
            return super.onEaten(pStack, pWorld, pPlayer);
        }

        Arrays.stream(this.potionEffects)
            .map(PotionEffect::new)             // 我们需要先拷贝一份数据，因为 PotionEffect 与 ItemStack 一样，是一个数据对象。
            .forEach(pPlayer::addPotionEffect); // 然后赋予给玩家。

        return super.onEaten(pStack, pWorld, pPlayer);
    }
}
