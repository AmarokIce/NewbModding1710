package club.snowlyicewolf.example.mod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

// 可以打开这个注解的详细类，Forge 设有详细的文档。
@Mod(
        // 这里是我们的 ModID。
        modid = ModMain.ID,
        name = ModMain.NAME,
        version = ModMain.VERSION,

        // 如果为 true 则将会要求 Forge 读取 mcmod.info 覆盖大部分设定。
        useMetadata = false,
        // 默认就是 Java，当然你可以选择 Scala，如果使用 Kotlin,需要安装第三方模组。
        modLanguage = "java",
        // 如果你修改了语言，需要提供一个适配器。适配器用于解析主要类中的魔法，例如 @Proxy 与 @Instance。
        modLanguageAdapter = "",
        // 远端版本，远端是指客户端。如果远端版本为任意，意味着这是一个客户端可选模组。
        acceptableRemoteVersions = "*",
        // 模组关系，例如：{@code required-after:mymod;} 意思为依赖 mymod 且处于之后加载。如果去掉 required-，则是仅滞后。
        dependencies = ""
)
public class ModMain {
    public static final String ID = "example_mod";
    public static final String NAME = "Example Mod";
    public static final String VERSION = "0.0.1";

    // 魔法设置主类实例。
    @Mod.Instance(ModMain.ID)
    public static ModMain instance;

    // @Proxy


    // 初始化加载，通常我们在这完成大部分注册，包括配置文件。
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    // 标准加载，这是你最后注册的机会。通常处理一些标准流程和注册过之后要做的事情。
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {}

    // 通讯加载，这里用于与其他模组使用封包交流，这是一种灵活的联动方式，例如 Waila 就是采用这种方式与其他模组交流。
    // 请不要在这里注册事件与封包之外的东西，除非你是个 🦀。
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
}
