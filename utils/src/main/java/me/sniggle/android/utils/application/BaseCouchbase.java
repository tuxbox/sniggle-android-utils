package me.sniggle.android.utils.application;

import com.couchbase.lite.Attachment;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.Reducer;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.View;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.otto.Bus;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A convenience wrapper for Couchbase access and model conversion
 */
public class BaseCouchbase {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Database database;
  private final Bus bus;

  /**
   *
   * @param bus
   *  the app's event bus
   * @param databaseManager
   *  the couchbase manager
   * @param databaseName
   *  the database to use
   * @throws CouchbaseLiteException
   */
  protected BaseCouchbase(Bus bus, Manager databaseManager, String databaseName) throws CouchbaseLiteException {
    this.bus = bus;
    this.database = databaseManager.getDatabase(databaseName);
  }

  /**
   * publishes and event to the app's event bus
   *
   * @param event
   *  the event to be published
   */
  protected void publishEvent(Object event) {
    if( bus != null ) {
      bus.post(event);
    }
  }

  /**
   * generic query creation method with a mapper only
   *
   * @param viewName
   *  the name of the view
   * @param mapper
   *  the mapper to be used
   * @param version
   *  the version of the view
   * @return the query
   */
  protected Query createQueryForView(String viewName, Mapper mapper, String version) {
    View view = database.getView(viewName);
    view.setMap(mapper, version);
    return view.createQuery();
  }

  /**
   * generic query creation method with a mapper and reducer
   *
   * @param viewName
   *  the name of the view
   * @param mapper
   *  the mapper to be used
   * @param reducer
   *  the reducer to be used
   * @param version
   *  the version of the view
   * @return the query
   */
  protected Query createQueryForView(String viewName, Mapper mapper, Reducer reducer, String version) {
    View view = database.getView(viewName);
    view.setMapReduce(mapper, reducer, version);
    return view.createQuery();
  }

  /**
   * converts an object graph to a Map
   *
   * @param source
   *  the source object
   * @return the corresponding map
   */
  protected Map<String,Object> convertJsonModelToMap(Object source) {
    return objectMapper.convertValue(source, Map.class);
  }

  /**
   * converts a Couchbase document to a typed Object
   *
   * @param document
   *  the source Couchbase document
   * @param modelClass
   *  the target model class
   * @param <T>
   *  the target type
   * @return the object representation of the document
   */
  protected <T> T convertDocumentToModel(Document document, Class<T> modelClass) {
    return objectMapper.convertValue(document.getProperties(), modelClass);
  }


  /**
   * creates a new document
   *
   * @return the new document or null
   */
  public Document createDocument() {
    return (database == null) ? null : database.createDocument();
  }

  /**
   * returns a document by it's id
   *
   * @param documentId
   *  the document id
   * @return the matching document
   */
  public Document getDocumentById(String documentId) {
    return (database == null) ? null : database.getExistingDocument(documentId);
  }

  /**
   * returns a document as a specific Object
   *
   * @param documentId
   *  the document id
   * @param targetType
   *  the target type
   * @param <T>
   *  the target type
   * @return the matching document as object
   */
  public <T> T getDocument(String documentId, Class<T> targetType) {
    Document document = getDocumentById(documentId);
    return (document == null) ? null : objectMapper.convertValue(document.getProperties(), targetType);
  }

  /**
   * updates the document identified by this id with the new properties
   *
   * @param documentId
   *  the target document's id
   * @param updatedProperties
   *  the new source properties
   * @return true if update was successful
   */
  public boolean updateDocument(String documentId, Map<String, Object> updatedProperties) {
    return updateDocument(database.getDocument(documentId), updatedProperties);
  }

  /**
   * updates the document identified by this id with the new properties
   *
   * @param documentId
   *  the target document's id
   * @param object
   *  the new source object
   * @return true if update was successful
   */
  public boolean updateDocument(String documentId, Object object) {
    return updateDocument(documentId, objectMapper.convertValue(object, Map.class));
  }

  /**
   * updates the document identified by this id with the new properties
   *
   * @param currentDocument
   *  the current document
   * @param object
   *  the new values as typed Object
   * @return true if update was successful
   */
  public boolean updateDocument(Document currentDocument, Object object) {
    return updateDocument(currentDocument, objectMapper.convertValue(object, Map.class));
  }

  /**
   * updates the document identified by this id with the new properties
   *
   * @param currentDocument
   *  the current document
   * @param updatedProperties
   *  the new source properties
   * @return true if update was successful
   */
  public boolean updateDocument(Document currentDocument, Map<String, Object> updatedProperties) {
    boolean result = true;
    Map<String, Object> currentProperties = new HashMap<>();
    if( currentDocument.getProperties() != null ) {
      currentProperties.putAll(currentDocument.getProperties());
    }
    currentProperties.putAll(updatedProperties);
    try {
      currentDocument.putProperties(currentProperties);
    } catch (CouchbaseLiteException e) {
      result &= false;
    }
    return result;
  }

  /**
   * deletes the document with the given id
   *
   * @param documentId
   *  the document's id
   * @return true if successful
   */
  public boolean deleteDocument(String documentId) {
    return deleteDocument(getDocumentById(documentId));
  }

  /**
   * deletes the provided document
   *
   * @param document
   *  the current document
   * @return true if successful
   */
  public boolean deleteDocument(Document document) {
    boolean result = true;
    try {
      result &= document.delete();
    } catch (CouchbaseLiteException e) {
      result &= false;
    }
    return result;
  }

  /**
   * adds an attachment to an existing document
   *
   * @param documentId
   *  the document's id
   * @param name
   *  the name of the attachment
   * @param mimeType
   *  the mime type of the attachment
   * @param inputStream
   *  the source data stream
   * @return true if successful
   */
  public boolean addAttachment(String documentId, String name, String mimeType, InputStream inputStream) {
    return addAttachment(getDocumentById(documentId), name, mimeType, inputStream);
  }

  /**
   * adds an attachment to an existing document
   *
   * @param document
   *  the target document
   * @param name
   *  the name of the attachment
   * @param mimeType
   *  the mime type of the attachment
   * @param inputStream
   *  the source data stream
   * @return true if successful
   */
  public boolean addAttachment(Document document, String name, String mimeType, InputStream inputStream) {
    boolean result = true;
    try {
      UnsavedRevision newRevision = document.getCurrentRevision().createRevision();
      newRevision.setAttachment(name, mimeType, inputStream);
      newRevision.save();
    } catch (CouchbaseLiteException e) {
      result &= false;
    }
    return result;
  }

  /**
   * retrieves an attachment from an existing document
   *
   * @param documentId
   *  the document's id
   * @param attachmentName
   *  the name of the attachment
   * @return the matching attachment
   */
  public Attachment getAttachment(String documentId, String attachmentName) {
    return getAttachment(getDocumentById(documentId), attachmentName);
  }

  /**
   * retrieves an attachment from an existing document
   *
   * @param document
   *  the document
   * @param attachmentName
   *  the name of the attachment
   * @return the matching attachment
   */
  public Attachment getAttachment(Document document, String attachmentName) {
    SavedRevision savedRevision = document.getCurrentRevision();
    return savedRevision.getAttachment(attachmentName);
  }

  /**
   * closes the connection to the database
   */
  public void close() {
    database.close();
  }

}
