package client.modules;

import client.Module;
import client.Setting;
import client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.item.ItemBlock;
import java.util.Arrays;

public class Scaffold extends Module {
    private final Minecraft mc = Minecraft.getMinecraft();
    public Setting mode;

    public Scaffold() {
        super("Scaffold", 28, "Combat");
        Client.instance.addSetting(mode = new Setting("Mode", this, "Legit", Arrays.asList("Legit")));
    }

    public void onTickUpdate() {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        // Ensure the local player is currently holding placeable blocks
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) return;

        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(playerPos).getBlock().getMaterial().isReplaceable()) {
            
            BlockPos targetPos = playerPos.down();
            EnumFacing facing = EnumFacing.UP;
            
            // Set silent server rotations
            mc.thePlayer.rotationYaw = 180.0F; 
            mc.thePlayer.rotationPitch = 82.0F;

            // Bypasses the invisible errors by letting the entity engine force sneak states directly
            mc.thePlayer.setSneaking(true);

            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), targetPos, facing, new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ()));
            mc.thePlayer.swingItem();
        } else {
            mc.thePlayer.setSneaking(false);
        }
    }

    public void onModuleDisabled() {
        if (mc.thePlayer != null) {
            mc.thePlayer.setSneaking(false);
        }
    }
}
