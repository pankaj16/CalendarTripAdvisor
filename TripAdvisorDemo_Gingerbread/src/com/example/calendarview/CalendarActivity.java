package com.example.calendarview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.tripadvisor.CalendarView;
import com.tripadvisor.CalendarView.SelectionMode;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.tripadvisor.CalendarView.DateSelectableFilter;
import static com.tripadvisor.CalendarView.OnInvalidDateSelectedListener;
import static com.tripadvisor.CalendarView.SelectionMode.RANGE;
import static com.tripadvisor.CalendarView.SelectionMode.SINGLE;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;


@SuppressWarnings("deprecation")
public class CalendarActivity extends Activity implements TabListener, DateSelectableFilter, OnInvalidDateSelectedListener {

    private CalendarView mCalendarView;
    private Calendar today = Calendar.getInstance();
    private Locale locale;

    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mCalendarView = (CalendarView) findViewById(R.id.calendar_view);
        Button button = (Button) findViewById(R.id.button);
        locale = Locale.US;
        today = Calendar.getInstance(locale);
        final Calendar dec2012 = buildCal(2013, DECEMBER, 1);
        final Calendar dec2013 = buildCal(2020, DECEMBER, 1);
        Calendar march2013 = buildCal(2014, MARCH, 1);
        mCalendarView.init(dec2012.getTime(), dec2013.getTime(), locale)
                .inMode(RANGE).withSelectedDate(march2013.getTime());
        setMidnight(today);
//        final ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//            ActionBar.Tab oneWayTab = actionBar.newTab();
//            ActionBar.Tab roundTripTab = actionBar.newTab();
//            ActionBar.Tab multipleTab = actionBar.newTab();
//            oneWayTab.setText("Select Day");
//            roundTripTab.setText("Select Range");
//            multipleTab.setText("Multiple");
//            oneWayTab.setTabListener(this);
//            roundTripTab.setTabListener(this);
//            multipleTab.setTabListener(this);
//            actionBar.addTab(oneWayTab);
//            actionBar.addTab(roundTripTab);
//            actionBar.addTab(multipleTab);
//        }
        
        button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StringBuilder stringBuilder = new StringBuilder();
				for(Date date : mCalendarView.getSelectedDates()){
					stringBuilder.append(date.toString()+"\n");
				}
				DisplayMessage(stringBuilder.toString());
			}
		});
        
    }
    
    public void DisplayMessage(String messageString) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(CalendarActivity.this);
		dlgAlert.setTitle("Date");
		dlgAlert.setMessage(messageString);
		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

    /**
     * Clears out the hours/minutes/seconds/millis of a Calendar.
     */
    private static void setMidnight(Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
    }

    @Override
    public boolean isDateSelectable(Date date) {
        boolean selectable = false;
        if (today.getTime().compareTo(date) <= 0) {
            selectable = true;
        }
        return selectable;
    }

    @Override
    public void onInvalidDateSelected(Date date) {
        Log.d("CalendarActivity", "onInvalidDateSelected:" + date.toString());
    }

    private Calendar buildCal(int year, int month, int day) {
        Calendar jumpToCal = Calendar.getInstance(locale);
        jumpToCal.set(year, month, day);
        return jumpToCal;
    }


	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction arg1) {
		 if (tab.getPosition() == 0) {
	            mCalendarView.setSelectionMode(SINGLE);
	        } else if (tab.getPosition() == 1) {
	            mCalendarView.setSelectionMode(RANGE);
	        } else if (tab.getPosition() == 2) {
	            mCalendarView.setSelectionMode(SelectionMode.MULTIPLE);
	        }
	}


	@Override
	public void onTabUnselected(Tab tab,
			android.support.v4.app.FragmentTransaction arg1) {
		Log.d("CalendarActivity", "Tab Unselected:"+tab.getText());
	}
}
