package client;

public class Module {
    private String name;
    private int key;
    private boolean toggled;
    private String category;

    public Module(String name, int key, String category) {
        this.name = name;
        this.key = key;
        this.category = category;
        this.toggled = false;
    }

    public String getName() { return name; }
    public int getKey() { return key; }
    public boolean isToggled() { return toggled; }
    public String getCategory() { return category; }

    public void toggle() {
        this.toggled = !this.toggled;
        if (this.toggled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
}
