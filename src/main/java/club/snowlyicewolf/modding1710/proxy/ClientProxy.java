package club.snowlyicewolf.modding1710.proxy;

import club.snowlyicewolf.modding1710.ModMain;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ClientProxy implements IProxy {
    @Override
    public void preInit() {
        ModMain.LOGGER.info("Client Proxy PreInit");
    }

    @Override
    public void init() {
        ModMain.LOGGER.info("Client Proxy Init");
    }
}
