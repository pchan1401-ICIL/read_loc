package com.example.brb_lab.read_loc;

/**
 * Created by BRB_LAB on 2015-03-19.
 */
public class SensorData
{
    int mTime;
    float[] mAcc;
    float[] mRot;
    float[] mMag;
    int lengAcc;
    int lengRot;
    int lengMag;

    SensorData()
    {
        mTime = 0;
        mAcc = new float[3];
        mRot = new float[3];
        mMag = new float[3];
        lengAcc = 0;
        lengRot = 0;
        lengMag = 0;
    }

}
