package com.example.brb_lab.read_loc;

import android.app.Activity;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MainActivity extends Activity
{
    ArrayList<SensorData> mData = new ArrayList<>();
    ArrayList<RawData>mRaw = new ArrayList<>();
    EditText editText1;
    EditText editText2;
    float absMax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);

        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String filename = editText1.getText().toString();
                String contents = editText2.getText().toString();

                filename = "/sdcard/" + filename;

                writeToFile(filename, contents);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                String filename = editText1.getText().toString();

                filename = "/sdcard/" + filename;

                String contents = readFromFile(filename);
                if (contents != null)
                {
                    editText2.setText(contents);
                } else
                {
                    System.out.println("File contents is null.");
                }
                SensorToRaw(mData,mRaw);
                RawToString(mRaw);
            }

        });
    }

    public void writeToFile(String filename,String contents)
    {
        File file = new File("/sdcard/raw.txt");
        try
        {
            FileOutputStream outstream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outstream);
            writer.write(rawString);

            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();

            Toast.makeText(this, "파일에 쓰기 실패", Toast.LENGTH_LONG);
        }
    }

    public String readFromFile(String filename)
    {
        File file = new File("/sdcard/20150323_175940SensorData.txt");

        String output = null;
        try
        {
            FileInputStream instream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

            StringBuffer StrBuf = new StringBuffer();
            String aLine = "";
            SensorData sData = new SensorData();


            aLine = reader.readLine();
            if(aLine != null)
            {
                sData.mTime = Integer.parseInt(aLine.substring(1));
            }
            while(aLine != null)
            {
                aLine = reader.readLine();
                if (aLine != null)
                {
                    if(aLine.contains("A"))
                    {
                        float accel = Math.abs(Float.parseFloat(aLine.substring(1)));
                        if(absMax < accel)
                        {
                            absMax = accel;
                        }
                        sData.mAcc[sData.lengAcc++]= accel;

                    }
                    else if(aLine.contains("R"))
                    {
                        sData.mRot[sData.lengRot++]= Float.parseFloat(aLine.substring(1));
                    }
                    else if(aLine.contains("M"))
                    {
                        sData.mMag[sData.lengMag++]= Float.parseFloat(aLine.substring(1));
                    }
                    else if(aLine.contains("T"))
                    {
                        mData.add(sData);
                        sData = new SensorData();
                        sData.mTime = Integer.parseInt(aLine.substring(1));
                    }
                    StrBuf.append(aLine + "\n");
                }
            }
            mData.add(sData);

            output = StrBuf.toString();

            reader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();

            Toast.makeText(this, "파일에서 읽기 실패",Toast.LENGTH_LONG);
        }

        return output;
    }

    void SensorToRaw(ArrayList<SensorData> data, ArrayList<RawData> raw)
    {
        RawData rawData = new RawData();
        for(int i = 0; i < data.size(); i++)
        {
            rawData = new RawData();
            rawData.time = data.get(i).mTime;
            rawData.mLoc[0] = data.get(i).mAcc[0]/absMax;
            rawData.mLoc[1] = data.get(i).mAcc[1]/absMax;
            rawData.mLoc[2] = data.get(i).mAcc[2]/absMax;
            raw.add(rawData);
        }
    }

    String rawString = new String();
    String iString = new String();
    void RawToString(ArrayList<RawData> raw)
    {
        for(int i = 0; i < raw.size(); i++) {
            iString = "T" + String.valueOf(raw.get(i).time).toString() + "\n" + "L" + String.valueOf(raw.get(i).mLoc[0]).toString() + "/" +  String.valueOf(raw.get(i).mLoc[1]).toString() + "/" + String.valueOf(raw.get(i).mLoc[2]).toString() + "\n\n";
            rawString += iString;
        }
    }
}
