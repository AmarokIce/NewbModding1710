# 重要概念

本篇将会对一些开发前你必须要了解的概念, 这些概念可能乍一看无法理解, 而随着你的深入, 你会逐渐理解这些概念.但是如果你一开始就完全没开始了解这些概念, 你可能会逐渐难以理解.

## Mod Entry Point（模组入口）

Forge Mod一直有一个默认且长存的注解 —— `@Mod`。
<br />
在1.7.10中, `@Mod` 注解是至关重要的, 它可以配置你的模组的加载顺序与联动关系, 以及更多。
<br />
如果我们没有任何需求, 那么只需要保证参数 modid 不为空就好.在你的初始类的顶部加入此主机让 Forge 知道要从哪开始。

```java
@Mod(
    // 这里是我们的 ModID。
    modid = ModMain.ID,
    name = ModMain.NAME,
    version = ModMain.VERSION,
    // 如果为 true 则将会要求 Forge 读取 mcmod.info 覆盖大部分设定。
    useMetadata = false, /*(5)*/
    // 默认就是 Java，当然你可以选择 Scala，如果使用 Kotlin,需要安装第三方模组。
    modLanguage = "java", /*(4)*/
    // 如果你修改了语言，需要提供一个适配器。适配器用于解析主要类中的魔法，例如 @Proxy 与 @Instance。
    modLanguageAdapter = "",
    // 远端版本，远端是指客户端。如果远端版本为任意，意味着这是一个客户端可选模组。
    acceptableRemoteVersions = "*",
    // 模组关系，例如：{@code required-after:mymod;} 意思为依赖 mymod 且处于之后加载。如果去掉 required-，则是仅滞后。
    dependencies = ""
)
public class ModMain {
    public static final String ID = "examplemod"; // (1)
    public static final String NAME = "Example Mod";
    public static final String VERSION = "0.0.1"; // (2)
    public static final Logger LOGGER = LogManager.getLogger(NAME); // (3)
}
```

1. 这里是我们的 Modid! 1.7.10 中没有强求与 mcmod.info 中的 modid 相同, 并且除非你同时在 `@Mod` 中使用 `useMetainfo = true` 标记, 否则 mod.info 是装饰性文件.
   <br />
   对应 `@Mod` 参数的modid.
2. 这里是版本! 版本会影响远端(服务器与客户端通讯)的兼容范围, 但是这不是最重要的, 即便不写也是没有问题的.
   <br />
   对应 `@Mod` 参数的version.
3. 这里是Log4J的日志记录器, 在这里推送你需要向控制台推送的东西! 在可能会出现问题的地方或是其他你喜欢的地方推送日志, 可以辨别模组在运行哪部分时出现过问题.
4. modLanguage 即便不写, 默认也是 java. 此部分主要为 Scala 用户准备, 而 AJSCore 提供了 Kotlin 的 modLanguage 兼容, 而 LKM 中不填也不影响.
5. 如果这边启用了 useMeta, 那么 mcmod.info 中的内容请认真填写.

模组启动后，Forge 会自动寻找入口，然后使用适配器进行魔法处理。我们使用 `@Mod.EventHandler` 注解标记生命周期实践，并且最常用的就是三种初始化事件：

<br />**FMLPreInitializationEvent**：预备初始化加载，通常我们在这完成大部分注册，包括配置文件。
<br />**FMLInitializationEvent**：初始化加载，如果你没在预备初始化注册，那么至少需要在这里完成注册。通常处理一些标准流程和注册过之后要做的事情。
<br />**FMLPostInitializationEvent**：通讯初始化，通常用于与其他模组交流，也可以注册一些事件或封包。请不要再注册物品之类的东西了，除非你是个 🦀。

另外是一些服务端生命周期事件，没有上面三种那么常用，但你依然可能会用到：

<br />**FMLServerAboutToStartEvent**： 服务端预启动事件，如果你想赶在服务端启动前做点什么，趁现在去吧。
<br />**FMLServerStartingEvent**： 服务端启动中事件，我们在这里**注册指令，以及修改等**。
<br />**FMLServerStartedEvent**： 服务端启动事件，此时服务端已经完成启动，几乎全部模组都已经完成他们的注册流程。
<br />**FMLServerStoppingEvent**： 服务端关闭中事件，在服务端关闭之前做点什么。.
<br />**FMLServerStoppedEvent**： 服务端关闭事件，通常来讲只有专用服务端才会发送这个一次性事件。

以及两种特殊事件：

<br />**FMLFingerprintViolationEvent**： 签名违规事件，如果你的模组签名出现了问题（依赖问题等），这个事件会被推送。
<br />**FMLInterModComms.IMCEvent**： IMC 交换事件，全称 `Inter Mod Communication`，另一种与其他模组交换数据的方式。可以交换字符串，ItemStack 与 NBT。可以参考 [https://minecraftforgetuts.weebly.com/inter-mod-communication.html](https://minecraftforgetuts.weebly.com/inter-mod-communication.html)。

## 资源路径

`ResourceLocation` 是贯穿整个 Minecraft 的资源分配的类, 它的构造由两部分组合—— `Modid` 与 `Path`.通常, 一个标准的 "注册名" 是由 `Modid` 与物品名称组合的 `ResourceLocation`, 看起来就像这样: `minecraft:apple` 。
<br />
而对于贴图名称, 我们也应该提交完整的 `ResourceLocation` 格式的字符串, 这种物品的贴图名 `minecraft:apple` 会被自动解析为这样: `minecraft:assets/minecraft/items/apple`.那在填写的时候需要填写这么完整吗?答案是不需要, 在进阶的使用之前, 只需要记住前者的格式就好。

ResourceLocation 的申明方式有三种, 其中第一种是只填写名称, 此时 `Modid` 会变为 `minecraft`。看起来就像这样:

```java
new ResourceLocation("apple"); // (1)
```

1. 实际的 `ResourceLocation` 就是 `minecraft:apple`

另外两种就没有什么特别的区别, 只是习惯问题.

```java
new ResourceLocation("modid", "apple");
new ResourceLocation("modid:apple");
// (1)
```

1. 这两种申明的ResourceLocation都是 `modid:apple` .


## 网络发包

Minecraft 的运行分为三个线程: 客户端渲染线程（Renderer Thread），逻辑线程(Logic Server Thread，也叫内置服务端线程)，以及专用服务端线程（Dedicated Server Thread）。前两者，也就是渲染线程与逻辑线程，这些是处于客户端的，而后者，专用服务端现场，则是仅限于服务器主机的。我们常说的服务端和客户端，便是这些线程上的区别。请注意，跨线程访问大多情况下是不可行的，尤其是专用服务端线程中不可以访问任何渲染线程的内容！
<br />
但很显然，逻辑线程作为服务端线程的一种（被称之为内置服务端），大多逻辑都发生在上面。在开发时，这一部分也会被至于专用服务端。那这时候想渲染个粒子什么的不就炸缸了？没错，因此我们必须使用一种能够跨域通讯的方式——通讯发包。
<br />
当有需求客户端与服务端通讯时, 就得借助网络发包的力量.比如按钮, `GUI` 等这些在客户端执行的项目被调用时, 你可能会希望它们在服务端做点什么, 那么就需要 `C2S(Client To Server)` 发包, 反之则是 `S2C(Server To Client)`。

当然, 有时候我们可能不需要发包, 只是希望再某一侧单独执行一些代码.使用 `event` 中的 `side` 或者标记 `@SideOnly` 注解很好, 但是我们有更好且更干净的选择——代理.这一块会在之后的内容讲解, 但你应该想好你的逻辑代码应该在客户端执行还是服务端执行.由于 1.7.10 中诸如生成一个掉落物等是需要两端共同执行才能按照预期提供, 因此我们可能需要更多的进行实验来解决网络问题。必要的时候请毫不犹豫的使用封包系统。

但请注意，封包具有网络开销的需求，在封装封包时，应该注意仅打包必要的内容。可以采用信号，事件等方式降低流量压力。

## 总线与事件

相信读者应该知道什么是 `Tick`。游戏中发生的任何事情都应该在 `Tick` 的更新中发生，因此服务器上较低的 `Tick Per Seconds (TPS)` 就会让世界更新速度变慢。

为了让 `Tick` 中发生的事情能够让大伙都知道，Forge 借以 `Google Guava` 中的 `EventBus` 系统设计了一套独立的事件系统。
<br />
如果你曾经经常使用 `Guava` 那么对事件系统应该不陌生。但如果你从未了解过也没关系，这是一个很简单的概念：
<br />
`EventBus`，总线，而直译则是事件班车。正如其名，这就像是一辆班车。如果班车到了一站（到达兴趣点），那么广播就会通知乘客（订阅的事件们）当前位置。此时感兴趣的乘客就会按照列车顺序与上车顺序（时间优先级与注册时刻）依次下车参观。乘客可以下车带走一些纪念品（修改值）之后返程，或者选择空手而归。

与 `Guava` 不太一样的是，Forge 的 `Event` 之中有一些可取消事件。如果靠前的事件要求取消，那么在那之后的事件都不会被通知。因此采用优先级较高且取消事件时应该注意与其他模组的兼容情况。

## 注册与注册表

注册表用于登记每个物品，方块或别的有趣的东西。参与注册就会有自己的注册名，这是一种抽象数据，在存储玩家数据的时候通常会记录玩家所持有的各种物品，将会按照注册名保存（1.7.10 确实还在用 ID，但由于 1.8 开始推广使用注册名，为 1.13 的扁平化铺垫，因此 1.7.10 开始已经有注册名数据概念）。

另一个要注意的是，注册名是不可重复的，并且必须是唯一的。不过由于 ModID 也是实际注册名的一部分，因此我们不需要担心与其他模组撞名的问题。当然，如果 ModID 撞名了...

只有需要被保存的部分才需要注册，而像是伤害、GUI 一类动态数据则不需要注册。这一点与扁平化之后的版本非常不一样——如果你找不到注册方式，不用怀疑，就是没有。

> 在 1.7.10 中，注册的物品与方块是唯一对象，任何操作都应该发生在 `ItemStack`。
>
> 顺带一提，如果方块实体（TileEntity）未经注册，那么它不会保存任何 NBT。

### 注册示例

#### 一般注册

> 最普遍的注册方式, 除了相较于其他方法而言会麻烦之外几乎没有缺点.

```java
// 物品注册:
public class ItemInit {
    public static final Item ITEM_EXAMPLE = getItem("example_item")

    public static Item getItem(final String name) {
        final Item item = new Item();
        item.setUnlocalizedName(name);
        item.setTextureName(ModMain.ID + ":" + name);
        return item;
    }

    public static void init() {
        GameRegistry.registerItem(ITEM_EXAMPLE, "example_item", ModMain.ID);
    }
}

// 模组入口:
@Mod(modid = "examplemod")
public class ModMain {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ItemInit.init();
    }
}
```

#### 静态注册表

```java
public class ItemInit {
    public static final Map<String, Item> ITEM_LIST = Maps.newHashMap(); // (1)

    public static final Item ITEM_EXAMPLE = new ItemExample("example_item")

    public static void init() {
        for (Map.Entry<String, Item> item : ITEM_LIST.entrySet()) {
            GameRegistry.registerItem(item.getValue(), item.getKey(), ModMain.ID);
        }
    }
}

public class ItemExample extends Item {
    public ItemExample(final String name) {
        this.setUnlocalizedName(name);
        this.setTextureName(ModMain.ID + ":" + name);

        ITEM_LIST.put(name, this);
    }
}

// 模组入口:
@Mod(modid = "examplemod")
public class ModMain {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ItemInit.init();
    }
}
```

1. 这里的 String 是注册名.String 与 Item 谁为键都可以, 只需要自己还能知道如何去的值就好.虽然这也与自动构建同样需要 this 推送, 但是注册时这个对象一定是完成了实例化的.因此可以避开 this 泄漏风险.

#### 构建注册

这是一种指针泄漏访问，具有一定毒性。剂量不大时，这种方式相对更舒适，但不代表这是一种好的方式。

```java
public class ItemInit {
    public static final Item ITEM_EXAMPLE = new ItemExample("example_item")

    public static void init() {
    }
}

public class ItemExample extends Item {
    public ItemExample(final String name) {
        this.setUnlocalizedName(name);
        this.setTextureName(ModMain.ID + ":" + name);

        GameRegistry.registerItem(this, name, ModMain.ID);
    }
}

// 模组入口:
@Mod(modid = "examplemod")
public class ModMain {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ItemInit.init();
    }
}

```

??? question "什么是 this 指针泄漏?"
    Jvm this 指针泄漏指在创建构造函数时加入 this, 在部分情况下可能会导致对象未完成实例但已被加入到某个环境中, 对 Jvm 保持实例访问存在影响, 可能会导致空指针。

## 数据存储，Named Binary Tags（NBT）

在 Minecraft 中，我们通常使用 NBT 存储数据。这是一种基于键值存储的数据。
<br />
通常情况下只有 `ItemStack` 与 `TileEntity` 拥有可客制化存储的 NBT。至于生物 `Entity`，Forge 为我们拓展了一个额外的可客制化 NBT。

## 特别拓展:ASM, Mixin 与 Jvm 字节码调包

!!! note "这是一份特别拓展的内容! "
    此部分为特别拓展, 会在进阶开发中详细说明。你只需要有这一回事，不需要立刻开始了解它们。若你没有相关的基础与概念, 此部分你可能无法理解其用途, 甚至会从中犯下严重的错误。

> 当你对 Minecraft 1.7.10 的整体结构有一定了解后, 你可以对 Minecraft 进行注入。Forge 提供了 ASM 入口使得模组开发者们可以简单的加入字节码注入。 当然, 你可能会更希望使用 Mixin 取代 ASM, Mixin 拥有更友好的交互与更容易上手的学习途径, 你可以参考 Fabric Wiki 的 Mixin 相关篇章来编写此部分.

> 请注意, 1.7.10 可能存在较多没有被混淆的部分, 你需要灵活使用 "无需反混淆" 标记.

> 将 UniMixins 加入 Gradle 工作区, 就可以开始你的 Mixin 之旅了!

