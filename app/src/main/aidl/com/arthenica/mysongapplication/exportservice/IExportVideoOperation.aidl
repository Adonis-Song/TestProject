// IExportVideoOperation
package com.arthenica.mysongapplication.exportservice;

// Declare any non-default types here with import statements

import com.arthenica.mysongapplication.exportservice.LogoValues;

interface IExportVideoOperation {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void registerCallback(IBinder binder);
    void exportVideoByAss(int targetVideoWidth, int targetVideoHeight, int targetVideoQuality, String videoSrcPath, String videoDstPath, String assFilePath, in LogoValues logoValues);
    void cancel();
}
