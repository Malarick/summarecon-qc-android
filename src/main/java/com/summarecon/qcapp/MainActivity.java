package com.summarecon.qcapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends Activity {
    String username;
    String password;
    TextView lbl_user;
    Bundle bundle = new Bundle();
    String response;
    GetDataFromServer getdata = new GetDataFromServer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundle = getIntent().getBundleExtra("bundle");
        username = bundle.getString("username");
        password = bundle.getString("password");

        lbl_user = (TextView) findViewById(R.id.lbl_username);
        getdata.execute();
    }

    class GetDataFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Request ke Server
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://192.168.100.127/login/list.php");
            //HttpResponse response = null;


            try {
                //response = client.execute(request);
                //text = EntityUtils.toString(response.getEntity());

                // Add Multipart Post Data
                MultipartEntity entity = new MultipartEntity();
                entity.addPart("username", new StringBody(username));
                entity.addPart("password", new StringBody(password));
                request.setEntity(entity);

                // Get response from server
                HttpResponse httpResponse = client.execute(request);
                response = EntityUtils.toString(httpResponse.getEntity());

                // Parsing JSON
                //contact = new ArrayList<Contact>();
                JSONArray array = new JSONArray(response);
                Log.e("jason", "text: " + response);
                // for (int i=0; i<=array.length(); i++) {
                JSONObject obj = (JSONObject) array.get(0);
                username = obj.getString("username");
                password = obj.getString("password");
/*
                        Contact c = new Contact();
                        c.setId(obj.getInt("id"));
                        c.setName(obj.getString("name"));
                        c.setDob(obj.getString("dob"));
                        c.setPhoneNumber(obj.getString("phone_number"));
                        c.setPhotoURL("http://192.168.100.143/contactapp-webapi/" + obj.getString("photo_url"));


                        URI uri = new URI(c.getPhotoURL().replace(" ", "%20"));
                        Bitmap photoBitmap = downloadBitmap(uri.toString());

                        // Convert Bitmap to Byte
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] final_data = stream.toByteArray();

                        // Save Image Byte to File
                        File photo_directory = new File(Environment.getExternalStorageDirectory() + "/DCIM/CameraAPI");

                        // Create Folder if not exist
                        if (!photo_directory.exists()) {
                            photo_directory.mkdirs();
                        }

                        String filename = System.currentTimeMillis() + ".jpg";
                        File picture_file = new File(photo_directory, filename);
                        FileOutputStream fos;
                        try {
                            fos = new FileOutputStream(picture_file);
                            fos.write(final_data);
                            fos.flush();
                            fos.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        c.setPhotoURL(picture_file.getAbsolutePath());

                        // Debug untuk mendapatkan url photo
                            //String url = "http://192.168.100.113/contactapp-webapi/" + obj.getString("photo_url");
                            //Log.d("test_photo","photo_url : " + url);
                        contact.add(c);
*/
                // }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //catch (URISyntaxException e)
            //{
            //   e.printStackTrace();
            //}

            return null;
        }

/*
            private Bitmap downloadBitmap(String url) {
                Bitmap image = null;
                final DefaultHttpClient client = new DefaultHttpClient();
                final HttpGet getRequest = new HttpGet(url);
                try {
                    HttpResponse response = client.execute(getRequest);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode != HttpStatus.SC_OK) {
                        return null;
                    }

                    final HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream inputStream = null;
                        try {
                            inputStream = entity.getContent();
                            image = BitmapFactory.decodeStream(inputStream);
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            entity.consumeContent();
                        }
                    }
                } catch (Exception e) {
                    getRequest.abort();
                    Log.e("ImageDownloader", "Error : " + url + e.toString());
                }
                return image;
            }

            @Override
            public void onPostExecute(Void return_value){
                adapter = new  ContactListAdapter(MainActivity.this, R.layout.contact_list_item, contact);
                output.setAdapter(adapter);
            }
            */

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "username: " + username + " " + "Password: " + password, Toast.LENGTH_SHORT).show();
            lbl_user.setText("Welcome, "+username);
        }
    }

}
