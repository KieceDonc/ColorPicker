package com.vvdev.colorpicker.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.vvdev.colorpicker.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.inflate;
import static com.vvdev.colorpicker.fragment.Import.ImportFragment.IntentExtraPath;

public class Import_WebViewer extends AppCompatActivity {

//    https://stackoverflow.com/a/56091777/12577512

    // https://stackoverflow.com/questions/11613505/how-to-open-local-pdf-file-in-webview-in-android

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.import_webviewer);

        Intent receiveData = getIntent(); // get intent
        String path = receiveData.getStringExtra(IntentExtraPath); // get img path from intent
        File pdfFile = new File(path);
        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromFile(pdfFile).load();

        /*WebView webview = findViewById(R.id.import_WebView);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("https://mozilla.github.io/pdf.js/web/viewer.html");
        webview.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + file.getAbsolutePath() + "#zoom="+webview.getWidth()); // + "#zoom="+webview.getWidth()*/
    }

}
