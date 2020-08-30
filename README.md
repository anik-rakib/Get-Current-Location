# Get-Current-Location

Getting current Location using Google [ Fused Location Provider Api ](https://developers.google.com/location-context/fused-location-provider).It gets the current location,Latitude,Longitude,Address,Locality etc.<br>And check Location Service are Enable or Disable. If Disable then Show a AlertDialog and then go to Settings.   

### Add This Dependencies
``````groovy
dependencies {
    implementation 'com.google.android.gms:play-services-location:17.0.0'
}
``````
### Add premissions in androidmanifest.xml file

 ``````groovy
 
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

``````


