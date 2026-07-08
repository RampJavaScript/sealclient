package client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import client.modules.*;

public class Client {
    public static final Client instance = new Client();
    
    private final List<Module> modules = new ArrayList<Module>();
    private final List<Setting> settings = new ArrayList<Setting>();
    
    // Global UI Customization States
    public Setting guiRainbow;
    public Setting guiRed;
    public Setting guiGreen;
    public Setting guiBlue;

    public void init() {
        if (!this.modules.isEmpty()) {
            return;
        }

        // --- Register Base Hacks ---
        registerModule(new Scaffold());
        registerModule(new ESP());
        registerModule(new Freelook());
        registerModule(new Sprint());

        // --- Global UI Configuration Controls ---
        addSetting(guiRainbow = new Setting("HUD Rainbow", null, true));
        addSetting(guiRed = new Setting("HUD Color R", null, 127.0, 0.0, 255.0));
        addSetting(guiGreen = new Setting("HUD Color G", null, 0.0, 0.0, 255.0));
        addSetting(guiBlue = new Setting("HUD Color B", null, 255.0, 0.0, 255.0));

        openClickGui();
    }

    private void registerModule(Module module) {
        if (module != null && !this.modules.contains(module)) {
            this.modules.add(module);
        }
    }

    public void openClickGui() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null) {
            mc.displayGuiScreen(new ClickGUI());
        }
    }

    public void addSetting(Setting s) { 
        this.settings.add(s); 
    }
    
    public List<Module> getModules() { 
        return this.modules; 
    }
    
    public List<Setting> getSettingsByMod(Module mod) {
        List<Setting> modSettings = new ArrayList<Setting>();
        for (Setting s : this.settings) {
            if (s.getParent() == mod) {
                modSettings.add(s);
            }
        }
        return modSettings;
    }
    
    public List<Setting> getGlobalSettings() {
        List<Setting> glob = new ArrayList<Setting>();
        for (Setting s : this.settings) {
            if (s.getParent() == null) {
                glob.add(s);
            }
        }
        return glob;
    }

    // Call this inside your main game tick hook loop (Minecraft.java runTick)
    public void onTick() {
        for (Module m : this.modules) {
            if (m.isToggled()) {
                if (m instanceof client.modules.Scaffold) {
                    ((client.modules.Scaffold) m).onTickUpdate();
                } else {
                    m.onUpdate();
                }
            }
        }
    }

    public void onKeyPress(int key) {
        // If user presses Right Shift (Key code 54 in LWJGL), open the custom UI
        if (key == 54) {
            openClickGui();
            return;
        }

        // Handle regular hotkey binds
        for (Module m : this.modules) {
            if (m.getKey() == key) {
                m.toggle();
                
                // Fire fallback disable trigger if Scaffold is flipped off
                if (!m.isToggled() && m instanceof client.modules.Scaffold) {
                    ((client.modules.Scaffold) m).onModuleDisabled();
                }
            }
        }
    }
}
