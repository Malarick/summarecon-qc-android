package com.summarecon.qcapp.core;

import android.app.Application;

public class SummareconQCApplication extends Application {

    Configuration config;
    Session session;

    @Override
    public void onCreate() {
        super.onCreate();
        config = new Configuration();
        session = new Session();
    }

    public SummareconQCApplication getInstance() {
        return ((SummareconQCApplication) getApplicationContext());
    }

    public class Configuration {

        public Configuration() {

        }
    }

    public class Session {
        private String username;
        private String name;
        private String photoUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }
}
