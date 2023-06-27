package cz.mr.TurtleWeb;

import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.collect.Sets;
import com.google.appinventor.components.runtime.errors.IllegalArgumentError;
import com.google.appinventor.components.runtime.errors.FileIOError;
import com.google.appinventor.components.runtime.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@DesignerComponent(version = 1,
    description = "Turtle for WebViewer.",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "")
@UsesPermissions(permissionNames = "WRITE_EXTERNAL_STORAGE")
@SimpleObject(external = true)
public class TurtleWeb extends AndroidNonvisibleComponent implements Component {

    private WebViewerX mWebViewerX;
    private String mTurtleName;
 
    public TurtleWeb(ComponentContainer container) {
        super(container.$form());
        
        // The following values get set when WebTurtle is placed into a webviewer...
        mWebViewerX = null;
        mTurtleName = null;
    }
    
    @SimpleProperty(userVisible = true,
        description = "WebViewer for displaying the turtle")
    public void AddTurtleToWebViewer(Component wv) {
        // Nicer error messaging...
        if(!(wv instanceof WebViewer)) {
            throw new IllegalArgumentError("TurtleWeb can only be inside a WebViewer, not " + wv);
        }
        
        this.mWebViewerX = WebViewerX.getWebViewerX((WebViewer)wv);
        mWebViewerX.addTurtle(this);
        
        mWebViewerX.RunJavaScript("turtley.onReady = async () => {"
                                  + mTurtleName+" = new turtley.Turtle();"
                                  + mTurtleName+".init(); }; ");

        // Load all the properties
        mTurtleName = "turtle" + this.hashCode();
    }

    @SimpleFunction(description = "Move the turtle forward a distance in the direction the turtle is headed.")
    public void Forward(double distance) {
        if(mWebViewerX != null) {
            mWebViewerX.RunJavaScript(mTurtleName+".forward(" + distance + ")");
        }
    }   

    @SimpleFunction(description = "Move the turtle backward a distance in the direction the turtle is headed.")
    public void Backward(double distance) {
        if(mWebViewerX != null) {
            mWebViewerX.RunJavaScript(mTurtleName+".backward(" + distance + ")");
        }
    }   

    @SimpleFunction(description = "Turn the turtle to its right some number of degrees.")
    public void Right(double angle) {
        if(mWebViewerX != null) {
            mWebViewerX.RunJavaScript(mTurtleName+".right(" + angle + ")");
        }
    }
    
    @SimpleFunction(description = "Turn the turtle to its left some number of degrees.")
    public void Left(double angle) {
        if(mWebViewerX != null) {
            mWebViewerX.RunJavaScript(mTurtleName+".left(" + angle + ")");
        }
    }
    
    @SimpleFunction(description = "Move the turtle to the specified position.")
    public void Goto(double x, double y) {
        if(mWebViewerX != null) {
            mWebViewerX.RunJavaScript(mTurtleName+".moveTo(" + x + "," + y + ")");
        }
    }
    
    @SimpleFunction(description = "Pick up the turtle's pen.")
    public void PenUp() {
        if(mWebViewerX != null) {
            mWebViewerX.RunJavaScript(mTurtleName+".penUp()");
        }
    }
    
    @SimpleFunction(description = "Put down the turtle's pen.")
    public void PenDown() {
        if(mWebViewerX != null) {
            mWebViewerX.RunJavaScript(mTurtleName+".penDown()");
        }
    }
    
    @SimpleFunction(description = "...")
    public void HTMLLog(String s) {
        if(mWebViewerX != null) {
            mWebViewerX.RunJavaScript("document.body.appendChild(document.createTextNode('"+s+"'))");
        }
    }
    
    @SimpleEvent(description = "Screen Touched.")
    public void OnScreenClick(double x, double y) {
        EventDispatcher.dispatchEvent(this, "OnScreenClick", x, y);
    }


}
