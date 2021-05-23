package com.spartano.tiendamovil.ui.nuevaPublicacion;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.spartano.tiendamovil.R;

import java.util.Map;

public class SpinnerKVPAdapter implements SpinnerAdapter {
    private final Context context;
    private final Map<Integer, String> map;
    private final LayoutInflater inflater;

    public SpinnerKVPAdapter(@NonNull Context context, @NonNull Map<Integer, String> kvp, LayoutInflater layoutInflater) {
        super();
        this.context = context;
        this.map = kvp;
        this.inflater = layoutInflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = convertView != null ? convertView : inflater.inflate(R.layout.kvp_spinner_item, parent, false);
        String value = map.get(position);

        TextView tvValue = convertView.findViewById(R.id.tvValue);
        tvValue.setText(value);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return map.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }
}
