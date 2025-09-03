# 工具与防具材质

在前两个章节中我们学习了如何创建一个工具、武器以及一套盔甲，但是我们都接触到了一个概念：材质。这个章节我们将要学习如何创建一个我们自己的材质。

[**章节相关源码**](https://github.com/AmarokIce/NewbModding1710/blob/ModDev/src/main/java/club/snowlyicewolf/modding1710/init/InitMaterials.java)

## 思考：如何注册材质

我们在工具与武器章节中已经观察过 `ToolMaterial` 了，相信读者在编写盔甲时也应该已经观察过 `ArmorMaterial` 了。  
细心的读者应该会发现，这些材质类都采用了枚举 `enum` 的方式创建。如果读者对 Java 有一定了解，那么应该知道 `enum` 是一个外部不可拓展的的特殊类型。  
那我们应该如何注册它？答案是 `反射`。

这是一种肮脏的概念，不过很幸运，Forge 已经为我们提供了工具，因此读者不会被复杂的底层概念与学习反射必备的知识吓跑。如果读者已经准备好起身逃跑——别忙，回来，坐下来。

注入或修改 `enum` 通常不是一个好主意，这可能会导致较多问题。但 1.7.10 开发本身就是一件相对肮脏的体验，因此希望读者没有字节码洁癖。好吧，我们有些跑题了，总之先来看看这个特殊的类：`EnumHelper`。

```java title="EnumHelper.class"
private static Class[][] commonTypes =
{
    {EnumAction.class},
    {ArmorMaterial.class, int.class, int[].class, int.class},
    {EnumArt.class, String.class, int.class, int.class, int.class, int.class},
    {EnumCreatureAttribute.class},
    {EnumCreatureType.class, Class.class, int.class, Material.class, boolean.class, boolean.class},
    {Door.class},
    {EnumEnchantmentType.class},
    {EnumEntitySize.class},
    {Sensitivity.class},
    {MovingObjectType.class},
    {EnumSkyBlock.class, int.class},
    {EnumStatus.class},
    {ToolMaterial.class, int.class, int.class, float.class, float.class, int.class},
    {EnumRarity.class, EnumChatFormatting.class, String.class}
};
```

在 `commonTypes` 中的全部列出的枚举类都是我们将可以增加注册的部分。可以看到，我们可以增加注册 `ArmorMaterial` 与 `ToolMaterial`。  

## 注册一个材质

我们一开始创建了一个物品，但是还没有任何用处。或许读者有很多想法，但我得坦白：我其实没想过那个物品要做什么。那么我就称它为废料吧！（当然，我也不知道为什么）  
既然我们得到了废料，干脆就为我们的工具注册一个废料材质吧：

```java title="IniteMetarial.java"
public class InitMaterials {
    public static final Item.ToolMaterial TOOL_SCRAP = EnumHelper.addToolMaterial("scrap", 2, 150, 4.0F, 2.0F, 18);

    public static final ItemArmor.ArmorMaterial ARMOR_SCRAP = EnumHelper.addArmorMaterial("scrap", 13, new int[]{ 2, 5, 4, 2 }, 18);
}
```

这样，我们就算是完成注册了一套材质，现在我们可以用于我们刚注册过的工具与盔甲了！
