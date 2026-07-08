package client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.Gui;

public class ClickGUI extends GuiScreen {

    private String currentCategory = "Combat";

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Safe Check: Force close the UI if the player disconnects or leaves the world
        if (this.mc.theWorld == null) {
            this.mc.displayGuiScreen(null);
            return;
        }

        // Tinted overlay behind the panels
        this.drawDefaultBackground();

        // Centered Canvas math for Vape v4 floating framework (500x320 panel)
        int x = (this.width - 500) / 2;
        int y = (this.height - 320) / 2;

        // Custom Active Color Selector Engine
        int themeColor;
        if (Client.instance.guiRainbow.isRainbow()) {
            long time = System.currentTimeMillis();
            themeColor = java.awt.Color.HSBtoRGB((time % 4000) / 4000.0f, 0.75f, 0.9f);
        } else {
            themeColor = ((int)Client.instance.guiRed.getValDouble() << 16) | 
                         ((int)Client.instance.guiGreen.getValDouble() << 8) | 
                         ((int)Client.instance.guiBlue.getValDouble());
        }

        // 1. Left Sidebar Navigation Bar (Category Column)
        Gui.drawRect(x, y, x + 110, y + 320, 0xFF141414); // Dark side panel
        this.mc.fontRendererObj.drawStringWithShadow("SEAL v4", x + 15, y + 15, themeColor); // Branding Accent

        // Sidebar Category Tabs
        String[] categories = {"Combat", "Movement", "Render", "Player", "Config"};
        int catY = y + 50;
        for (String cat : categories) {
            boolean isSelected = currentCategory.equals(cat);
            // Draw square selection strip
            if (isSelected) {
                Gui.drawRect(x, catY - 2, x + 3, catY + 12, themeColor);
            }
            this.mc.fontRendererObj.drawString(cat, x + 15, catY, isSelected ? 0xFFFFFFFF : 0xFF777777);
            catY += 24;
        }

        // 2. Main Center Content Box (Workspace Canvas)
        Gui.drawRect(x + 110, y, x + 500, y + 320, 0xFF1B1B1B); // Main window fill

        if (currentCategory.equals("Config")) {
            // Render Global Design customization settings sliders
            int setY = y + 20;
            for (Setting s : Client.instance.getGlobalSettings()) {
                this.mc.fontRendererObj.drawString(s.getName(), x + 130, setY, 0xFFFFFFFF);
                if (s.getType().equals("BOOLEAN")) {
                    String state = s.isRainbow() ? "[ RAINBOW ]" : "[ RGB MODE ]";
                    this.mc.fontRendererObj.drawString(state, x + 320, setY, themeColor);
                } else if (s.getType().equals("NUMBER")) {
                    Gui.drawRect(x + 320, setY + 2, x + 450, setY + 6, 0xFF333333);
                    double percentage = (s.getValDouble() - s.getMin()) / (s.getMax() - s.getMin());
                    Gui.drawRect(x + 320, setY + 2, x + 320 + (int)(percentage * 130), setY + 6, themeColor);
                    this.mc.fontRendererObj.drawString(String.valueOf((int)s.getValDouble()), x + 460, setY, 0xFF888888);
                }
                setY += 30;
            }
        } else {
            // Render Module Cards list Grid
            int moduleX = x + 125;
            int moduleY = y + 20;
            int count = 0;

            for (Module m : Client.instance.getModules()) {
                if (m.getCategory().equalsIgnoreCase(currentCategory)) {
                    
                    // Track mouse position over the button card
                    boolean hovered = mouseX >= moduleX && mouseX <= moduleX + 110 && mouseY >= moduleY && mouseY <= moduleY + 35;
                    
                    // Vape-style Module Square Cards
                    int cardColor = m.isToggled() ? 0xFF242424 : (hovered ? 0xFF202020 : 0xFF181818);
                    Gui.drawRect(moduleX, moduleY, moduleX + 110, moduleY + 35, cardColor);
                    
                    // Left status indicator line
                    Gui.drawRect(moduleX, moduleY, moduleX + 2, moduleY + 35, m.isToggled() ? themeColor : 0xFF333333);

                    // Module Label
                    this.mc.fontRendererObj.drawString(m.getName(), moduleX + 8, moduleY + 8, m.isToggled() ? 0xFFFFFFFF : 0xFF999999);
                    
                    // Render sub-settings option hint icon
                    this.mc.fontRendererObj.drawString("...", moduleX + 95, moduleY + 22, 0xFF555555);

                    // Grid layout placement math
                    count++;
                    if (count % 3 == 0) {
                        moduleX = x + 125;
                        moduleY += 42;
                    } else {
                        moduleX += 120;
                    }
                }
            }
        }

        // Render the Dynamic Sorted HUD arraylist on the overlay layer
        this.drawArrayList(this.width, themeColor);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawArrayList(int width, int themeColor) {
        java.util.List<Module> activeMods = new java.util.ArrayList<>();
        for (Module m : Client.instance.getModules()) { 
            if (m.isToggled()) activeMods.add(m); 
        }

        // Advanced String Length Comparator Sorting Logic
        activeMods.sort((m1, m2) -> {
            String s1 = m1.getName() + (m1.getName().equals("Scaffold") ? " Legit" : "");
            String s2 = m2.getName() + (m2.getName().equals("Scaffold") ? " Legit" : "");
            return Integer.compare(this.mc.fontRendererObj.getStringWidth(s2), this.mc.fontRendererObj.getStringWidth(s1));
        });

        int yOffset = 4;
        for (Module m : activeMods) {
            String name = m.getName() + (m.getName().equals("Scaffold") ? " \u00A77Legit" : "");
            int stringWidth = this.mc.fontRendererObj.getStringWidth(name);
            
            // Clean rectangular array outline bars
            Gui.drawRect(width - stringWidth - 8, yOffset, width, yOffset + 12, 0xAA000000); // Dark backplate
            Gui.drawRect(width - 2, yOffset, width, yOffset + 12, themeColor); // Vertical accent line
            this.mc.fontRendererObj.drawStringWithShadow(name, width - stringWidth - 5, yOffset + 2, themeColor);
            yOffset += 13;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int x = (this.width - 500) / 2;
        int y = (this.height - 320) / 2;

        if (mouseButton == 0) {
            // Category Panel click detection
            int catY = y + 50;
            String[] categories = {"Combat", "Movement", "Render", "Player", "Config"};
            for (String cat : categories) {
                if (mouseX >= x && mouseX <= x + 110 && mouseY >= catY && mouseY <= catY + 20) {
                    currentCategory = cat;
                    return;
                }
                catY += 24;
            }

            // Click handling logic inside the Config Tab
            if (currentCategory.equals("Config")) {
                int setY = y + 20;
                for (Setting s : Client.instance.getGlobalSettings()) {
                    if (s.getType().equals("BOOLEAN") && mouseX >= x + 320 && mouseX <= x + 420 && mouseY >= setY && mouseY <= setY + 15) {
                        s.setRainbow(!s.isRainbow());
                        return;
                    }
                    if (s.getType().equals("NUMBER") && mouseX >= x + 320 && mouseX <= x + 450 && mouseY >= setY && mouseY <= setY + 15) {
                        double percentage = (double)(mouseX - (x + 320)) / 130.0;
                        double val = s.getMin() + (percentage * (s.getMax() - s.getMin()));
                        
                        // Save configuration adjustments back to Client variables
                        if (s.getName().contains("R")) Client.instance.guiRed.setValDouble(val);
                        if (s.getName().contains("G")) Client.instance.guiGreen.setValDouble(val);
                        if (s.getName().contains("B")) Client.instance.guiBlue.setValDouble(val);
                        Client.instance.guiRainbow.setRainbow(false); // Automatically disable rainbow mode when using single sliders
                        return;
                    }
                    setY += 30;
                }
            } else {
                // Click handling logic inside the Modules grid tabs
                int moduleX = x + 125; 
                int moduleY = y + 20; 
                int count = 0;
                for (Module m : Client.instance.getModules()) {
                    if (m.getCategory().equalsIgnoreCase(currentCategory)) {
                        if (mouseX >= moduleX && mouseX <= moduleX + 110 && mouseY >= moduleY && mouseY <= moduleY + 35) {
                            m.toggle();
                            return;
                        }
                        count++;
                        if (count % 3 == 0) { 
                            moduleX = x + 125; 
                            moduleY += 42; 
                        } else { 
                            moduleX += 120; 
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
