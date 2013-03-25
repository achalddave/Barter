package edu.berkeley.cs160.achaldave.prog3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class InteractiveMapFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.map_view, container, false);
    }
    
    public void setMap(int mapId) {
    	ImageView map = (ImageView) this.getActivity().findViewById(R.id.map);
    	map.setImageResource(mapId);
    }
    
    public void setRouteName(String name) {
    	TextView routeName = (TextView) getActivity().findViewById(R.id.routeName);
    	routeName.setText(name);
    }
}