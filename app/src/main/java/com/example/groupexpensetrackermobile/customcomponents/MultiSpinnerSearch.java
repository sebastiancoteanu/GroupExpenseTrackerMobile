package com.example.groupexpensetrackermobile.customcomponents;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupexpensetrackermobile.R;
import com.example.groupexpensetrackermobile.adapters.AddUsersAdapter;
import com.example.groupexpensetrackermobile.entities.SelectableUser;
import com.example.groupexpensetrackermobile.listeners.MultiSpinnerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;

public class MultiSpinnerSearch extends AppCompatSpinner implements DialogInterface.OnCancelListener {

    public static AlertDialog.Builder builder;
    public static AlertDialog ad;

    private boolean highlightSelected = false;
    private int highlightColor = Color.rgb(179, 229, 252);
    private int textColor = Color.GRAY;
    private int selected = 0;
    private String defaultText = "";
    private String spinnerTitle = "Add users";
    private String searchHint = "Type to search";
    private String clearText = "Clear All";

    private MultiSpinnerListener listener;

    private AddUsersAdapter adapter;
    private List<SelectableUser> items;

    public MultiSpinnerSearch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MultiSpinnerSearch(Context context) {
        super(context);
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
    }

    public void setSpinnerTitle(String spinnerTitle) {
        this.spinnerTitle = spinnerTitle;
    }

    public void setSpinnerText(String spinnerText) {
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), R.layout.text_view_for_spinner, new String[]{ spinnerText });
        setAdapter(adapterSpinner);
    }

    public List<SelectableUser> getSelectedItems() {
        List<SelectableUser> selectedItems = new ArrayList<>();
        for (SelectableUser item : items) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public List<Long> getSelectedIds() {
        List<Long> selectedItemsIds = new ArrayList<>();
        for (SelectableUser item : items) {
            if (item.isSelected()) {
                selectedItemsIds.add(item.getId());
            }
        }
        return selectedItemsIds;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

        /**
         * To hide dropdown which is already opened at the time of performClick...
         * This code will hide automatically and no need to tap by user.
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }).start();
    }

    @Override
    public boolean performClick() {

        super.performClick();
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle(spinnerTitle);

        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.alert_dialog_list_view_search, null);
        builder.setView(view);

        final RecyclerView recyclerView = view.findViewById(R.id.alertSearchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AddUsersAdapter(items, getContext());
        recyclerView.setAdapter(adapter);

        final EditText editText = view.findViewById(R.id.alertSearchEditText);
        editText.setVisibility(VISIBLE);
        editText.setHint(searchHint);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(adapter.getCurrentUserList() != null && adapter.getCurrentUserList().size() != 0) {
                    TextView tv = view.findViewById(R.id.empty);
                    if(tv != null) {
                        tv.setVisibility(INVISIBLE);
                    }
                } else {
                    TextView tv = view.findViewById(R.id.empty);
                    if(tv != null) {
                        tv.setVisibility(VISIBLE);
                    }
                }
            }
        });


        /**
         * For selected items
         */
        selected = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected())
                selected++;
        }

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            //Log.i(TAG, " ITEMS : " + items.size());
            listener.onSelectionEnd();
            dialog.cancel();
        });


        builder.setOnCancelListener(this);
        ad = builder.show();
        Button positiveButton = ad.getButton(DialogInterface.BUTTON_POSITIVE);

        positiveButton.setBackgroundColor(Color.WHITE);
        positiveButton.setTextColor(Color.rgb(1, 87, 155));
        positiveButton.setGravity(Gravity.END);

        Objects.requireNonNull(ad.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return true;
    }

    public void setItems(List<SelectableUser> items, MultiSpinnerListener listener) {
        this.items = items;
        this.listener = listener;
    }

}
