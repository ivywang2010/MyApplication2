package androidlabs.example.com.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
   //  private String windSpeed;
     public final String activity = "activity_weather_forecast";
     private String minTemp;
     private String maxTemp;
     private String currentTemp;
     private Bitmap currentWeatherIcon;
     private double uvrating;
     private ProgressBar progressBar;
     private TextView current_temp;
     private TextView min_temp;
     private TextView max_temp;
     private TextView uvRating;
     private ImageView weatherImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        ForecastQuery NetworkThread = new ForecastQuery();
        NetworkThread.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
//        NetworkThread.execute();

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);  //show the progress bar
        weatherImage = (ImageView)findViewById(R.id.weatherImage);
        uvRating =(TextView)findViewById(R.id.UVrating);
        max_temp = (TextView)findViewById(R.id.maximumTemp);
        min_temp =(TextView)findViewById(R.id.minimumTemp);
        current_temp = (TextView)findViewById(R.id.currentTemperature);
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{
        // wind speed, min, max, and current temperature. There should also be a Bitmap variable to store the picture for the current weather.

        @Override
        protected String doInBackground(String ... params) {


            try {


                //get the string url:
                String myUrl = params[0];
                // create the network connection:
              //  URL url = new URL(myUrl);
                URL url = new URL(myUrl);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inStream = urlConnection.getInputStream();


                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");


                //now loop over the XML:
                while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                {

                    if(xpp.getEventType() == XmlPullParser.START_TAG)
                    {

                        String tagName = xpp.getName(); //get the name of the starting tag: <tagName>

                        if(tagName.equals("temperature"))
                        {

                            currentTemp = xpp.getAttributeValue(null, "value");
                            Log.e("AsyncTask", "Found current temperature: "+ currentTemp);
                            publishProgress(25); //tell android to call onProgressUpdate with 25 as parameter
                            minTemp = xpp.getAttributeValue(null, "min");
                            Log.e("AsyncTask", "Found minimum temperature value: "+ minTemp);
                            publishProgress(50); //tell android to call onProgressUpdate with 50 as parameter
                            maxTemp = xpp.getAttributeValue(null, "max");
                            Log.e("AsyncTask", "Found max temperature: "+ maxTemp);
                            publishProgress(75); //tell android to call onProgressUpdate with 75 as parameter

                        }


                        else if(tagName.equals("weather"))
                        {
                            String icon = xpp.getAttributeValue(null,"icon");
                            Log.e("AsyncTask", "Found currentweather image: "+ icon);

                            if(!fileExistance(icon)){
                            //download icon
                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + icon+ ".png");
                            HttpURLConnection connection = (HttpURLConnection) iconUrl.openConnection();
                            connection.connect();
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200) {
                                currentWeatherIcon = BitmapFactory.decodeStream(connection.getInputStream());
                            }
                            //save icon to a file
                            FileOutputStream outputStream = openFileOutput( icon + ".png", Context.MODE_PRIVATE);
                            currentWeatherIcon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);

                            outputStream.flush();
                            outputStream.close();
                                System.out.println(6);
                            }else{
                                FileInputStream fis = null;
                                try {    fis = openFileInput(icon+".png");   }
                                catch (FileNotFoundException e) {    e.printStackTrace();  }
                                currentWeatherIcon = BitmapFactory.decodeStream(fis);
                                Log.i("weather_forecast","Find the weather image.");
                            System.out.println(7);
                            }
                            publishProgress(100); //tell android to call onProgressUpdate with 100 as parameter
                        }
                    }
                    xpp.next(); //advance to next XML event
                }
                //End of XML reading

                //Start of JSON reading of UV factor:

                //create the network connection:
                URL UVurl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
                uvrating = jObject.getDouble("value");
                Log.i("UV is:", ""+ uvrating);

                //END of UV rating

                Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin

            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

            //return type 3, which is String:
            return "Finished task";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
          //  Log.i(activity, "update:" + values[0]);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            //set image bitmap here:
            weatherImage.setImageBitmap(currentWeatherIcon);
            current_temp.setText("current temperature is: "+ currentTemp);
            min_temp.setText("minimum temperature is:"+ minTemp);
            max_temp.setText("maximum temperature is:"+ maxTemp);
            uvRating.setText("uvrating is:"+ uvrating);
            progressBar.setVisibility(View.INVISIBLE);
        }

        //method to check if image is already downloaded
        public boolean fileExistance(String fname){
               File file = getBaseContext().getFileStreamPath(fname);
                return file.exists();   }

      }
}
