package client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen {

    private String currentCategory = "All";

    public ClickGUI() {
        this.allowUserInput = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int x = (this.width - 540) / 2;
        int y = (this.height - 340) / 2;

        int themeColor;
        if (Client.instance.guiRainbow.isRainbow()) {
            long time = System.currentTimeMillis();
            themeColor = java.awt.Color.HSBtoRGB((time % 4000) / 4000.0f, 0.75f, 0.9f);
        } else {
            themeColor = ((int) Client.instance.guiRed.getValDouble() << 16)
                    | ((int) Client.instance.guiGreen.getValDouble() << 8)
                    | (int) Client.instance.guiBlue.getValDouble();
        }

        Gui.drawRect(x, y, x + 120, y + 340, 0xFF131313);
        Gui.drawRect(x + 120, y, x + 540, y + 340, 0xFF1B1B1B);
        Gui.drawRect(x + 6, y + 10, x + 114, y + 34, themeColor);
        this.mc.fontRendererObj.drawStringWithShadow("SEAL GUI", x + 14, y + 16, 0xFFFFFFFF);

        String[] categories = {"All", "Combat", "Movement", "Render", "Player", "Config"};
        int catY = y + 52;
        for (String cat : categories) {
            boolean selected = currentCategory.equals(cat);
            Gui.drawRect(x + 8, catY - 2, x + 16, catY + 12, selected ? themeColor : 0xFF333333);
            this.mc.fontRendererObj.drawString(cat, x + 24, catY, selected ? 0xFFFFFFFF : 0xFF8A8A8A);
            catY += 24;
        }

        this.mc.fontRendererObj.drawStringWithShadow("Modules", x + 138, y + 16, 0xFFFFFFFF);

        if (currentCategory.equals("Config")) {
            int setY = y + 50;
            for (Setting s : Client.instance.getGlobalSettings()) {
                this.mc.fontRendererObj.drawString(s.getName(), x + 140, setY, 0xFFFFFFFF);
                if (s.getType().equals("BOOLEAN")) {
                    String state = s.isRainbow() ? "[ RAINBOW ]" : "[ RGB MODE ]";
                    this.mc.fontRendererObj.drawString(state, x + 330, setY, themeColor);
                } else if (s.getType().equals("NUMBER")) {
                    Gui.drawRect(x + 330, setY + 2, x + 460, setY + 6, 0xFF333333);
                    double percentage = (s.getValDouble() - s.getMin()) / (s.getMax() - s.getMin());
                    Gui.drawRect(x + 330, setY + 2, x + 330 + (int) (percentage * 130), setY + 6, themeColor);
                    this.mc.fontRendererObj.drawString(String.valueOf((int) s.getValDouble()), x + 470, setY, 0xFF8A8A8A);
                }
                setY += 24;
            }
        } else {
            List<Module> visibleModules = getVisibleModules();
            int moduleX = x + 140;
            int moduleY = y + 46;
            int count = 0;
            if (visibleModules.isEmpty()) {
                this.mc.fontRendererObj.drawString("No modules here", x + 140, y + 70, 0xFFAAAAAA);
            } else {
                for (Module module : visibleModules) {
                    boolean hovered = mouseX >= moduleX && mouseX <= moduleX + 110 && mouseY >= moduleY && mouseY <= moduleY + 32;
                    int cardColor = module.isToggled() ? 0xFF252525 : (hovered ? 0xFF202020 : 0xFF181818);
                    Gui.drawRect(moduleX, moduleY, moduleX + 110, moduleY + 32, cardColor);
                    Gui.drawRect(moduleX, moduleY, moduleX + 3, moduleY + 32, module.isToggled() ? themeColor : 0xFF444444);
                    this.mc.fontRendererObj.drawString(module.getName(), moduleX + 8, moduleY + 9, module.isToggled() ? 0xFFFFFFFF : 0xFFB2B2B2);
                    this.mc.fontRendererObj.drawString(module.isToggled() ? "ON" : "OFF", moduleX + 72, moduleY + 9, module.isToggled() ? themeColor : 0xFF666666);

                    count++;
                    if (count % 3 == 0) {
                        moduleX = x + 140;
                        moduleY += 42;
                    } else {
                        moduleX += 120;
                    }
                }
            }
        }

        drawArrayList(this.width, themeColor);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private List<Module> getVisibleModules() {
        List<Module> modules = new ArrayList<Module>();
        for (Module module : Client.instance.getModules()) {
            if (currentCategory.equals("All") || module.getCategory().equalsIgnoreCase(currentCategory)) {
                modules.add(module);
            }
        }
        return modules;
    }

    private void drawArrayList(int width, int themeColor) {
        List<Module> activeMods = new ArrayList<Module>();
        for (Module module : Client.instance.getModules()) {
            if (module.isToggled()) {
                activeMods.add(module);
            }
        }

        activeMods.sort((m1, m2) -> Integer.compare(this.mc.fontRendererObj.getStringWidth(m2.getName()), this.mc.fontRendererObj.getStringWidth(m1.getName())));

        int yOffset = 4;
        for (Module module : activeMods) {
            String name = module.getName();
            int stringWidth = this.mc.fontRendererObj.getStringWidth(name);
            Gui.drawRect(width - stringWidth - 10, yOffset, width, yOffset + 12, 0xAA000000);
            Gui.drawRect(width - 2, yOffset, width, yOffset + 12, themeColor);
            this.mc.fontRendererObj.drawStringWithShadow(name, width - stringWidth - 7, yOffset + 2, themeColor);
            yOffset += 13;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int x = (this.width - 540) / 2;
        int y = (this.height - 340) / 2;

        if (mouseButton != 0) {
            return;
        }

        int catY = y + 52;
        String[] categories = {"All", "Combat", "Movement", "Render", "Player", "Config"};
        for (String cat : categories) {
            if (mouseX >= x + 8 && mouseX <= x + 116 && mouseY >= catY - 2 && mouseY <= catY + 14) {
                currentCategory = cat;
                return;
            }
            catY += 24;
        }

        if (currentCategory.equals("Config")) {
            int setY = y + 50;
            for (Setting s : Client.instance.getGlobalSettings()) {
                if (s.getType().equals("BOOLEAN") && mouseX >= x + 330 && mouseX <= x + 430 && mouseY >= setY && mouseY <= setY + 15) {
                    s.setRainbow(!s.isRainbow());
                    return;
                }
                if (s.getType().equals("NUMBER") && mouseX >= x + 330 && mouseX <= x + 460 && mouseY >= setY && mouseY <= setY + 15) {
                    double percentage = (double) (mouseX - (x + 330)) / 130.0;
                    double val = s.getMin() + (percentage * (s.getMax() - s.getMin()));
                    if (s.getName().contains("R")) Client.instance.guiRed.setValDouble(val);
                    if (s.getName().contains("G")) Client.instance.guiGreen.setValDouble(val);
                    if (s.getName().contains("B")) Client.instance.guiBlue.setValDouble(val);
                    Client.instance.guiRainbow.setRainbow(false);
                    return;
                }
                setY += 24;
            }
            return;
        }

        int moduleX = x + 140;
        int moduleY = y + 46;
        int count = 0;
        for (Module module : getVisibleModules()) {
            if (mouseX >= moduleX && mouseX <= moduleX + 110 && mouseY >= moduleY && mouseY <= moduleY + 32) {
                module.toggle();
                return;
            }
            count++;
            if (count % 3 == 0) {
                moduleX = x + 140;
                moduleY += 42;
            } else {
                moduleX += 120;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
