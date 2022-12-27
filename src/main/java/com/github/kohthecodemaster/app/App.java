package com.github.kohthecodemaster.app;

import com.github.kohthecodemaster.controller.MainController;
import stdlib.utils.MyTimer;

public class App {

    public static void main(String[] args) {

        MyTimer myTimer = new MyTimer();
        myTimer.startTimer();

        App obj = new App();
        obj.major();

        myTimer.stopTimer(true);

    }

    private void major() {

        System.out.println("Begin.\n");

        init();

        System.out.println("\nEnd.");

    }

    private void init() {

        MainController mainController = new MainController();
        mainController.major();

    }

}
