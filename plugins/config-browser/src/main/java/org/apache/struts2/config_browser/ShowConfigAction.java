/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.struts2.config_browser;

import java.beans.PropertyDescriptor;
import java.util.Set;
import java.util.TreeSet;

import org.apache.struts2.xwork2.ObjectFactory;
import org.apache.struts2.xwork2.config.entities.ActionConfig;
import org.apache.struts2.xwork2.inject.Inject;
import org.apache.struts2.xwork2.util.logging.Logger;
import org.apache.struts2.xwork2.util.logging.LoggerFactory;
import org.apache.struts2.xwork2.util.reflection.ReflectionProvider;

/**
 * ShowConfigAction
 */
public class ShowConfigAction extends ActionNamesAction {

    private static final long serialVersionUID = -1630527489407671652L;

    private static final PropertyDescriptor[] PDSAT = new PropertyDescriptor[0];

    private String namespace;
    private String actionName;
    private ActionConfig config;
    private Set actionNames;
    private String detailView = "results";
    private PropertyDescriptor[] properties;
    private static Logger LOG = LoggerFactory.getLogger(ShowConfigAction.class);
    
    private ObjectFactory objectFactory;
    private ReflectionProvider reflectionProvider;

    public String getDetailView() {
        return detailView;
    }

    public void setDetailView(String detailView) {
        this.detailView = detailView;
    }

    public Set getActionNames() {
        return actionNames;
    }

    public String getNamespace() {
        return namespace;
    }
    
    @Inject
    public void setObjectFactory(ObjectFactory fac) {
        this.objectFactory = fac;
    }
    
    @Inject
    public void setReflectionProvider(ReflectionProvider prov) {
        this.reflectionProvider = prov;
    }

    public String stripPackage(Class clazz) {
        return clazz.getName().substring(clazz.getName().lastIndexOf('.') + 1);
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public ActionConfig getConfig() {
        return config;
    }

    public PropertyDescriptor[] getProperties() {
        return properties;
    }

    public String execute() throws Exception {
        super.execute();
        config = configHelper.getActionConfig(namespace, actionName);
        actionNames =
                new TreeSet(configHelper.getActionNames(namespace));
        try {
            Class clazz = objectFactory.getClassInstance(getConfig().getClassName());
            properties = reflectionProvider.getPropertyDescriptors(clazz);
        } catch (Exception e) {
            LOG.error("Unable to get properties for action " + actionName, e);
            addActionError("Unable to retrieve action properties: " + e.toString());
        }

        if (hasErrors()) //super might have set some :)
            return ERROR;
        else
            return SUCCESS;
    }
}

