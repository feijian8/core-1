/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.javaee.jpa.ui.setup;

import java.util.Comparator;
import java.util.TreeSet;

import javax.inject.Inject;

import org.jboss.forge.addon.convert.Converter;
import org.jboss.forge.addon.javaee.jpa.PersistenceContainer;
import org.jboss.forge.addon.javaee.jpa.PersistenceMetaModelFacet;
import org.jboss.forge.addon.javaee.jpa.PersistenceProvider;
import org.jboss.forge.addon.javaee.jpa.containers.JBossEAP6Container;
import org.jboss.forge.addon.javaee.jpa.providers.HibernateProvider;
import org.jboss.forge.addon.javaee.ui.AbstractJavaEECommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIValidationContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.input.UISelectOne;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.result.NavigationResult;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.wizard.UIWizard;
import org.jboss.forge.furnace.services.Imported;

public class PersistenceSetupWizard extends AbstractJavaEECommand implements UIWizard
{

   @Inject
   @WithAttributes(shortName = 'c', label = "Container", required = true)
   private UISelectOne<PersistenceContainer> container;

   @Inject
   @WithAttributes(shortName = 'p', label = "Provider", required = true)
   private UISelectOne<PersistenceProvider> provider;

   @Inject
   @WithAttributes(shortName = 'm', label = "Install a JPA 2 metamodel generator?")
   private UIInput<Boolean> configureMetadata;

   @Inject
   private JBossEAP6Container defaultContainer;

   @Inject
   private HibernateProvider defaultProvider;

   @Inject
   private Imported<PersistenceMetaModelFacet> metaModelFacets;

   @Override
   public Metadata getMetadata(UIContext context)
   {
      return Metadata.from(super.getMetadata(context), getClass()).name("JPA: Setup")
               .description("Setup JPA in your project")
               .category(Categories.create(super.getMetadata(context).getCategory().getName(), "JPA"));
   }

   /**
    * Return true only if a project is selected
    */
   @Override
   public boolean isEnabled(UIContext context)
   {
      return containsProject(context);
   }

   @Override
   public void initializeUI(UIBuilder builder) throws Exception
   {
      initContainers(builder.getUIContext());
      initProviders();
      initConfigureMetadata();
      builder.add(container).add(provider).add(configureMetadata);
   }

   private void initContainers(UIContext context)
   {
      final boolean isGUI = context.getProvider().isGUI();
      container.setItemLabelConverter(new Converter<PersistenceContainer, String>()
      {
         @Override
         public String convert(PersistenceContainer source)
         {
            return source != null ? source.getName(isGUI) : null;
         }
      });
      // Ordering items:
      TreeSet<PersistenceContainer> treeSet = new TreeSet<PersistenceContainer>(new Comparator<PersistenceContainer>()
      {
         @Override
         public int compare(PersistenceContainer o1, PersistenceContainer o2)
         {
            return String.valueOf(o1.getName(isGUI)).compareTo(o2.getName(isGUI));
         }
      });
      Iterable<PersistenceContainer> valueChoices = container.getValueChoices();
      for (PersistenceContainer persistenceContainer : valueChoices)
      {
         treeSet.add(persistenceContainer);
      }
      container.setValueChoices(treeSet);
      container.setDefaultValue(defaultContainer);
   }

   private void initProviders()
   {
      provider.setItemLabelConverter(new Converter<PersistenceProvider, String>()
      {
         @Override
         public String convert(PersistenceProvider source)
         {
            return source != null ? source.getName() : null;
         }
      });
      provider.setDefaultValue(defaultProvider);
   }

   private void initConfigureMetadata()
   {
      configureMetadata.setDefaultValue(Boolean.FALSE);
      if (metaModelFacets.isUnsatisfied())
      {
         configureMetadata.setEnabled(false);
      }
   }

   @Override
   public void validate(UIValidationContext validator)
   {
      // NOOP
   }

   @Override
   public Result execute(final UIContext context) throws Exception
   {
      applyUIValues(context);
      return Results.success();
   }

   @Override
   public NavigationResult next(UIContext context) throws Exception
   {
      applyUIValues(context);
      return Results.navigateTo(PersistenceSetupConnectionStep.class);
   }

   private void applyUIValues(final UIContext context)
   {
      context.setAttribute(PersistenceProvider.class, provider.getValue());
      context.setAttribute(PersistenceContainer.class, container.getValue());
      context.setAttribute("ConfigureMetadata", configureMetadata.getValue());
   }

   public UISelectOne<PersistenceContainer> getContainers()
   {
      return container;
   }

   public UISelectOne<PersistenceProvider> getProviders()
   {
      return provider;
   }

   public UIInput<Boolean> getConfigureMetadata()
   {
      return configureMetadata;
   }

   @Override
   protected boolean isProjectRequired()
   {
      return false;
   }
}
