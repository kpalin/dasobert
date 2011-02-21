package uk.ac.ebi.www.ontology_lookup.OntologyQuery;

public class QueryProxy implements uk.ac.ebi.www.ontology_lookup.OntologyQuery.Query {
  private String _endpoint = null;
  private uk.ac.ebi.www.ontology_lookup.OntologyQuery.Query query = null;
  
  public QueryProxy() {
    _initQueryProxy();
  }
  
  public QueryProxy(String endpoint) {
    _endpoint = endpoint;
    _initQueryProxy();
  }
  
  private void _initQueryProxy() {
    try {
      query = (new uk.ac.ebi.www.ontology_lookup.OntologyQuery.QueryServiceLocator()).getOntologyQuery();
      if (query != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)query)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)query)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (query != null)
      ((javax.xml.rpc.Stub)query)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public uk.ac.ebi.www.ontology_lookup.OntologyQuery.Query getQuery() {
    if (query == null)
      _initQueryProxy();
    return query;
  }
  
  public java.lang.String getVersion() throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getVersion();
  }
  
  public java.lang.String getTermById(java.lang.String termId, java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermById(termId, ontologyName);
  }
  
  public java.util.HashMap getTermMetadata(java.lang.String termId, java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermMetadata(termId, ontologyName);
  }
  
  public java.util.HashMap getTermXrefs(java.lang.String termId, java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermXrefs(termId, ontologyName);
  }
  
  public java.util.HashMap getOntologyNames() throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getOntologyNames();
  }
  
  public java.lang.String getOntologyLoadDate(java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getOntologyLoadDate(ontologyName);
  }
  
  public java.util.HashMap getAllTermsFromOntology(java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getAllTermsFromOntology(ontologyName);
  }
  
  public java.util.HashMap getRootTerms(java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getRootTerms(ontologyName);
  }
  
  public java.util.HashMap getTermsByName(java.lang.String partialName, java.lang.String ontologyName, boolean reverseKeyOrder) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermsByName(partialName, ontologyName, reverseKeyOrder);
  }
  
  public java.util.HashMap getTermsByExactName(java.lang.String exactName, java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermsByExactName(exactName, ontologyName);
  }
  
  public java.util.HashMap getPrefixedTermsByName(java.lang.String partialName, boolean reverseKeyOrder) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getPrefixedTermsByName(partialName, reverseKeyOrder);
  }
  
  public java.util.HashMap getTermParents(java.lang.String termId, java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermParents(termId, ontologyName);
  }
  
  public java.util.HashMap getTermChildren(java.lang.String termId, java.lang.String ontologyName, int distance, int[] relationTypes) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermChildren(termId, ontologyName, distance, relationTypes);
  }
  
  public java.util.HashMap getTermRelations(java.lang.String termId, java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermRelations(termId, ontologyName);
  }
  
  public java.util.HashMap getChildrenFromRoot(java.lang.String rootTermId, java.lang.String ontologyName, java.util.Vector childrenIds) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getChildrenFromRoot(rootTermId, ontologyName, childrenIds);
  }
  
  public boolean isObsolete(java.lang.String termId, java.lang.String ontologyName) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.isObsolete(termId, ontologyName);
  }
  
  public uk.ac.ebi.ook.web.model.DataHolder[] getTermsByAnnotationData(java.lang.String ontologyName, java.lang.String annotationType, java.lang.String strValue, double fromDblValue, double toDblValue) throws java.rmi.RemoteException{
    if (query == null)
      _initQueryProxy();
    return query.getTermsByAnnotationData(ontologyName, annotationType, strValue, fromDblValue, toDblValue);
  }
  
  
}