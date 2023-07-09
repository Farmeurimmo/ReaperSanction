package main.java.fr.farmeurimmo.reapersanction.gui;

public enum ActionErrorCodes {

    NO_SECTION(0),
    NOT_ENOUGH_ARGUMENTS(1),
    NO_DECISION(2),
    WHAT_GUI(3),
    NO_PLAYER_SELECTED(4),
    NO_GUI(5),
    NO_INTRUCTION(6);


    private int code;

    ActionErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
