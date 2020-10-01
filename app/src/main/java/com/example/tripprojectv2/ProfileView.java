package com.example.tripprojectv2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class ProfileView extends Fragment {

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static ProfileView newInstance() {
        ProfileView fragment = new ProfileView();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.profile_edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.profile_edit_button).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.profile_gender_val_tv).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.profile_email_et).setEnabled(true);
                getActivity().findViewById(R.id.profile_view_tv_name).setEnabled(true);
                getActivity().findViewById(R.id.profile_save_button).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.profile_radio_group).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.sign_out_button).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.profile_avatar).setClickable(true);
            }
        });

        getActivity().findViewById(R.id.profile_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.profile_edit_button).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.profile_gender_val_tv).setVisibility(View.VISIBLE);

                getActivity().findViewById(R.id.profile_email_et).setEnabled(false);
                getActivity().findViewById(R.id.profile_view_tv_name).setEnabled(false);
                getActivity().findViewById(R.id.profile_save_button).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.profile_radio_group).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.profile_avatar).setClickable(false);
                
                mListener.save(); // just to go firebase and update the details and relaunch profile view
            }
        });

        getActivity().findViewById(R.id.profile_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        getActivity().findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.sign_out();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }else {
               throw new RuntimeException(context.toString()
                             + " must implement OnFragmentInteractionListener");
                }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void save();
        void sign_out();
    }


}
