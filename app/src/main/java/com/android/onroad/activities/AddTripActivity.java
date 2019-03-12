package com.android.onroad.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.onroad.R;
import com.android.onroad.beans.Note;
import com.android.onroad.beans.Trip;
import com.google.android.gms.common.api.Status;


import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity {
    private static final String TAG = "Error...";
    Button btnTimePicker, btnDatePicker, AddTrip;
    TextView txtDate, txtTime;
    Spinner spnRepeat, spnStatus;

    EditText tripName, myNote;
    Date date;
    Date myDateCheck;

    String  myStartPoint  = "" , myEndPoint = "", sLat = "", sLong = "", ePoint = "", eLat = "", eLong = "";
    double mysLat, mysLong, myeLat, myeLong;
    ArrayList<Note> myArrayNote = new ArrayList<>();

    Date myDate = new Date();

    Date myTime;
    String myStatus,myRepeat;


     String myTripName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        btnDatePicker = findViewById(R.id.btnAddDate);
        btnTimePicker = findViewById(R.id.btnAddTime);
        txtDate = findViewById(R.id.in_date_add);
        txtTime = findViewById(R.id.in_time_add);

        tripName = findViewById(R.id.txtTripName);
        AddTrip = findViewById(R.id.btnAddTrip);
        spnRepeat = findViewById(R.id.spnRepeat);
        spnStatus = findViewById(R.id.spnStatus);
        myNote = findViewById(R.id.txtAddNote);

        AddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trip myTrip = new Trip();
               myTripName= tripName.getText().toString();
               myTrip.setTripName(myTripName);
               myTrip.setDateTime(myDate);
               myTrip.setEndPoint(myEndPoint);
               myTrip.setStartPoint(myStartPoint);
               myTrip.setLatEndPoint(myeLat);
               myTrip.setLangEndPoint(myeLong);
               myTrip.setLatStartPoint(mysLat);
               myTrip.setLangStartPoint(mysLong);
               myTrip.setNotes(myArrayNote);
               myTrip.setRepeat(myRepeat);
               myTrip.setStatus(myStatus);

//                Intent myIntent = new Intent(AddTripActivity.this,HomeActivity.class);
//                myIntent.putExtra("myTrip",myTrip);
//                startActivity(myIntent);
            }
        });

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myStatus = spnStatus.getItemAtPosition(position).toString();
                Toast.makeText(AddTripActivity.this, myStatus, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myRepeat = spnRepeat.getItemAtPosition(position).toString();
                Toast.makeText(AddTripActivity.this, myRepeat, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtTime.setText(selectedHour + ":" + selectedMinute);
                        myTime = new Date();
                        myTime.setHours(selectedHour);
                        myTime.setMinutes(selectedMinute);
                        myDate.setHours(selectedHour);
                        myDate.setMinutes(selectedMinute);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
              int  mYear = c.get(Calendar.YEAR);
               int mMonth = c.get(Calendar.MONTH);
             int   mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String mySDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                try {
                                    date = format.parse(timeStamp);
                                    myDateCheck = format.parse(mySDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(myDateCheck.equals(null)) {
                                    try {
                                        myDateCheck = format.parse(timeStamp);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    if (myDateCheck.before(date)) {
                                        Toast.makeText(view.getContext(), "Enter Valid Date", Toast.LENGTH_SHORT).show();
                                    } else {
                                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        myDate.setMonth(monthOfYear+1);
                                        myDate.setYear(year);
                                        myDate.setDate(dayOfMonth);

                                    }
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        PlaceAutocompleteFragment autocompleteFragment1 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.txtStartPoint);
        if (autocompleteFragment1 != null)
            autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "Place: " + place.getName());
                    myStartPoint = place.getName().toString();
                    LatLng myLatLong = place.getLatLng();
                    mysLat = myLatLong.latitude;
                    mysLong = myLatLong.longitude;
                    sLat = mysLat + "";
                    sLong = mysLong + "";


                }

                @Override
                public void onError(com.google.android.gms.common.api.Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        else Toast.makeText(this, "Problem with loading page", Toast.LENGTH_LONG).show();


        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.txtEndPoint);
        if (autocompleteFragment2 != null )
            autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place./
                    Log.i(TAG, "Place: " + place.getName());
                    String placeName = place.getName().toString();
                    Toast.makeText(AddTripActivity.this, "the place is "+ placeName ,Toast.LENGTH_SHORT).show();
                    myEndPoint = place.getName().toString();
                    LatLng myLatLong = place.getLatLng();
                    myeLat = myLatLong.latitude;
                    myeLong = myLatLong.longitude;
                    eLat = myeLat + "";
                    eLong = myeLong + "";
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        else Toast.makeText(this, "Problem with loading page", Toast.LENGTH_LONG).show();
    }


    public void addNote(View view) {
        if(myNote.getText().toString().equals(""))
            Toast.makeText(this, "enter Note", Toast.LENGTH_SHORT).show();
        else{
            Note n = new Note();
            n.setNote(myNote.getText().toString());
            myArrayNote.add(n);
            Toast.makeText(this, "note is added", Toast.LENGTH_SHORT).show();
            myNote.setText("");

        }
    }
}
