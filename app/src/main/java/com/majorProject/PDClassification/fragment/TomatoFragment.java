package com.majorProject.PDClassification.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.majorProject.PDClassification.Classifier;
import com.majorProject.PDClassification.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TomatoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TomatoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView imgView;
    private Button select,predict,camera;
    private TextView tv;
    private static final int GALLERY_REQUEST = 2;
    private static final int CAMERA_REQUEST = 1;
    private Bitmap img;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TomatoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TomatoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TomatoFragment newInstance(String param1, String param2) {
        TomatoFragment fragment = new TomatoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tomato, container, false);

        imgView = (ImageView) view.findViewById(R.id.imageView);
        tv = (TextView) view.findViewById(R.id.textView);
        select = (Button) view.findViewById(R.id.button);
        camera = (Button) view.findViewById(R.id.button2);

        //SELECT IMGAGE FROM GALLERY
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sTomatoSendData();

            }
        });

        //STARTING CAMERA
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cTomatoSendData();

            }
        });

        return view;
    }

    public void sTomatoSendData(){

        Intent intent = new Intent(getActivity().getApplication(), Classifier.class);
        intent.putExtra("SENDER_KEY","GAL");
        intent.putExtra("MODEL","tomato");
        intent.putExtra("FROM","select");

        startActivity(intent);

    }

    public void cTomatoSendData(){

        Intent intent = new Intent(getActivity().getApplication(), Classifier.class);
        intent.putExtra("SENDER_KEY","CAM");
        intent.putExtra("MODEL","tomato");
        intent.putExtra("FROM","camera");

        startActivity(intent);

    }
}