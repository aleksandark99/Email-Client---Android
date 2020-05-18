package com.example.email.utility;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.email.R;
import com.example.email.model.items.Tag;

import java.util.ArrayList;

public class Helper {

    public static boolean DoesItContainString(ArrayList<String> s,String contains){
        for (String ss:s
        ) {
            if(ss.toLowerCase().contains(contains.toLowerCase())){
                return true;
            }

        }
        return false;
    };

    public static boolean DoesItContainTag(ArrayList<Tag> s, String contains){
        for (Tag ss:s
        ) {
            if(ss.getTagName().toLowerCase().contains(contains.toLowerCase())){
                return true;
            }

        }
        return false;
    };

}
