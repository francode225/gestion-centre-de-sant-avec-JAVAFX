package com.example.javaweb.alem.model;

import javax.swing.*;

public class SoinsModel {

    public static SoinsModel getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder{
        private static final SoinsModel INSTANCE = new SoinsModel();
    }




}
