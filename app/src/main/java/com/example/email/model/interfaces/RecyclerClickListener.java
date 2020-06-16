package com.example.email.model.interfaces;

import android.view.View;

public interface RecyclerClickListener {

    void OnItemClick(View view, int position);
    void OnLongItemClick(View view, int position);
    void onDeleteClick(View view, int position);
}
