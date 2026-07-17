package org.unisiga.main;

import java.awt.EventQueue;
import org.unisiga.controller.MenuPrincipalController;
import org.unisiga.view.MenuPrincipalView;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MenuPrincipalView vista = new MenuPrincipalView();
            new MenuPrincipalController(vista);
            vista.setVisible(true);
        });
    }
}

