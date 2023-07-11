package fr.farmeurimmo.reapersanction.gui;

public class GuiManager {

    public static GuiManager INSTANCE;

    public GuiManager() {
        INSTANCE = this;

        new ActionGuiInterpreter();

        new CustomInventories();
    }
}
