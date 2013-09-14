/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.addon.ui.context;

import java.util.ArrayList;
import java.util.List;

import org.jboss.forge.addon.ui.input.InputComponent;

/**
 * This class provides a skeletal implementation of the <tt>UIValidationContext</tt> interface, to minimize the effort
 * required to implement this interface.
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
public abstract class AbstractUIValidationContext implements UIValidationContext
{
   private List<String> errors = new ArrayList<String>();
   private List<String> warnings = new ArrayList<String>();
   private List<String> informations = new ArrayList<String>();
   private InputComponent<?, ?> currentInput;

   @Override
   public void addValidationError(InputComponent<?, ?> input, String errorMessage)
   {
      errors.add(errorMessage);
   }

   @Override
   public void addValidationWarning(InputComponent<?, ?> input, String warningMessage)
   {
      warnings.add(warningMessage);
   }

   @Override
   public void addValidationInformation(InputComponent<?, ?> input, String infoMessage)
   {
      informations.add(infoMessage);
   }

   public void setCurrentInput(InputComponent<?, ?> currentInput)
   {
      this.currentInput = currentInput;
   }

   @Override
   public InputComponent<?, ?> getCurrentInputComponent()
   {
      return currentInput;
   }

   public List<String> getErrors()
   {
      return errors;
   }

   public List<String> getInformations()
   {
      return informations;
   }

   public List<String> getWarnings()
   {
      return warnings;
   }
}
