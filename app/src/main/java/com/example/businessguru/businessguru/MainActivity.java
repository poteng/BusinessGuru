package com.example.businessguru.businessguru;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    static boolean bulb1 = false;
    static boolean lock1 = false;
    static boolean ac1 = false;

    private ViewPager mViewPager;
    private ImageView imageBulb;
    private ImageView imageLock;
    private ImageView imageAC;
    private TextView textViewGPS1;
    private TextView textViewGPS2;
    private TextView textViewGPS3;

    //GPS receiver -Boden
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Set up the ImageView of bulb image. -Boden
        imageBulb = (ImageView) findViewById(R.id.imageViewBulb1);
        imageLock = (ImageView) findViewById(R.id.imageViewLock1);
        imageAC = (ImageView) findViewById(R.id.imageViewAC1);
        textViewGPS1 = (TextView) findViewById(R.id.textViewGPS1);
        textViewGPS2 = (TextView) findViewById(R.id.textViewGPS2);
        textViewGPS3 = (TextView) findViewById(R.id.textViewGPS3);



        // Check GPS permission. -Boden
        if(!runtime_permissions()) {
            startGPS();
        }



        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

    }


    private void startGPS() {
        // Call GPS service
        Intent i =new Intent(getApplicationContext(), GPS_Service.class);
        startService(i);
    }

    // Check GPS permission. -Boden
    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                  //  textViewGPS
                    imageBulb = (ImageView) findViewById(R.id.imageViewBulb1);
                    imageLock = (ImageView) findViewById(R.id.imageViewLock1);
                    imageAC = (ImageView) findViewById(R.id.imageViewAC1);

                    textViewGPS1 = (TextView) findViewById(R.id.textViewGPS1);
                    textViewGPS2 = (TextView) findViewById(R.id.textViewGPS2);
                    textViewGPS3 = (TextView) findViewById(R.id.textViewGPS3);

                    try {
                        if ( intent.getExtras().get("light_signal").equals("On") ) {
                            if (bulb1){
                                textViewGPS1.setText("Status:\nOn");
                                imageBulb.setImageResource(R.mipmap.lighton);
                            } else {
                                textViewGPS1.setText("Status:\nOff");
                                imageBulb.setImageResource(R.mipmap.lightoff);
                            }
                            if (lock1){
                                textViewGPS2.setText("Status:\nOn");
                                imageLock.setImageResource(R.mipmap.lockon);
                            }else {
                                textViewGPS2.setText("Status:\nOff");
                                imageLock.setImageResource(R.mipmap.lockoff);
                            }
                            if (ac1){
                                textViewGPS3.setText("Status:\nOn");
                                imageAC.setImageResource(R.mipmap.acon);
                            }else {
                                textViewGPS3.setText("Status:\nOff");
                                imageAC.setImageResource(R.mipmap.acoff);
                            }
                        } else if ( intent.getExtras().get("light_signal").equals("Off") ) {
                            if (bulb1){
                                textViewGPS1.setText("Status:\nOff");
                                imageBulb.setImageResource(R.mipmap.lightoff);
                            }
                            if (lock1){
                                textViewGPS2.setText("Status:\nOff");
                                imageLock.setImageResource(R.mipmap.lockoff);
                            }
                            if (ac1){
                                textViewGPS3.setText("Status:\nOff");
                                imageAC.setImageResource(R.mipmap.acoff);
                            }
                        } else {
                            //imageBulb.setImageResource(R.mipmap.ic_launcher);
                        }
                    } catch (RuntimeException e) {
                        Log.e("onResume", e.getMessage());
                    }



                    //textViewGPS1.setText("Status:\nOn");

                    //textViewGPS.append("\n" +intent.getExtras().get("light_signal"));
                    //textView.append("\n" +intent.getExtras().get("coordinates"));
                    // scrollView1.scrollTo(0, scrollView1.getBottom());
                    //textView2.append("\n" +intent.getExtras().get("light_signal"));
                    // scrollView2.scrollTo(0, scrollView2.getBottom());
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        //   registerReceiver(broadcastReceiver,new IntentFilter("light_switch"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                //enable_buttons();
                startGPS();
            }else {
                runtime_permissions();
            }
        }
    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Status Fragment
     */
    public static class StatusFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public StatusFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static StatusFragment newInstance(int sectionNumber) {
            StatusFragment fragment = new StatusFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_status, container, false);

         //   TextView textView = (TextView) rootView.findViewById(R.id.section_label);
          //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)))
            Switch toggle1 = (Switch) rootView.findViewById(R.id.connectSwitch1);
            Switch toggle2 = (Switch) rootView.findViewById(R.id.connectSwitch2);
            Switch toggle3 = (Switch) rootView.findViewById(R.id.connectSwitch3);
            final TextView textViewConnect1 = (TextView) rootView.findViewById(R.id.textViewConnect1);
            final TextView textViewConnect2 = (TextView) rootView.findViewById(R.id.textViewConnect2);
            final TextView textViewConnect3 = (TextView) rootView.findViewById(R.id.textViewConnect3);

            //ToggleButton toggle = (ToggleButton) findViewById(R.id.connectSwitch);
            toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled
                        textViewConnect1.setText("Device 1 is connected.");
                        bulb1 = true;
                    } else {
                        // The toggle is disabled
                        textViewConnect1.setText("Device 1 is not connected.");
                        bulb1 = false;
                    }
                }
            });

            toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled
                        textViewConnect2.setText("Device 2 is connected.");
                        lock1 = true;
                    } else {
                        // The toggle is disabled
                        textViewConnect2.setText("Device 2 is not connected.");
                        lock1 = false;
                    }
                }
            });

            toggle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled
                        textViewConnect3.setText("Device 3 is connected.");
                        ac1 = true;
                    } else {
                        // The toggle is disabled
                        textViewConnect3.setText("Device 3 is not connected.");
                        ac1 = false;
                    }
                }
            });



            return rootView;
        }

        //Switch connectSwitch = (Switch) findViewById(R.id.connectSwitch);
    }


    /**
     *  Devices Fragment
     */
    public static class DevicesFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public DevicesFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DevicesFragment newInstance(int sectionNumber) {
            DevicesFragment fragment = new DevicesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_devices, container, false);

            //   TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)))
            return rootView;
        }
    }
    /**
     *  Second Tab Fragment
     */
    public static class SettingsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public SettingsFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SettingsFragment newInstance(int sectionNumber) {
            SettingsFragment fragment = new SettingsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

            //   TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)))
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
           // return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return DevicesFragment.newInstance(position + 1);
                case 1:
                    return StatusFragment.newInstance(position + 1);
                case 2:
                    return SettingsFragment.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab1);
                case 1:
                    return getString(R.string.tab2);
                case 2:
                    return getString(R.string.tab3);
            }
            return null;
        }
    }
}
