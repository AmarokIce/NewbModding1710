# 食物

在 1.7.10 中, 食物有它专属的类, 我们只需要继承这个类继续编写即可, 当然也可以仿造这个类再制作我们自己的实现方式——大部分情况下是不必要的，并且统一使用类继承可以很好的为对象分类。

这一章节中，我们将会创建一个可以食用或饮用的物品。

[**章节相关源码**](https://github.com/AmarokIce/NewbModding1710/blob/ModDev/src/main/java/club/snowlyicewolf/modding1710/common/item/ItemFoodExample.java)

## 创建一个食物

首先创建新的类 `ItemFoodExample`, 继承 `ItemFood`。此时，IDE 应该会警告读者这有一个错误：因为 `ItemFood` 是一个带构造函数的类。现在，我们开始观察 `ItemFood` 的构造函数：

```java title="ItemFood.class"
public ItemFood(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_)
{
    this.itemUseDuration = 32;
    this.healAmount = p_i45339_1_;
    this.isWolfsFavoriteMeat = p_i45339_3_;
    this.saturationModifier = p_i45339_2_;
    this.setCreativeTab(CreativeTabs.tabFood);
}
```
哦不！我们遇到了 `Srg 名`！不过没关系，更具参数，我们可以尝试翻译一下这些 `Srg 名` 都意味着什么：

```java
public ItemFood(int amount, float saturation, boolean isWolfFood)
{
    this.itemUseDuration = 32;                  // 使用时刻，默认 32 tick
    this.healAmount = amount;                   // 饥饿值，每 2 点为一个鸡腿，上限 20
    this.isWolfsFavoriteMeat = isWolfFood;      // 狼的食物，如果这个为 true，我们可以喂狼
    this.saturationModifier = saturation;       // 饱和度，每 0.2f 点为一个鸡腿，上限 2.0f
    this.setCreativeTab(CreativeTabs.tabFood);  // 物品加入创造模式物品栏，别担心，这个是可以覆盖的
}
```

很好，现在我们已经基本清楚 `ItemFood` 的细节了，我们可以回到我们的类并 `super` 链接父类了:

```java title="ItemFoodExample"
public class ItemFoodExample extends ItemFood {
    public ItemFoodExample(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        final String name = "example_item_food";
        // 设置本地化键。
        this.setUnlocalizedName(name);
        // 设置贴图资源。
        this.setTextureName(ModMain.ID + ":" + name);
        // 加入创造模式物品栏
        this.setCreativeTab(ModMain.TAB);
        // 注册物品。
        InitItems.registerItem(name, this);
    }
}
```

看起来就像是创建一个普通物品？没错，就是这么简单！不过单纯的食物肯定无法满足我们不是吗？我们还需要更多的拓展，利用 `ItemFood` 的一些特殊方法，我们可以稍稍改造一下类：

```java title="ItemFoodExample.java"
public class ItemFoodExample extends ItemFood {
    // 总是可以食用。
    private final boolean fastEat;
    // 食用后获得的药水效果。
    private final PotionEffect[] potionEffects;

    public ItemFoodExample(int amount, float saturation, boolean isWolfFood,
                           boolean fastEat, boolean alwaysEatable, PotionEffect ... effects) {
        super(amount, saturation, isWolfFood);

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
    public int getMaxItemUseDuration(ItemStack pItemStack) {
        // 使用刻，当我们设定了快速食用，那么就缩短一倍时间 —— 就像是在啃干海带那样快。
        return this.fastEat ? 16 : 32;
    }

    @Override
    public ItemStack onEaten(ItemStack pItemStack, World pWorld, EntityPlayer pPlayer) {
        // 如果为空，那么直接返回父类执行。
        if (this.potionEffects.length < 1) {
            return super.onEaten(pItemStack, pWorld, pPlayer);
        }

        Arrays.stream(this.potionEffects)
            .map(PotionEffect::new)             // 我们需要先拷贝一份数据，因为 PotionEffect 与 ItemStack 一样，是一个数据对象。
            .forEach(pPlayer::addPotionEffect); // 然后赋予给玩家。

        return super.onEaten(pItemStack, pWorld, pPlayer);
    }
}
```

现在，我们得到了一个看起来相对复杂的食物类，但我们能做的事情更多了！比如允许玩家像是金苹果那样总是食用，并且稍后为玩家附加效果... 快去注册一个物品试一试吧！

## 拓展阅读

### 食用或者饮用

如果我们想要做一些药水或者类似的饮品，我们应该修改使用方式。  
但是我们应该从哪开始？仔细想一想，既然弓可以拉，药水可以喝，食物可以吃，那么必然有一种控制器决定了玩家的行为。我们可以从两个位置考虑：

1. 它们在 `Item` 中。
2. 它们在 `EntityPlayer` 中。

无论如何，让我们先来看一下 `ItemFood` 中是否有覆写我们需要的内容。

```java title="ItemFood.class"
/**
 * returns the action that specifies what animation to play when the items is being used
 */
public EnumAction getItemUseAction(ItemStack p_77661_1_)
{
    return EnumAction.eat;
}
```

一段搭配了 Forge 文档的方法，方法名写着“获取物品使用动作”，那么这就是我们需要的了！很显然，如果我们要控制食物是食用或是饮用, 我们需要复写方法 `Item#getItemUseAction`。  
`Item#getItemUseAction` 方法的参数是 `ItemStack`, 对应的就是这个物品的 `ItemStack`. 如果读者对 `ItemStack` 还毫无概念, 请重读 [Item 与 ItemStack](./ItemStack.md) 章节。  
而这个方法的返回值 `EnumAction` 是玩家动作的集合, 先让我们看一下这个类里都是一些什么：

```java title="EnumAction.class"
public enum EnumAction
{
    none,   // 无动作，Item 中默认。
    eat,    // 食用，ItemFood 中默认。
    drink,  // 啜饮
    block,  // 放置
    bow;    // 拉弓
}
```

在食物中我们通常会用到 `eat` 和 `drink`, 而默认则是 `eat`。如果我们想制作一个饮品，那么此处改为 `drink` 即可。

```java title="ItemFoodExample.java"
@Override
public EnumAction getItemUseAction(ItemStack pStack) {
    return EnumAction.drink;
}
```

### 数量限制

在 Minecraft 中，能饮用的通常都是药水，并且只能堆叠一个。我们也可以设计一个只能堆叠一个的物品，只需要在构造方法中加上一句即可：

```java
this.setMaxStackSize(1);
```
