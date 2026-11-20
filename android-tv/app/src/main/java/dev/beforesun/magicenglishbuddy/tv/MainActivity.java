package dev.beforesun.magicenglishbuddy.tv;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.view.KeyEvent;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import java.io.IOException;

public class MainActivity extends Activity {
    private static final String ORIGIN = "https://appassets.androidplatform.net";
    private WebView webView;
    private PermissionRequest pendingAudio;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        webView = new WebView(this);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
            .addPathHandler("/magic-english-buddy/", new WebViewAssetLoader.AssetsPathHandler(this))
            .build();
        webView.setWebViewClient(new WebViewClientCompat() {
            @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (request.getUrl().getPath().equals("/magic-english-buddy/")) {
                    try { return new WebResourceResponse("text/html", "UTF-8", getAssets().open("index.html")); }
                    catch (IOException ignored) { return null; }
                }
                return loader.shouldInterceptRequest(request.getUrl());
            }
            @Override public void onPageFinished(WebView view, String url) {
                view.evaluateJavascript("document.documentElement.classList.add('tv-mode');" +
                    "var s=document.createElement('style');s.textContent='*:focus{outline:4px solid #ffcc00!important;outline-offset:3px!important}';document.head.appendChild(s);" +
                    "document.addEventListener('keydown',function(e){if(!['ArrowUp','ArrowDown','ArrowLeft','ArrowRight'].includes(e.key))return;" +
                    "if(['INPUT','TEXTAREA'].includes(document.activeElement.tagName))return;" +
                    "var a=[...document.querySelectorAll('button,a,input,select,textarea,[role=button],[tabindex]')].filter(x=>!x.disabled&&x.getBoundingClientRect().width>0&&x.getBoundingClientRect().height>0);" +
                    "var c=document.activeElement,r=c.getBoundingClientRect(),cx=r.left+r.width/2,cy=r.top+r.height/2;" +
                    "if(!a.includes(c)){a[0]?.focus();e.preventDefault();return;}" +
                    "var v=a.filter(x=>x!==c).map(x=>{var b=x.getBoundingClientRect(),dx=b.left+b.width/2-cx,dy=b.top+b.height/2-cy;return{x,dx,dy}})" +
                    ".filter(q=>e.key==='ArrowRight'?q.dx>4:e.key==='ArrowLeft'?q.dx< -4:e.key==='ArrowDown'?q.dy>4:q.dy< -4)" +
                    ".sort((p,q)=>{var f=z=>(e.key==='ArrowRight'||e.key==='ArrowLeft')?Math.abs(z.dx)+Math.abs(z.dy)*2:Math.abs(z.dy)+Math.abs(z.dx)*2;return f(p)-f(q)});" +
                    "if(v.length){v[0].x.focus();v[0].x.scrollIntoView({block:'nearest',inline:'nearest'});e.preventDefault();}});", null);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onPermissionRequest(PermissionRequest request) {
                if (!"https".equals(request.getOrigin().getScheme()) ||
                    !"appassets.androidplatform.net".equals(request.getOrigin().getHost()) ||
                    !java.util.Arrays.asList(request.getResources()).contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                    request.deny(); return;
                }
                pendingAudio = request;
                if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    request.grant(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE});
                    pendingAudio = null;
                } else requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            }
            @Override public void onPermissionRequestCanceled(PermissionRequest request) {
                if (pendingAudio == request) pendingAudio = null;
            }
        });
        webView.loadUrl(ORIGIN + "/magic-english-buddy/");
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == 1 && pendingAudio != null) {
            if (results.length > 0 && results[0] == android.content.pm.PackageManager.PERMISSION_GRANTED)
                pendingAudio.grant(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE});
            else pendingAudio.deny();
            pendingAudio = null;
        }
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) { webView.goBack(); return true; }
        return super.onKeyDown(keyCode, event);
    }
}
