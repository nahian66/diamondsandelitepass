package com.rowdystudio.fffdiamondsandelitespass.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rowdystudio.fffdiamondsandelitespass.R;
import com.rowdystudio.fffdiamondsandelitespass.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link selectLaungaugeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class selectLaungaugeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    private RadioGroup language_group;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public selectLaungaugeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment selectLaungaugeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static selectLaungaugeFragment newInstance() {
        selectLaungaugeFragment fragment = new selectLaungaugeFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_select_laungauge, container, false);

        language_group = view.findViewById(R.id.radio_group_language);
        view.findViewById(R.id.select_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInit();
            }
        });
        return view;
    }

    private void onInit() {
        int selectId = language_group.getCheckedRadioButtonId();

        if (selectId == R.id.english_button) {
            Constant.setString(context, Constant.LANGUAGE, "en");
            Constant.setLanguage(context, Constant.getString(context, Constant.LANGUAGE));
            if (getActivity() == null) {
                return;
            }
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_Login, LoginFragment.newInstance()).addToBackStack(null).commit();
        } else if (selectId == R.id.hindi_button) {
            Constant.setString(context, Constant.LANGUAGE, "hi");
            Constant.setLanguage(context, Constant.getString(context, Constant.LANGUAGE));
            if (getActivity() == null) {
                return;
            }
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_Login, LoginFragment.newInstance()).addToBackStack(null).commit();
        }

    }
}