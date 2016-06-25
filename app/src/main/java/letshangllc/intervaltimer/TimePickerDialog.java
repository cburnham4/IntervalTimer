package letshangllc.intervaltimer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by Carl on 6/24/2016.
 */
public class TimePickerDialog extends DialogFragment {

    NumberPicker numberPickerMinute;
    NumberPicker numberPickerSecond;
    private int minutes, seconds;


    public void setMinutes(int minutes){
        this.minutes = minutes;
    }
    public void setSeconds(int seconds){
        this.seconds = seconds;
    }

    public interface Listener{
        void postitiveClick(int minutes, int seconds);
    }

    private Listener listener;
    private Context context;


    public void setCallback(Listener listener){
        this.listener = listener;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_number_picker, null);

        context = this.getContext();

        /* call the private method to set up the number pickers */
        this.setUpNumberPickers(view);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        numberPickerMinute.clearFocus();
                        numberPickerSecond.clearFocus();

                        int minutes = numberPickerMinute.getValue();
                        int seconds = numberPickerSecond.getValue();

                        listener.postitiveClick(minutes, seconds);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TimePickerDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


    private void setUpNumberPickers(View view){
        numberPickerMinute = (NumberPicker) view.findViewById(R.id.nmp_minute);
        numberPickerMinute.setMaxValue(60);
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setValue(minutes);

        numberPickerSecond = (NumberPicker) view.findViewById(R.id.nmp_second);
        numberPickerSecond.setMaxValue(60);
        numberPickerSecond.setMinValue(0);
        numberPickerSecond.setValue(seconds);
    }
}