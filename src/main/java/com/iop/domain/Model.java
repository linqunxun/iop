package com.iop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 型号
 */
@ApiModel(description = "型号")
@Entity
@Table(name = "model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Model implements Serializable {

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
     * 代号
     */
    @ApiModelProperty(value = "代号")
    @Column(name = "code_name")
    private String codeName;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    @ManyToOne
    private Brand brand;

    /**
     * 对接方式
     */
    @ApiModelProperty(value = "对接方式")
    @ManyToOne
    private Docking docking;

    /**
     * 场景列表
     */
    @ApiModelProperty(value = "场景列表")
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_model__scenes",
        joinColumns = @JoinColumn(name = "model_id"),
        inverseJoinColumns = @JoinColumn(name = "scenes_id")
    )
    @JsonIgnoreProperties(value = { "models" }, allowSetters = true)
    private Set<Scenes> scenes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Model id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Model name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeName() {
        return this.codeName;
    }

    public Model codeName(String codeName) {
        this.codeName = codeName;
        return this;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public Model brand(Brand brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Docking getDocking() {
        return this.docking;
    }

    public Model docking(Docking docking) {
        this.setDocking(docking);
        return this;
    }

    public void setDocking(Docking docking) {
        this.docking = docking;
    }

    public Set<Scenes> getScenes() {
        return this.scenes;
    }

    public Model scenes(Set<Scenes> scenes) {
        this.setScenes(scenes);
        return this;
    }

    public Model addScenes(Scenes scenes) {
        this.scenes.add(scenes);
        scenes.getModels().add(this);
        return this;
    }

    public Model removeScenes(Scenes scenes) {
        this.scenes.remove(scenes);
        scenes.getModels().remove(this);
        return this;
    }

    public void setScenes(Set<Scenes> scenes) {
        this.scenes = scenes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Model)) {
            return false;
        }
        return id != null && id.equals(((Model) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Model{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", codeName='" + getCodeName() + "'" +
            "}";
    }
}
