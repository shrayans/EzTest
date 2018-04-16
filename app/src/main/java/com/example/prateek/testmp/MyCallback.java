package com.example.prateek.testmp;

import java.util.ArrayList;

/**
 * Created by prateek on 3/15/18.
 */

public interface MyCallback {
    void onCallback(ArrayList<Object> value);

    void onCallbackString(String string);
}

