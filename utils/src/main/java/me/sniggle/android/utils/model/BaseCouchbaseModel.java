package me.sniggle.android.utils.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by iulius on 04/03/16.
 */
public abstract class BaseCouchbaseModel {

  @JsonProperty("_id")
  private String id;
  @JsonProperty("_rev")
  private String revision;
  @JsonProperty("document_type")
  private String documentType;

  protected BaseCouchbaseModel(String documentType) {
    this.documentType = documentType;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRevision() {
    return revision;
  }

  public void setRevision(String revision) {
    this.revision = revision;
  }
}
