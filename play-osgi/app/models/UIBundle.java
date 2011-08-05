/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import org.osgi.framework.Bundle;

/**
 *
 * @author mathieuancelin
 */
public class UIBundle {
    
    private final Bundle bundle;

    public UIBundle(Bundle bundle) {
        this.bundle = bundle;
    }
    
    
    public String getName() {
        return bundle.getSymbolicName();
    }
    
    public void setName(String name) {
        
    }
    
    public String getState() {
        return "" + bundle.getState();
    }
    
    public void setState(String state) {
        
    }

    
}
