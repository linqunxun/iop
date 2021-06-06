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
 * 场景
 */
@ApiModel(description = "场景")
@Entity
@Table(name = "scenes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Scenes implements Serializable {

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
     * 封面图片地址
     */
    @ApiModelProperty(value = "封面图片地址")
    @Column(name = "cover")
    private String cover;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Column(name = "jhi_desc")
    private String desc;

    /**
     * 标准数据规范
     */
    @ApiModelProperty(value = "标准数据规范")
    @Column(name = "data_spec")
    private String dataSpec;

    @ManyToMany(mappedBy = "scenes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "brand", "docking", "scenes" }, allowSetters = true)
    private Set<Model> models = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Scenes id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Scenes name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return this.cover;
    }

    public Scenes cover(String cover) {
        this.cover = cover;
        return this;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDesc() {
        return this.desc;
    }

    public Scenes desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDataSpec() {
        return this.dataSpec;
    }

    public Scenes dataSpec(String dataSpec) {
        this.dataSpec = dataSpec;
        return this;
    }

    public void setDataSpec(String dataSpec) {
        this.dataSpec = dataSpec;
    }

    public Set<Model> getModels() {
        return this.models;
    }

    public Scenes models(Set<Model> models) {
        this.setModels(models);
        return this;
    }

    public Scenes addModel(Model model) {
        this.models.add(model);
        model.getScenes().add(this);
        return this;
    }

    public Scenes removeModel(Model model) {
        this.models.remove(model);
        model.getScenes().remove(this);
        return this;
    }

    public void setModels(Set<Model> models) {
        if (this.models != null) {
            this.models.forEach(i -> i.removeScenes(this));
        }
        if (models != null) {
            models.forEach(i -> i.addScenes(this));
        }
        this.models = models;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Scenes)) {
            return false;
        }
        return id != null && id.equals(((Scenes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Scenes{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cover='" + getCover() + "'" +
            ", desc='" + getDesc() + "'" +
            ", dataSpec='" + getDataSpec() + "'" +
            "}";
    }
}
