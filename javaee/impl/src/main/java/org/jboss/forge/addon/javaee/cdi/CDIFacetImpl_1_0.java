/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.javaee.cdi;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.javaee.Descriptors;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
import org.jboss.forge.furnace.versions.SingleVersion;
import org.jboss.forge.furnace.versions.Version;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;

/**
 * Implementation of {@link CDIFacet} for spec version 1.0
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class CDIFacetImpl_1_0 extends AbstractCDIFacetImpl<BeansDescriptor> implements CDIFacet_1_0
{
   private static final Dependency JBOSS_ANNOTATION_API = DependencyBuilder
            .create("org.jboss.spec.javax.annotation:jboss-annotations-api_1.1_spec");
   private static final Dependency JAVAX_INTERCEPTOR_API = DependencyBuilder
            .create("org.jboss.spec.javax.interceptor:jboss-interceptors-api_1.1_spec");
   private static final Dependency JAVAX_INJECT = DependencyBuilder.create("javax.inject:javax.inject:1");
   private static final Dependency JAVAX_ANNOTATION_API = DependencyBuilder.create("javax.annotation:jsr250-api:1.0");
   private static final Dependency CDI_API = DependencyBuilder.create("javax.enterprise:cdi-api:[1.0,1.1)");

   @Inject
   public CDIFacetImpl_1_0(DependencyInstaller installer)
   {
      super(installer);
   }

   @Override
   protected Class<BeansDescriptor> getDescriptorType()
   {
      return BeansDescriptor.class;
   }

   @Override
   public Version getSpecVersion()
   {
      return new SingleVersion("1.0");
   }

   @Override
   protected String getDescriptorContent()
   {
      return Descriptors.create(BeansDescriptor.class).exportAsString();
   }

   @Override
   protected Map<Dependency, List<Dependency>> getRequiredDependencyOptions()
   {
      Map<Dependency, List<Dependency>> result = new LinkedHashMap<Dependency, List<Dependency>>();

      result.put(CDI_API, Arrays.asList(CDI_API));
      result.put(JAVAX_ANNOTATION_API, Arrays.asList(JAVAX_ANNOTATION_API, JBOSS_ANNOTATION_API));
      result.put(JAVAX_INJECT, Arrays.asList(JAVAX_INJECT));
      result.put(JAVAX_INTERCEPTOR_API, Arrays.asList(JAVAX_INTERCEPTOR_API));

      return result;
   }

}
