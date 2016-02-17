package com.unify.ucevent;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.Calendar;

/* Author: Sandra Dai
 * File:   CheckEventInput.java
 * Contains methods to validate user-input when creating/editing an event
 */

public class CheckEventInput {
    private EditText title;
    private EditText location;
    private EditText email;
    private DatePicker date;
    private TimePicker start;
    private TimePicker end;

    public CheckEventInput(EditText eventTitle, EditText eventLoc, EditText contactEmail,
                           DatePicker eventDate, TimePicker eventStart, TimePicker eventEnd) {
        title = eventTitle;
        location = eventLoc;
        email = contactEmail;
        date = eventDate;
        start = eventStart;
        end = eventEnd;
    }

    /* Error Checking Methods */

    /* Title is required and must be unique */
    public boolean confirmTitle() {
        
        // Ensure non-empty
        if( title.getText().toString().matches("") ) {  
            return false;
        }

        // Ensure unique
        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("Title", title.getText().toString());
        int count;
        try {
            count = query.count();
        } catch (ParseException e) {
            return false;
        }
        return count <= 0;
    }
    
    /* Location is required */
    public boolean confirmLocation() {
        return !location.getText().toString().matches("");
    }
    
    /* Contact information must be specified */
    public boolean confirmContact() {
        
        // Checks for basic features of an email: "@", at least 3 characters (i.e. a@b )
        if( ParseUser.getCurrentUser().getEmail() == null ){
            if(email.getText().toString().matches("") ||
                    (email.getText().toString().length() < 3) ||
                    !email.getText().toString().contains("@")) {
                return false;
            }
        }
        return true;
    }
    
    public boolean confirmDate() {
        Calendar cal = Calendar.getInstance();

        System.out.println("Date.year: "+date.getYear());
        System.out.println("Calendar.year: "+cal.get(Calendar.YEAR));
        System.out.println("Date.month: "+date.getMonth());
        System.out.println("Calendar.month: "+cal.get(Calendar.MONTH));
        System.out.println("Date.day: "+date.getDayOfMonth());
        System.out.println("Calendar.day: "+cal.get(Calendar.DAY_OF_MONTH));

        if( date.getYear() < cal.get(Calendar.YEAR) ) {
            return false;
        }

        if( date.getMonth() < cal.get(Calendar.MONTH) ) {
            return false;
        }

        return !(date.getMonth() == cal.get(Calendar.MONTH) &&
                date.getDayOfMonth() < cal.get(Calendar.DAY_OF_MONTH));

    }
    public boolean confirmStartTime() {
        Calendar cal = Calendar.getInstance();

        if( confirmDate() ) {
            if( date.getMonth() == cal.get(Calendar.MONTH) &&
                    date.getDayOfMonth() == cal.get(Calendar.DAY_OF_MONTH) &&
                    date.getYear() == cal.get(Calendar.YEAR) ) {
                if(start.getCurrentHour() < cal.get(Calendar.HOUR_OF_DAY) ) {
                    return false;
                }
                if(start.getCurrentHour() == cal.get(Calendar.HOUR_OF_DAY) &&
                        start.getCurrentMinute() < cal.get(Calendar.MINUTE) ) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean confirmEndTime() {
        if( confirmDate() ) {
            if( end.getCurrentHour()<start.getCurrentHour() ){
                return false;
            }
            if (end.getCurrentHour() == start.getCurrentHour() &&
                    end.getCurrentMinute() < start.getCurrentMinute()) return false;
            return true;
        }
        return false;
    }
}
