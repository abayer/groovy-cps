package com.cloudbees.groovy.cps.impl;

import com.cloudbees.groovy.cps.Env;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link Env} for a new block.
 *
 * @author Kohsuke Kawaguchi
 */
// TODO: should be package local once all the impls move into this class
public class BlockScopeEnv extends ProxyEnv {
    private final Map<String,Object> locals = new HashMap<String, Object>();
    private Map<String, Class> types = new HashMap<String, Class>();

    public BlockScopeEnv(Env parent) {
        super(parent);
    }

    public void declareVariable(Class type, String name) {
        locals.put(name, null);
        getTypes().put(name, type);
    }

    public Object getLocalVariable(String name) {
        if (locals.containsKey(name))
            return locals.get(name);
        else
            return parent.getLocalVariable(name);
    }

    /** Because might deserialize old version of class with null value for field */
    private Map<String, Class> getTypes() {
        if (types == null) {
            this.types = new HashMap<String, Class>();
        }
        return this.types;
    }

    public Class getLocalVariableType(String name) {
        return (locals.containsKey(name)) ? getTypes().get(name) : parent.getLocalVariableType(name);
    }

    public void setLocalVariable(String name, Object value) {
        if (locals.containsKey(name))
            locals.put(name,value);
        else
            parent.setLocalVariable(name, value);
    }

    private static final long serialVersionUID = 1L;
}
