package com.chat.crypto.johnathannash.cryptothat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chat.crypto.johnathannash.cryptothat.R;
import com.chat.crypto.johnathannash.cryptothat.models.CipherSpinnerData;

import java.util.List;

public class CipherSpinnerAdapter extends ArrayAdapter<CipherSpinnerData> {

    public CipherSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<CipherSpinnerData> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(false);
        }
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(true);
        }
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    private View initialSelection(boolean dropdown) {
        TextView view = new TextView(getContext());
        view.setText(R.string.selectOne);
        view.setPadding(0, 5, 0, 5);

        if (dropdown) {
            view.setHeight(0);
        }

        return view;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.cipher_spinner_layout, parent, false);

        position = position - 1;
        CipherSpinnerData item = getItem(position);

        ((TextView)row.findViewById(R.id.cipherSpinnerLayout_CipherName)).setText(item.getCipherName());

        return row;
    }
}
