package client.modules;
import client.Module;
import net.minecraft.client.Minecraft;

public class Sprint extends Module {
    public Sprint() { super("Sprint", 47, "Movement"); } // V Key
    @Override
    public void onUpdate() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null && mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
