package org.woped.metrics.exceptions;

import org.woped.gui.translations.Messages;

public class FormulaVariableNotFoundException extends CalculateFormulaException {

 /**
  * 
  */
 private static final long serialVersionUID = 3548418885943793131L;
 
 private String variable;
 private int positionInLine=0;
 
 public FormulaVariableNotFoundException(String variable) {
  super();
  this.variable = variable;
 }
 

 public FormulaVariableNotFoundException(String variable, int positionInLine) {
  super();
  this.variable = variable;
  this.positionInLine = positionInLine;
 }


 public String getVariable() {
  return variable;
 }

 @Override
 public String getLocalizedMessage() {
  if(positionInLine == 0){
   return Messages.getString("Metrics.Calculate.Error.FormulaVariableNotFound") + " " + this.getVariable();
  }else{
   return Messages.getString("Metrics.Calculate.Error.Antlr.Position") + " " + positionInLine + ": "
   + Messages.getString("Metrics.Calculate.Error.FormulaVariableNotFound") + 
   " " + this.getVariable();
  }
 }
 
 
}