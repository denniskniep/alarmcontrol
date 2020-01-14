package com.alarmcontrol.server.data.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrganisationConfiguration {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Long organisationId;
  private String key;
  @Column(columnDefinition="clob")
  private String value;

  protected OrganisationConfiguration() {
  }

  public OrganisationConfiguration(Long organisationId, String key, String value) {
    this.organisationId = organisationId;
    this.key = key;
    this.value = value;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOrganisationId() {
    return organisationId;
  }

  public void setOrganisationId(Long organisationId) {
    this.organisationId = organisationId;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
/*
  public ArrayList<RuleContainerData> deserialize(){
    RuleContainerDataCollection ruleContainerDataCollection;
    try {
      ruleContainerDataCollection = new ObjectMapper().readValue(getValue(), RuleContainerDataCollection.class);
    } catch (IOException e) {
      throw new RuntimeException("Error during 'RuleContainerDataCollection' deserialisation for AAO", e);
    }
    return ruleContainerDataCollection.getRuleContainers();
  }*/
}

