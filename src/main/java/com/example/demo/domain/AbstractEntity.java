/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.domain;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Base class to derive entity classes from.
 * 
 */
@MappedSuperclass
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

//  @Column(nullable = false)
  private Date modificationTime;

//  @Column(nullable = false)
  private Date creationTime;
  
  @Version
  private long version = 0;
  
  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (this.id == null || obj == null
        || !(this.getClass().equals(obj.getClass()))) {
      return false;
    }

    AbstractEntity that = (AbstractEntity) obj;

    return this.id.equals(that.getId());
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }

  @PreUpdate
  public void preUpdate() {
    modificationTime = new Date();
  }

  @PrePersist
  public void prePersist() {
    Date now = new Date();
    creationTime = now;
    modificationTime = now;
  }

  /**
   * Returns the identifier of the entity.
   * 
   * @return the id
   */
  public Long getId() {
    return id;
  }

  public Date getModificationTime() {
    return modificationTime;
  }

  public long getVersion() {
    return version;
  }

  public Date getCreationTime() {
    return creationTime;
  }

}
