package com.example.knowyourfood;
import java.util.ArrayList;
import java.util.HashMap;

public interface OnDataReceiveCallback {
    void onDataReceived(ArrayList<HashMap<String, Object>> data);

}
