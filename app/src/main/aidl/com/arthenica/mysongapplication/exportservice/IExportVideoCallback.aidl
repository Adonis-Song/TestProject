// IExportVideoCallback.aidl
package com.arthenica.mysongapplication.exportservice;

// Declare any non-default types here with import statements

interface IExportVideoCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void setCurrentExportTime(int time);
    void setExportResult(boolean result);
}
