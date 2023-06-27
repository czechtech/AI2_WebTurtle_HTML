package cz.mr.TurtleWeb;

import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.google.appinventor.components.runtime.WebViewer;

import java.util.ArrayList;
import java.util.List;

public class WebViewerX {
    
		// List of the WebViewerXs
		private static List<WebViewerX> sWebViewerXs = new ArrayList<WebViewerX>();

		private WebViewer mWebViewer;
		private WebViewX  mWebViewX;
		private List<TurtleWeb> mCurrentTurtles;
		
    // This should only be used if no WebViewerX already exists for the WebViewer
		protected WebViewerX(WebViewer wvr) {
        mWebViewer = wvr;
        mCurrentTurtles = new ArrayList<TurtleWeb>();
        sWebViewerXs.add(this);
        
        // **This is integrates WebViewerX into the Event sequence**
        mWebViewX = new WebViewX((WebView)wvr.getView());
        
        // TODO: listen for WebViewer's page loads to know when to remove turtles?
		}
		
		// Factory Method to prevent multiple WebViewerX for a single WebViewer
    public static WebViewerX getWebViewerX(WebViewer wvr) {
				for(WebViewerX wvx : sWebViewerXs) {
						if(wvx.mWebViewer == wvr) {
								return wvx;
						}
				}
				return new WebViewerX(wvr);
    }

    public void addTurtle(TurtleWeb t) {
        // Remove turtle from any prior WebViewer
				if(getWebViewerX(t) != null)  {
				    getWebViewerX(t).removeTurtle(t);
        }
				mCurrentTurtles.add(t);
        if(mCurrentTurtles.size() == 1) {
    				mWebViewer.GoToUrl("https://czechtech.github.io/AI2_WebTurtle_HTML/blank.html");
    		}
    }
    
    public void removeTurtle(TurtleWeb t) {
        mCurrentTurtles.remove(t);
        if(mCurrentTurtles.size() == 0) {
            mWebViewer.GoToUrl("about:blank");
        }
    }
    
    protected static WebViewerX getWebViewerX(TurtleWeb t) {
				for(WebViewerX wvx : sWebViewerXs) {
						if(wvx.mCurrentTurtles.contains(t)) {
								return wvx;
						}
				}
				return null;
    }
    
    public void RunJavaScript(String js) {
        mWebViewer.RunJavaScript(js);
    }

		private final class WebViewX implements OnTouchListener {
		    private WebView mWebView;
		    
		    public WebViewX(WebView wv) {
		        mWebView = wv;
		        mWebView.setOnTouchListener(this);
		    }
		
				public boolean onTouch(View v, MotionEvent e) {
						double x = (double)e.getX();
						double y = (double)e.getY();
						x = x - v.getWidth()/2.0; // adjust to 0,0 in the center
						y = -y + v.getHeight()/2.0; // invert y and adjust for 0,0 in the center
						for(TurtleWeb t : mCurrentTurtles) {
								t.OnScreenClick(x, y);
								// TODO: parse for other events
						}
				
						// TODO: Should probably return true if one of the turtles is touched
						return mWebView.onTouchEvent(e);
				}
		}

}
