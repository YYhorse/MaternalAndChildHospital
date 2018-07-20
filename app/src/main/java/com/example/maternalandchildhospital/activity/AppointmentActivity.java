package com.example.maternalandchildhospital.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.publics.view.CalendarPickerDialog;

public class AppointmentActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
    }

    public void ClickAppointmentMethod(View view){
        CalendarPickerDialog.TurnoverChooseCalendar(this);
    }
}
