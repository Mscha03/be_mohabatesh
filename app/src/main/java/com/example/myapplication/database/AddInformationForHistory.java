package com.example.myapplication.database;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.example.myapplication.model.Period;

public interface AddInformationForHistory {

    static void addDays(String period, long id, RoutineDB db){

        JalaliDateTime jalaliDateTime = JalaliDateTime.Now();

        if (period.equals(Period.daily.toString())){
            for (int i = 1; i <= 6 ; i++) {
                for (int j = 1; j <= 31; j++) {
                    db.insertDays((int)id, 0, j, 0, i, jalaliDateTime.getYear());
                }
            }
            for (int i = 7; i <= 11 ; i++) {
                for (int j = 1; j <= 30; j++) {
                    db.insertDays((int)id, 0, j, 0, i, jalaliDateTime.getYear());
                }
            }
            for (int j = 1; j <= 29; j++) {
                db.insertDays((int)id, 0, j, 0, 12, jalaliDateTime.getYear());
            }

        }
        else if (period.equals(Period.weekly.toString())){
            for (int i = 1; i <= 52; i++) {
                db.insertDays((int)id, 0, 0, i, 0, jalaliDateTime.getYear());
            }


        }
        else if (period.equals(Period.monthly.toString())){
            for (int i = 1; i <= 12; i++) {
                db.insertDays((int)id, 0, 0, 0, i, jalaliDateTime.getYear());
            }
        }

    }
}
