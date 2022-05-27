package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name="tb_category")
public class Category implements Serializable{
	/*Serializable - padrão para que o objeto possa ser convertido em sequencia de bytes,
	 *  para ser gravado em arquivos e passar em redes */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private String Name;
	
	/*Armazena o momento de criação da categoria com o padrão UTC (SEM ESPECIFICAR UMA TIMEZONE)"*/
	@Column(columnDefinition="TIMESTAMP WITHOUT TIME ZONE") /*Armazena o tempo de criação da categoria com*/
	private Instant createdAt;
	@Column(columnDefinition="TIMESTAMP WITHOUT TIME ZONE") /*Armazena o tempo de criação da categoria com*/
	private Instant updatedAt;
	
	public Category() {
		
	}

	public Category(Long id, String name) {
		this.id = id;
		Name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}
	@PrePersist /*Anotation fará esse código rodar precedente à criação do objeto*/
	public void prePersist() {
		createdAt = Instant.now();
	}
	@PreUpdate /*Anotation fará esse código rodar precedente ao update do objeto*/
	public void preUpdate() {
		updatedAt = Instant.now();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id);
	}
	
}
