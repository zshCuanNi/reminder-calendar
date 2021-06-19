package com.example.reminder_calendar;

import java.util.ArrayList;

public class ListContent {
    public static ArrayList<String> titleDataSet = new ArrayList<>();
    public static ArrayList<String> contentDataSet = new ArrayList<>();
    public static ArrayList<String> timeDataSet = new ArrayList<>();
    public static ArrayList<Integer> idDataset = new ArrayList<>();
    public static void clearData() {
        titleDataSet.clear();
        contentDataSet.clear();
        timeDataSet.clear();
        idDataset.clear();
    }
}
