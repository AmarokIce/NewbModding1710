# 食物

在 1.7.10 中, 食物有它专属的类, 我们只需要继承这个类继续编写即可, 当然也可以仿造这个类再制作我们自己的实现方式——大部分情况下是不必要的, 同时, 如果你尝试使用自己的实现, 那么可能会在与别的模组的兼容上出现一些 "小问题". 诸如一些获取食物的模组会忽略你自己实现的食物。

首先创建新的类 ItemExampleFood, 继承 ItemFood. 然后我们观察 ItemFood 的构造函数:

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

构造函数上的三个变量分别对应饥饿水平, 饱和水平和肉类.

> 饥饿水平:玩家的饥饿值, 整数.上限为20.<br />
> 饱和水平:玩家的饱和度, 浮点.上限为2.0F.<br />
> 肉类:狼可食用的肉食物, 布尔值.当为True时与其他肉食相同, 可以喂狼.

当然, 我们还可以对它进行一些构造, 现在回到ItemExampleFood类上, 开始编写我们的构造函数:

```java title="ItemExampleFood.java"
public ItemExampleFood(int hunger, float saturation, boolean isMeat, boolean canAlwaysEatable) {
    super(hunger, saturation, isMeat); // (1)
    if (canAlwaysEatable) this.setAlwaysEdible();
    this.maxStackSize = 1; // (2)
}
```

1. super(); 是被称为"超类"的特殊方法, 必须置于构造函数的顶部.当继承的类有必要的构造函数时, 子类必须使用 super 链接回父类的构造函数的成员参数.
2. `maxStackSize` 变量是Item类中的控制物品最大堆叠的变量, 上限为64, 下限为1, 超出部分无效.此处设为1则代表这个食物与水瓶一样只能堆叠一个.

## 属性拓展

**食用或者饮用**

要控制食物是食用或是饮用, 我们需要复写方法 `getItemUseAction`.

`getItemUseAction` 的参数是 ItemStack, 对应的就是这个物品的 ItemStack. 如果你对 ItemStack 还毫无概念, 请重读 `Item 与 ItemStack` 章节.

EnumAction 是玩家动作的集合, 在食物中我们只会用到 eat 和 drink, 而默认则是 eat.

```java title="ItemExampleFood.java"
@Override
public EnumAction getItemUseAction(ItemStack item) {
    return EnumAction.eat;
}
```

**快速食用**

如果你尝试过在 1.13+ 的版本开发, 你对 FoodProperties 的 fast 参形可能并不陌生. 但是 1.7.10 中没有这个参形, 我们需要自己修改.

```java title="ItemExampleFood.java"
@Override
public int getMaxItemUseDuration(ItemStack item){
    return 32;
}
```

在更高的那些版本里看起来应该是这样的:

```java
public int getMaxItemUseDuration(ItemStack item){
    return this.fast ? 16 : 32;
}
```