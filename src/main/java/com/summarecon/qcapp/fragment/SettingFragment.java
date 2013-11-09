package com.summarecon.qcapp.fragment;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.summarecon.qcapp.R;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingFragment extends PreferenceFragment {
    Preference checkbox_1,edittext_1;
    @Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        //return rootView;
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //checkbox_1 = (Preference) findPreference("checkbox_preference");
        edittext_1 = (Preference) findPreference("edittext_preference");
        loadPref();
/*
        checkbox_1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            //boolean my_checkbox_preference = mySharedPreferences.getBoolean("checkbox_preference", false);
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean check= (Boolean) newValue;
                if (check){
                    checkbox_1.setSummary("Enable");
                }else
                {
                    checkbox_1.setSummary("Disable");
                }
                return true;
            }
        });
*/
        edittext_1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String text= (String) newValue;
                edittext_1.setSummary(text);
                //edittext_1.setSummary(mySharedPreferences.getString("edittext_preference","127.0.0.0"));
                return true;
            }
        });
    }

    private void loadPref(){
        Log.e("APF : ", " loadPref()");
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //boolean my_checkbox_preference = mySharedPreferences.getBoolean("checkbox_preference", false);
        String my_edittext_preference = mySharedPreferences.getString("edittext_preference", "");
/*
        if (my_checkbox_preference){
            checkbox_1.setSummary("Enable");
        }else
        {
            checkbox_1.setSummary("Disable");
        }
*/

        if (my_edittext_preference != "")
        {
            edittext_1.setSummary(mySharedPreferences.getString("edittext_preference",""));
            Toast.makeText(getActivity().getApplicationContext(),my_edittext_preference,Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(getActivity().getApplicationContext(),"isian kosong!!",Toast.LENGTH_SHORT).show();
        }

        //prefEditText.setText(my_edittext_preference);

    }
}