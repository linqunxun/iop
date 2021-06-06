package com.iop.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 对接方式
 */
@ApiModel(description = "对接方式")
@Entity
@Table(name = "docking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Docking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @Column(name = "name")
    private String name;

    /**
     * 接入变量
     */
    @ApiModelProperty(value = "接入变量")
    @Column(name = "variable")
    private String variable;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Docking id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Docking name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariable() {
        return this.variable;
    }

    public Docking variable(String variable) {
        this.variable = variable;
        return this;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Docking)) {
            return false;
        }
        return id != null && id.equals(((Docking) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Docking{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", variable='" + getVariable() + "'" +
            "}";
    }
}
