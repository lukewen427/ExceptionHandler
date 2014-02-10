package com.connexience.server.ejb.search;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.io.Serializable;

/**
 * Author: Simon
 * Date: May 19, 2009
 */
public class MetaDataSearchResults implements Serializable
{
  /*
  * List of search results which contain the meta data doc and documents that it is linked from
  * */
  private List<MetaDataSearchResult> results = new ArrayList<MetaDataSearchResult>();

  /*
  * Number of results that this query generated so that we can paginate
  * */
  private int maxQueryResults;

  public MetaDataSearchResults()
  {
  }

  public MetaDataSearchResults(List<MetaDataSearchResult> results, int numResults)
  {
    this.results = results;
    this.maxQueryResults = numResults;
  }

  public void add(MetaDataSearchResult result)
  {
    results.add(result);
  }

  public int size()
  {
    return results.size();
  }

  public List<MetaDataSearchResult> getResults()
  {
    return results;
  }

  public void setResults(List<MetaDataSearchResult> results)
  {
    this.results = results;
  }

  public int getMaxQueryResults()
  {
    return maxQueryResults;
  }

  public void setMaxQueryResults(int maxQueryResults)
  {
    this.maxQueryResults = maxQueryResults;
  }
}
