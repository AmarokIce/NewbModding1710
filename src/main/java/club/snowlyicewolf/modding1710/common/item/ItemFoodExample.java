package club.snowlyicewolf.modding1710.common.item;

import club.snowlyicewolf.modding1710.ModMain;
import club.snowlyicewolf.modding1710.init.InitItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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

    public ItemFoodExample(final int amount, final float saturation, final boolean isWolfFood,
                           final boolean fastEat, final boolean alwaysEatable, final PotionEffect ... effects) {
        super(amount, saturation, isWolfFood);

        // 设置最大堆叠数量。
        this.setMaxStackSize(16);

        // 设置快速食用。
        this.fastEat = fastEat;
        // 附加我们的药水效果。
        this.potionEffects = effects;
        // 定义是否为总是食用。
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

    // 设置我们的食用方式，是食用还是饮用。
    @Override
    public EnumAction getItemUseAction(final ItemStack pStack) {
        return EnumAction.eat;
    }

    // 设置食用（使用）的速度。
    @Override
    public int getMaxItemUseDuration(final ItemStack pStack) {
        // 使用刻，当我们设定了快速食用，那么就缩短一倍时间 —— 就像是在啃干海带那样快。
        return this.fastEat ? 16 : 32;
    }

    // 食用（使用）后。
    @Override
    public ItemStack onEaten(final ItemStack pStack, final World pWorld, final EntityPlayer pPlayer) {
        // 确定返回物品
        final ItemStack itemCraftingBack = new ItemStack(Items.bowl);

        // 当无法把物品塞到玩家的物品栏的时候，我们应该让它以掉落物形式生成到世界
        if (!pPlayer.inventory.addItemStackToInventory(itemCraftingBack)) {
            // 创建一个掉落物，位置在玩家所在位置，此处 y 增加 0.5 是为了让掉落物处于玩家所在方块位置（BlockPos）的中心位置。
            final EntityItem entityItem = new EntityItem(pWorld,
                pPlayer.posX, pPlayer.posY + 0.5, pPlayer.posZ,
                itemCraftingBack);
            // 然后生成到世界。
            pWorld.spawnEntityInWorld(entityItem);
        }

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
