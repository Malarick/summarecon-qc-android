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
    Preference checkbox_1,edt_server_ip,edt_client_ip;
    @Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        //return rootView;
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        //checkbox_1 = (Preference) findPreference("checkbox_preference");
        edt_server_ip = (Preference) findPreference("server_ip_preference");
        edt_client_ip = (Preference) findPreference("client_ip_preference");
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
        edt_server_ip.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object new_ip_server) {
                String text2= (String) new_ip_server;
                edt_server_ip.setSummary(text2);
                //edittext_1.setSummary(mySharedPreferences.getString("edittext_preference","127.0.0.0"));
                return true;
            }
        });

        edt_client_ip.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object new_ip_client) {
                String text2= (String) new_ip_client;
                edt_client_ip.setSummary(text2);
                //edittext_1.setSummary(mySharedPreferences.getString("edittext_preference","127.0.0.0"));
                return true;
            }
        });
    }

    private void loadPref(){
        Log.e("APF : ", " loadPref()");
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //boolean my_checkbox_preference = mySharedPreferences.getBoolean("checkbox_preference", false);
        String server_ip = mySharedPreferences.getString("server_ip_preference", "");
        String client_ip = mySharedPreferences.getString("client_ip_preference", "");
/*
        if (my_checkbox_preference){
            checkbox_1.setSummary("Enable");
        }else
        {
            checkbox_1.setSummary("Disable");
        }
*/

        if (server_ip != "")
        {
            edt_server_ip.setSummary(mySharedPreferences.getString("server_ip_preference",""));
            Toast.makeText(getActivity().getApplicationContext(),server_ip,Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(getActivity().getApplicationContext(),"isian kosong!!",Toast.LENGTH_SHORT).show();
        }

        if (client_ip != "")
        {
            edt_client_ip.setSummary(mySharedPreferences.getString("client_ip_preference",""));
            Toast.makeText(getActivity().getApplicationContext(),client_ip,Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(getActivity().getApplicationContext(),"isian kosong!!",Toast.LENGTH_SHORT).show();
        }
        //prefEditText.setText(my_edittext_preference);

    }
}