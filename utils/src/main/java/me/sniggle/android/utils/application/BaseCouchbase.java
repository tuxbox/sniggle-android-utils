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
 * Created by iulius on 04/03/16.
 */
public class BaseCouchbase {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Database database;
  private final Bus bus;

  protected BaseCouchbase(Bus bus, Manager databaseManager, String databaseName) throws CouchbaseLiteException {
    this.bus = bus;
    this.database = databaseManager.getDatabase(databaseName);
  }

  protected void publishEvent(Object event) {
    if( bus != null ) {
      bus.post(event);
    }
  }

  protected Query createQueryForView(String viewName, Mapper mapper, String version) {
    View view = database.getView(viewName);
    view.setMap(mapper, version);
    return view.createQuery();
  }

  protected Query createQueryForView(String viewName, Mapper mapper, Reducer reducer, String version) {
    View view = database.getView(viewName);
    view.setMapReduce(mapper, reducer, version);
    return view.createQuery();
  }

  protected Map<String,Object> convertJsonModelToMap(Object source) {
    return objectMapper.convertValue(source, Map.class);
  }

  protected <T> T convertDocumentToModel(Document document, Class<T> modelClass) {
    return objectMapper.convertValue(document.getProperties(), modelClass);
  }


  public Document createDocument() {
    return (database == null) ? null : database.createDocument();
  }

  public Document getDocumentById(String documentId) {
    return (database == null) ? null : database.getExistingDocument(documentId);
  }

  public <T> T getDocument(String documentId, Class<T> targetType) {
    Document document = getDocumentById(documentId);
    return (document == null) ? null : objectMapper.convertValue(document.getProperties(), targetType);
  }

  public boolean updateDocument(String documentId, Map<String, Object> updatedProperties) {
    return updateDocument(database.getDocument(documentId), updatedProperties);
  }

  public boolean updateDocument(String documentId, Object object) {
    return updateDocument(documentId, objectMapper.convertValue(object, Map.class));
  }

  public boolean updateDocument(Document currentDocument, Object object) {
    return updateDocument(currentDocument, objectMapper.convertValue(object, Map.class));
  }

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

  public boolean deleteDocument(String documentId) {
    return deleteDocument(getDocumentById(documentId));
  }

  public boolean deleteDocument(Document document) {
    boolean result = true;
    try {
      result &= document.delete();
    } catch (CouchbaseLiteException e) {
      result &= false;
    }
    return result;
  }

  public boolean addAttachment(String documentId, String name, String mimeType, InputStream inputStream) {
    return addAttachment(getDocumentById(documentId), name, mimeType, inputStream);
  }

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

  public Attachment getAttachment(String documentId, String attachmentName) {
    return getAttachment(getDocumentById(documentId), attachmentName);
  }

  public Attachment getAttachment(Document document, String attachmentName) {
    SavedRevision savedRevision = document.getCurrentRevision();
    return savedRevision.getAttachment(attachmentName);
  }

  public void close() {
    database.close();
  }

}
