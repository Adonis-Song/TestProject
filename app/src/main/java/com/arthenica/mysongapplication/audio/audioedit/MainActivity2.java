package com.arthenica.mysongapplication.audio.audioedit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.arthenica.mysongapplication.R;

public class MainActivity2 extends AppCompatActivity {

  // Used to load the 'native-lib' library on application startup.
  static {
    System.loadLibrary("native-lib");
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  }

}
