package com.example.mobileapplication9_;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et;
    Button b_Go;
    WebView wv;
    ProgressDialog dialog;
    LinearLayout linearLayout;
    Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout)findViewById(R.id.linear);
        et = (EditText)findViewById(R.id.editText);
        b_Go = (Button)findViewById(R.id.b_Go);
        wv = (WebView)findViewById(R.id.webView);
        //통신하고싶으면
        wv.addJavascriptInterface(new javaScriptMethod(),"myApp");//지금 내가 만든 클래스, 자바스크립트가 얘 사용할 떄 부를 이름
        dialog = new ProgressDialog(this);

        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setBuiltInZoomControls(true);
        ws.setSupportZoom(true);

        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }//어떤 연결 프로그램으로 할거?를 안 찾도록

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                et.setText(url);
            }//페이지 가져오는 콜백 후, 할 활동

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.setMessage("로딩중...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
            }
        });
        wv.loadUrl("http://blog.daum.net/qkrska66");

        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress >= 100)dialog.dismiss();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });

        anim = AnimationUtils.loadAnimation(this,R.anim.translate);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"즐찾");//두번째꺼가 아이디
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 1){
            wv.loadUrl("file:///android_asset/www/index.html");//아까 불러왔던 웹파일
            linearLayout.setAnimation(anim);
            anim.start();
        }
        return super.onOptionsItemSelected(item);
    }//어떤 메뉴 item 눌렀을 때 로드 url 했던 곳으로 이동하는!!!꺄아 된닿ㅎㅎㅎ

    Handler myHan = new Handler();

    class javaScriptMethod {
        //자바 스크립트가 쓸 거라고 어노테이션 필수(안 하면 웹페이지에서 사용 못해)
        @JavascriptInterface
        public void displayToast() {
//            Toast.makeText(getApplicationContext(),"HI",Toast.LENGTH_SHORT).show();
            myHan.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("그림 바꾸기").setMessage("바꿔요?")
                            .setNegativeButton("ㄴㄴ",null)
                            .setPositiveButton("ㅇㅋ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    wv.loadUrl("javascript:changeImage()");
                                }
                            }).show();
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.b_Go){

        }
        else if(view.getId() == R.id.b_aa){
            wv.loadUrl("javascript:changeImege()");
        }
    }
}