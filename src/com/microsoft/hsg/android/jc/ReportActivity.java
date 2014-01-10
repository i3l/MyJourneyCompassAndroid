package com.microsoft.hsg.android.jc;

import com.microsoft.hsg.android.jc.util.CustomUtil;

import android.app.ProgressDialog;
import android.app.Activity;
import android.graphics.Bitmap;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class ReportActivity extends Activity {
	private WebView webView;
	protected String urlString = "https://journeycompass.i3l.gatech.edu/SymptomSummary.aspx";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.report_activity);

		setProgressBarIndeterminateVisibility(true);
		setProgressBarVisibility(true);

		try {
			webView = (WebView) findViewById(R.id.reportWebView);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);

			final Activity myActivity = this;

			webView.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					// Activities and WebViews measure progress with different
					// scales.
					// The progress meter will automatically disappear when we
					// reach
					// 100%
					// myActivity.setProgress(progress * 1000);
					setProgress(progress * 100);
					if (progress == 100) {
						setProgressBarIndeterminateVisibility(false);
						setProgressBarVisibility(false);
					}
				}

			});
			webView.setWebViewClient(new WebViewClient() {
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					Toast.makeText(myActivity, "Oh no! " + description,
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onReceivedSslError(WebView view,
						SslErrorHandler handler, SslError error) {
					handler.proceed(); // Ignore SSL certificate errors
				}
				
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					setProgressBarIndeterminateVisibility(true);
					setProgressBarVisibility(true);

					super.onPageStarted(view, url, favicon);
			    }
			});

			// Load a page
			webView.loadUrl(urlString);

			Button button = (Button) findViewById(R.id.reload);
		    button.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	webView.stopLoading();
		        	webView.loadUrl(urlString);             
		        }
		    });

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void loadSymptomActivity(View arg) {
		Intent intent = new Intent(ReportActivity.this, SymptomActivity.class);
		ReportActivity.this.startActivity(intent);
	}

	public void loadSettingsActivity(View arg) {
		if (CustomUtil.getInstance().isNetworkAvailable(this)) {
			Intent intent = new Intent(ReportActivity.this,
					SettingsActivity.class);
			ReportActivity.this.startActivity(intent);
		} else {
			Toast.makeText(
					ReportActivity.this,
					"Settings requires Internet connection.\nPlease try again in Wi-Fi Network.",
					Toast.LENGTH_LONG).show();
		}
	}
}
