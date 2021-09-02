package com.example.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;



import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.journeyapps.barcodescanner.camera.CameraSettings;


public class CustomScanActivity extends AppCompatActivity {
    DecoratedBarcodeView barcodeView;
    ImageButton flipCam;
    ImageButton toggleFlash;
    boolean frontCam, flashOn;
    CaptureManager captureManager;
    ViewfinderView viewfinderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scan);
        barcodeView = findViewById(R.id.zxing_barcode_scanner);
        flipCam = findViewById(R.id.flipcamera);
        toggleFlash = findViewById(R.id.flash);
        toggleFlash.setImageResource(R.drawable.ic_baseline_flash_off_24);
        frontCam = flashOn = false;

        if (!getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            toggleFlash.setVisibility(View.GONE);
        }

        barcodeView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                if (!flashOn)
                    toggleFlash.setImageResource(R.drawable.ic_baseline_flash_on_24);
                flashOn = true;
            }

            @Override
            public void onTorchOff() {
                if (flashOn)
                    toggleFlash.setImageResource(R.drawable.ic_baseline_flash_off_24);
                flashOn = false;
            }
        });

        toggleFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashOn)
                    barcodeView.setTorchOff();
                else
                    barcodeView.setTorchOn();
            }
        });

        flipCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeView.pauseAndWait();
                CameraSettings cameraSettings = barcodeView.getCameraSettings();
                if (!frontCam) {
                    cameraSettings.setRequestedCameraId(1);
                    barcodeView.getBarcodeView().setCameraSettings(cameraSettings);
                    barcodeView.resume();
                    toggleFlash.setVisibility(View.GONE);
                    frontCam = true;
                } else {
                    cameraSettings.setRequestedCameraId(0);
                    barcodeView.getBarcodeView().setCameraSettings(cameraSettings);
                    barcodeView.resume();
                    toggleFlash.setVisibility(View.VISIBLE);
                    frontCam = false;
                }

            }
        });

        viewfinderView = findViewById(R.id.zxing_viewfinder_view);
        captureManager = new CaptureManager(this, barcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.setShowMissingCameraPermissionDialog(false);
        captureManager.decode();

    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        startActivity(new Intent(CustomScanActivity.this, MainActivity.class));
//        finishAffinity();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();

    }

//    @Override
//    public void onBackPressed() {
//        finishAffinity();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        finishAffinity();
//    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        finishAffinity();
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        captureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
