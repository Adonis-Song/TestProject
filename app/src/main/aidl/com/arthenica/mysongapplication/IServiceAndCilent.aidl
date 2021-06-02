// IServiceAndCilent.aidl
package com.arthenica.mysongapplication;

// Declare any non-default types here with import statements

interface IServiceAndCilent {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    /*void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);*/

      void getString(in String string);
      String returnString();
}
