package com.github.jameshnsears.quoteunquote.configure.fragment.appearance;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.jameshnsears.quoteunquote.R;
import com.github.jameshnsears.quoteunquote.configure.fragment.FragmentCommon;
import com.github.jameshnsears.quoteunquote.databinding.FragmentAppearanceBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentAppearance extends FragmentCommon {
    @Nullable
    public FragmentAppearanceBinding fragmentAppearanceBinding;
    @Nullable
    public PreferenceAppearance preferenceAppearance;

    protected FragmentAppearance(final int widgetId) {
        super(widgetId);
    }

    @NonNull
    public static FragmentAppearance newInstance(final int widgetId) {
        final FragmentAppearance fragment = new FragmentAppearance(widgetId);
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    @NonNull
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        preferenceAppearance = new PreferenceAppearance(this.widgetId, this.getContext());

        fragmentAppearanceBinding = FragmentAppearanceBinding.inflate(getLayoutInflater());
        return fragmentAppearanceBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAppearanceBinding = null;
    }

    @Override
    public void onViewCreated(
            @NonNull final View view, final Bundle savedInstanceState) {
        createListenerTransparency();
        createListenerTextColour();
        createListenerTextSize();
        createListenerToolbarFirst();
        createListenerToolbarPrevious();
        createListenerToolbarReport();
        createListenerToolbarToggleFavourite();
        createListenerToolbarShare();
        createListenerToolbarNextRandom();
        createListenerToolbarNextSequential();

        setTransparency();
        setTextColour();
        setTextSize();
        setToolbar();
    }

    private void setToolbar() {
        fragmentAppearanceBinding.toolbarSwitchFirst.setChecked(preferenceAppearance.getAppearanceToolbarFirst());
        fragmentAppearanceBinding.toolbarSwitchPrevious.setChecked(preferenceAppearance.getAppearanceToolbarPrevious());
        fragmentAppearanceBinding.toolbarSwitchReport.setChecked(preferenceAppearance.getAppearanceToolbarReport());
        fragmentAppearanceBinding.toolbarSwitchToggleFavourite.setChecked(preferenceAppearance.getAppearanceToolbarFavourite());
        fragmentAppearanceBinding.toolbarSwitchShare.setChecked(preferenceAppearance.getAppearanceToolbarShare());
        fragmentAppearanceBinding.toolbarSwitchNextRandom.setChecked(preferenceAppearance.getAppearanceToolbarRandom());
        fragmentAppearanceBinding.toolbarSwitchNextSequential.setChecked(preferenceAppearance.getAppearanceToolbarSequential());
    }

    private void createListenerToolbarFirst() {
        fragmentAppearanceBinding.toolbarSwitchFirst.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceAppearance.setAppearanceToolbarFirst(isChecked);
        });
    }

    private void createListenerToolbarPrevious() {
        fragmentAppearanceBinding.toolbarSwitchPrevious.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceAppearance.setAppearanceToolbarPrevious(isChecked);
        });
    }

    private void createListenerToolbarReport() {
        fragmentAppearanceBinding.toolbarSwitchReport.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceAppearance.setAppearanceToolbarReport(isChecked);
        });
    }

    private void createListenerToolbarToggleFavourite() {
        fragmentAppearanceBinding.toolbarSwitchToggleFavourite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceAppearance.setAppearanceToolbarFavourite(isChecked);
        });
    }

    private void createListenerToolbarShare() {
        fragmentAppearanceBinding.toolbarSwitchShare.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceAppearance.setAppearanceToolbarShare(isChecked);
        });
    }

    private void createListenerToolbarNextRandom() {
        fragmentAppearanceBinding.toolbarSwitchNextRandom.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceAppearance.setAppearanceToolbarRandom(isChecked);
        });
    }

    private void createListenerToolbarNextSequential() {
        fragmentAppearanceBinding.toolbarSwitchNextSequential.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceAppearance.setAppearanceToolbarSequential(isChecked);
        });
    }

    private void createListenerTransparency() {
        fragmentAppearanceBinding.seekBarTransparency.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
                        if (preferenceAppearance.getAppearanceTransparency() != progress) {
                            preferenceAppearance.setAppearanceTransparency(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(final SeekBar seekBar) {
                        // do nothing
                    }

                    @Override
                    public void onStopTrackingTouch(final SeekBar seekBar) {
                        // do nothing
                    }
                });
    }

    private void createListenerTextColour() {
        final Spinner spinner = fragmentAppearanceBinding.spinnerColour;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long selectedItemId) {
                String selectedItem = spinner.getSelectedItem().toString();
                if (!preferenceAppearance.getAppearanceTextColour().equals(selectedItem)) {
                    preferenceAppearance.setAppearanceTextColour(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    private void createListenerTextSize() {
        final Spinner spinner = fragmentAppearanceBinding.spinnerSize;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long selectedItemId) {
                int selectedItem = Integer.parseInt(spinner.getSelectedItem().toString());
                if (preferenceAppearance.getAppearanceTextSize() != selectedItem) {
                    preferenceAppearance.setAppearanceTextSize(selectedItem);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    protected void setTextSize() {
        final int[] sizeArray = getResources().getIntArray(R.array.fragment_appearance_size_array);
        final List<Integer> sizeIntegerArray = getTextSizeIntegerArray(sizeArray);

        final ArrayAdapter<Integer> spinnerArrayAdapter =
                new ArrayAdapter<Integer>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        sizeIntegerArray) {

                    @Override
                    public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {
                        return getTextSizeDefault(
                                position,
                                super.getView(position, convertView, parent),
                                sizeIntegerArray);
                    }

                    @Override
                    public View getDropDownView(final int position, final View convertView, @NonNull final ViewGroup parent) {
                        return getTextSizeDefault(
                                position,
                                super.getDropDownView(position, convertView, parent),
                                sizeIntegerArray);
                    }
                };

        fragmentAppearanceBinding.spinnerSize.setAdapter(spinnerArrayAdapter);
        setTextSizePreference(sizeIntegerArray, fragmentAppearanceBinding.spinnerSize);
    }

    private void setTextSizePreference(
            @NonNull final List<Integer> sizeIntegerArray,
            @NonNull final Spinner spinnerSize) {
        final int testSizePreference = this.preferenceAppearance.getAppearanceTextSize();
        if (testSizePreference == -1) {
            spinnerSize.setSelection(2);
        } else {
            int selectionIndex = 0;
            for (final Integer sizeInteger : sizeIntegerArray) {
                if (testSizePreference == sizeInteger) {
                    spinnerSize.setSelection(selectionIndex);
                    break;
                }
                selectionIndex++;
            }
        }
    }

    @NonNull
    public View getTextSizeDefault(
            final int position,
            @NonNull final View view,
            @NonNull final List<Integer> sizeIntegerArray) {
        final TextView textView = (TextView) view;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeIntegerArray.get(position).floatValue());
        return view;
    }

    @NonNull
    private List<Integer> getTextSizeIntegerArray(final int... sizeArray) {
        final List<Integer> sizeIntegerArray = new ArrayList<>();
        for (final int size : sizeArray) {
            final Integer integer = size;
            sizeIntegerArray.add(integer);
        }
        return sizeIntegerArray;
    }

    protected void setTransparency() {
        final int transparency = preferenceAppearance.getAppearanceTransparency();

        if (transparency == -1) {
            fragmentAppearanceBinding.seekBarTransparency.setProgress(2);
        } else {
            fragmentAppearanceBinding.seekBarTransparency.setProgress(transparency);
        }
    }

    protected void setTextColour() {
        final Spinner spinnerColour = fragmentAppearanceBinding.spinnerColour;
        spinnerColour.setAdapter(new ColourSpinnerAdapter(getActivity().getBaseContext()));

        final String spinnerColourPreference = this.preferenceAppearance.getAppearanceTextColour();
        if ("".equals(spinnerColourPreference)) {
            spinnerColour.setSelection(0);
        } else {
            int selectionIndex = 0;
            for (final String colour : getActivity().getBaseContext().getResources().getStringArray(R.array.fragment_appearance_colour_array)) {
                if (colour.equals(spinnerColourPreference)) {
                    spinnerColour.setSelection(selectionIndex);
                    break;
                }
                selectionIndex++;
            }
        }
    }
}
