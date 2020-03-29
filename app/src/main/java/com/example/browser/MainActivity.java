package com.example.browser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView browser;
    EditText searchBar;
    Button forward;
    Button back;
    Button reload;
    Button search;
    ProgressBar progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        browser = findViewById(R.id.web_view);
        searchBar = findViewById(R.id.search_bar);
        forward = findViewById(R.id.forward_btn);
        back = findViewById(R.id.back_btn);
        reload = findViewById(R.id.reload_btn);
        search = findViewById(R.id.search_btn);
        progbar = findViewById(R.id.simpleProgressBar);

        browser.loadUrl("https://www.google.com");

        search.setOnClickListener(searchClick);

        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER){
                    searchQuery();
                }
                return false;
            }
        });



        browser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wv, String request) {
                browser.getSettings().setJavaScriptEnabled(true);
                browser.getSettings().setLoadsImagesAutomatically(true);
                browser.getSettings().setDomStorageEnabled(true);
                wv.loadUrl(request);
                progbar.setVisibility(View.VISIBLE);
                browser.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                searchBar.setText(browser.getUrl());
                progbar.setVisibility(View.GONE);
                browser.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
            }
        });
    }





    public void searchQuery(){
        if (searchBar.length() != 0) {
            browser.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            browser.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            browser.getSettings().setLoadsImagesAutomatically(true);
            browser.getSettings().setJavaScriptEnabled(true);

            if (!searchBar.getText().toString().contains("https://") && searchBar.getText().toString().contains(".com")) {
                browser.loadUrl("https://" + searchBar.getText().toString());
            } else if (!searchBar.getText().toString().contains("https://") && !searchBar.getText().toString().contains(".com")) {
                String url = "https://www.google.com/search?q=";
                browser.loadUrl(url + searchBar.getText().toString());
            } else {
                browser.loadUrl(searchBar.getText().toString());
            }
            browser.setVisibility(View.INVISIBLE);
            progbar.setVisibility(View.VISIBLE);

        }
    }


    private View.OnClickListener searchClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchQuery();
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_btn:
                if (browser.canGoBack()) {
                    browser.goBack();
                }
                return true;

            case R.id.forward_btn:
                if (browser.canGoForward()) {
                    browser.goForward();
                }
                return true;

            case R.id.reload_btn:
                browser.loadUrl(searchBar.getText().toString());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}