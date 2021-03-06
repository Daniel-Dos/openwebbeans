/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.corespi.se;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.webbeans.spi.BDABeansXmlScanner;

/**
 * 
 * Tracks contents of each BDA's beans.xml to determine
 * the interceptor, alternative, decorator, or etc that
 * is applicable for a given BDA.
 *
 */
public class DefaultBDABeansXmlScanner implements BDABeansXmlScanner
{

    /**
     * Map containing bean classes with reference to beans.xml of containing JAR
     */
    private Map<Class<?>, String> jarBeanClasses;

    /**
     * Maps BDA beans.xml file to the set of interceptors defined in the
     * beans.xml
     */
    private final Map<String, Set<Class<?>>> interceptorsPerBDA;

    /**
     * Maps BDA beans.xml file to the set of decorators defined in the beans.xml
     */
    private final Map<String, Set<Class<?>>> decoratorsPerBDA;

    /**
     * Maps BDA beans.xml file to the set of alternatives defined in the
     * beans.xml
     */
    private final Map<String, Set<Class<?>>> alternativesPerBDA;

    /**
     * Maps BDA beans.xml file to the set of stereotypes defined in the
     * beans.xml
     */
    private final Map<String, Set<Class<? extends Annotation>>> stereotypesPerBDA;

    public DefaultBDABeansXmlScanner()
    {
        interceptorsPerBDA = new HashMap<>();
        decoratorsPerBDA = new HashMap<>();
        stereotypesPerBDA = new HashMap<>();
        alternativesPerBDA = new HashMap<>();
        jarBeanClasses = new HashMap<>();
    }

    /**
     * Stores the beans.xml an Interceptor class is defined in
     * 
     * @return T - Interceptor class successfully stored; F - Interceptor class
     *         already exists and was not stored.
     */
    @Override
    public boolean addInterceptor(Class<?> interceptorClass, String beansXMLFilePath)
    {

        if (interceptorClass == null || beansXMLFilePath == null)
        {
            return false;
        }
        synchronized (interceptorsPerBDA)
        {
            Set<Class<?>> interceptorClasses = interceptorsPerBDA.computeIfAbsent(beansXMLFilePath, k -> new HashSet<>());
            return interceptorClasses.add(interceptorClass);
        }
    }

    /**
     * @param beansXMLFilePath
     * @return a non-null set of Interceptors defined by the specified
     *         beansXMLFilePath
     */
    @Override
    public Set<Class<?>> getInterceptors(String beansXMLFilePath)
    {
        Set<Class<?>> set;
        synchronized (interceptorsPerBDA)
        {
            set = interceptorsPerBDA.get(beansXMLFilePath);
            if (set != null)
            {
                return new HashSet<>(set);
            }
            else
            {
                return new HashSet<>();
            }
        }
    }

    /**
     * @param beansXMLFilePath
     * @return a non-null set of Decorators defined by the specified
     *         beansXMLFilePath
     */
    @Override
    public Set<Class<?>> getDecorators(String beansXMLFilePath)
    {
        Set<Class<?>> set;
        synchronized (decoratorsPerBDA)
        {
            set = decoratorsPerBDA.get(beansXMLFilePath);
            if (set != null)
            {
                return new HashSet<>(set);
            }
            else
            {
                return new HashSet<>();
            }
        }
    }

    /**
     * Stores the beans.xml a Decorator class is defined in
     * 
     * @return T - Decorator class successfully stored; F - Decorator class
     *         already exists and was not stored.
     */
    @Override
    public boolean addDecorator(Class<?> decoratorClass, String beansXMLFilePath)
    {

        if (decoratorClass == null || beansXMLFilePath == null)
        {
            return false;
        }
        synchronized (decoratorsPerBDA)
        {
            Set<Class<?>> decoratorClasses = decoratorsPerBDA.computeIfAbsent(beansXMLFilePath, k -> new HashSet<>());
            return decoratorClasses.add(decoratorClass);
        }

    }

    /**
     * Stores the beans.xml an Alternative class is defined in
     * 
     * @return T - Alternative class successfully stored; F - Alternative class
     *         already exists and was not stored.
     */
    @Override
    public boolean addAlternative(Class<?> alternativeClass, String beansXMLFilePath)
    {

        if (alternativeClass == null || beansXMLFilePath == null)
        {
            return false;
        }
        synchronized (alternativesPerBDA)
        {
            Set<Class<?>> alternativeClasses = alternativesPerBDA.computeIfAbsent(beansXMLFilePath, k -> new HashSet<>());
            return alternativeClasses.add(alternativeClass);
        }
    }

    /**
     * Stores the beans.xml a Stereotype class is defined in
     * 
     * @return T - Stereotype class successfully stored; F - Stereotype class
     *         already exists and was not stored.
     */
    @Override
    public boolean addStereoType(Class<? extends Annotation> stereoTypeClass, String beansXMLFilePath)
    {

        if (stereoTypeClass == null || beansXMLFilePath == null)
        {
            return false;
        }
        synchronized (stereotypesPerBDA)
        {
            Set<Class<? extends Annotation>> stereoTypeClasses = stereotypesPerBDA.computeIfAbsent(beansXMLFilePath, k -> new HashSet<>());
            return stereoTypeClasses.add(stereoTypeClass);
        }
    }

    /**
     * @param beansXMLFilePath
     * @return a non-null set of Alternatives defined by the specified
     *         beansXMLFilePath
     */
    @Override
    public Set<Class<?>> getAlternatives(String beansXMLFilePath)
    {
        Set<Class<?>> set;
        synchronized (alternativesPerBDA)
        {
            set = alternativesPerBDA.get(beansXMLFilePath);
            if (set != null)
            {
                return new HashSet<>(set);
            }
            else
            {
                return new HashSet<>();
            }
        }
    }

    /**
     * @param beansXMLFilePath
     * @return a non-null set of Stereotypes defined by the specified
     *         beansXMLFilePath
     */
    @Override
    public Set<Class<? extends Annotation>> getStereotypes(String beansXMLFilePath)
    {
        Set<Class<? extends Annotation>> set;
        synchronized (stereotypesPerBDA)
        {
            set = stereotypesPerBDA.get(beansXMLFilePath);
            if (set != null)
            {
                return new HashSet<>(set);
            }
            else
            {
                return new HashSet<>();
            }
        }
    }

    /**
     * @param class1 deployed class
     * @return A String representing the file path to the beans.xml of the
     *         specified class's BDA
     */
    @Override
    public String getBeansXml(Class<?> class1)
    {
        return jarBeanClasses.get(class1);
    }

    @Override
    public void setBeansXml(Class<?> class1, String beansXmlFilePath)
    {
        jarBeanClasses.put(class1, beansXmlFilePath);
    }
}
