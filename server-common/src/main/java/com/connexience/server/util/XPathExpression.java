package com.connexience.server.util;

import java.io.Serializable;

/**
 * Author: Simon
 * Date: May 14, 2009
 */
public class XPathExpression implements Serializable
{
  private String left;
  private String right;
  private String operator;

  public XPathExpression()
  {
  }

  public XPathExpression(String left, String right, String operator)
  {

    this.left = left;
    this.right = right;
    this.operator = operator;
  }

  public String getLeft()
  {
    return left;
  }

  public void setLeft(String left)
  {
    this.left = left;
  }

  public String getRight()
  {
    return right;
  }

  public void setRight(String right)
  {
    this.right = right;
  }

  public String getOperator()
  {
    return operator;
  }

  public void setOperator(String operator)
  {
    this.operator = operator;
  }


}
