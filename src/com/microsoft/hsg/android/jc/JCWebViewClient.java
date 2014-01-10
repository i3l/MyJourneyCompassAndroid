package com.microsoft.hsg.android.jc;

import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JCWebViewClient extends WebViewClient {

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		handler.proceed(); // Ignore SSL certificate errors
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		/*
		 * if (Uri.parse(url).getHost().equals("www.example.com")) { // This is
		 * my web site, so do not override; let my WebView load the page return
		 * false; } // Otherwise, the link is not for a page on my site, so
		 * launch another Activity that handles URLs Intent intent = new
		 * Intent(Intent.ACTION_VIEW, Uri.parse(url)); startActivity(intent);
		 * return true;
		 */
		view.loadUrl(url);
		return true;
	}

}
