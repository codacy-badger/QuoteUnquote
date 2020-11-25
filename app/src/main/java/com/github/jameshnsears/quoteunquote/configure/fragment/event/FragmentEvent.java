package com.github.jameshnsears.quoteunquote.configure.fragment.event;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TimePicker;

import com.github.jameshnsears.quoteunquote.configure.fragment.FragmentCommon;
import com.github.jameshnsears.quoteunquote.databinding.FragmentEventBinding;

public class FragmentEvent extends FragmentCommon {
    public FragmentEventBinding fragmentEventBinding;
    public PreferenceEvent preferenceEvent;

    protected FragmentEvent(final int widgetId) {
        super(widgetId);
    }

    public static FragmentEvent newInstance(final int widgetId) {
        final FragmentEvent fragment = new FragmentEvent(widgetId);
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        preferenceEvent = new PreferenceEvent(this.widgetId, this.getContext());

        fragmentEventBinding = FragmentEventBinding.inflate(getLayoutInflater());
        return fragmentEventBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentEventBinding = null;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        setDeviceUnlock();
        setDaily();
        setDailyTime();

        createListenerDeviceUnlock();
        createListenerDaily();
        createListenerDailyTime();
    }

    private void setDaily() {
        final boolean booleanDaily = preferenceEvent.getEventDaily();
        //preferenceEvent.setEventDaily(booleanDaily);

        fragmentEventBinding.checkBoxDailyAt.setChecked(booleanDaily);

        final TimePicker timePicker = fragmentEventBinding.timePickerDailyAt;

        timePicker.setEnabled(false);
        if (booleanDaily) {
            timePicker.setEnabled(true);
        }
    }

    private void setDeviceUnlock() {
        fragmentEventBinding.checkBoxDeviceUnlock.setChecked(preferenceEvent.getEventDeviceUnlock());
    }

    private void createListenerDeviceUnlock() {
        final CheckBox checkBoxDeviceUnlock = fragmentEventBinding.checkBoxDeviceUnlock;
        checkBoxDeviceUnlock.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferenceEvent.setEventDeviceUnlock(isChecked));
    }

    private void createListenerDaily() {
        final CheckBox checkBoxDailyAt = fragmentEventBinding.checkBoxDailyAt;
        checkBoxDailyAt.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceEvent.setEventDaily(isChecked);

            final TimePicker timePicker = fragmentEventBinding.timePickerDailyAt;

            timePicker.setEnabled(false);
            if (isChecked) {
                timePicker.setEnabled(true);
            }
        });
    }

    private void createListenerDailyTime() {
        final TimePicker timePicker = fragmentEventBinding.timePickerDailyAt;
        timePicker.setOnTimeChangedListener((view1, hourOfDay, minute)
                        -> {
                    preferenceEvent.setEventDailyTimeHour(timePicker.getHour());
                    preferenceEvent.setEventDailyTimeMinute(timePicker.getMinute());
                }

        );
    }

    protected void setDailyTime() {
        final TimePicker timePicker = fragmentEventBinding.timePickerDailyAt;

        final int hourOfDay = preferenceEvent.getEventDailyTimeHour();
        if (hourOfDay == -1) {
            preferenceEvent.setEventDailyTimeHour(6);
            timePicker.setHour(6);
        }

        final int minute = preferenceEvent.getEventDailyTimeMinute();
        if (minute == -1) {
            preferenceEvent.setEventDailyTimeMinute(0);
            timePicker.setMinute(0);
        }

        timePicker.setHour(hourOfDay);
        timePicker.setMinute(minute);
        timePicker.setIs24HourView(false);
    }
}
