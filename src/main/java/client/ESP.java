package client.modules;

import client.Module;
import client.Setting;
import client.Client;
import java.util.Arrays;

public class ESP extends Module {
    public Setting style, health, names, boxes;

    public ESP() {
        super("ESP", 0, "Render");
        Client.instance.addSetting(style = new Setting("Style", this, "Vape Box", Arrays.asList("Vape Box", "2D Corner", "Skeleton")));
        Client.instance.addSetting(health = new Setting("Health Bar", this, 1.0, 0.0, 1.0));
        Client.instance.addSetting(names = new Setting("Names", this, 1.0, 0.0, 1.0));
        Client.instance.addSetting(boxes = new Setting("Box Scale", this, 2.0, 1.0, 5.0));
    }
}
