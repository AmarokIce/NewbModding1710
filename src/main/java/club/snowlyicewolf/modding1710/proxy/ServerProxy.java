package club.snowlyicewolf.modding1710.proxy;

import club.snowlyicewolf.modding1710.ModMain;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public final class ServerProxy implements IProxy {
    @Override
    public void preInit() {
        ModMain.LOGGER.info("Server Proxy PreInit");
    }

    @Override
    public void init() {
        ModMain.LOGGER.info("Server Proxy Init");
    }
}
