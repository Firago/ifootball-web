package com.mobica.ifootball.domain;


import com.mobica.ifootball.domain.enumeration.ParameterKey;
import com.mobica.ifootball.domain.enumeration.ParameterType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Parameter.
 */
@Entity
@Table(name = "parameter")
public class Parameter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "key", nullable = false)
    private ParameterKey key;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ParameterType type;

    @Column(name = "value")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParameterKey getKey() {
        return key;
    }

    public void setKey(ParameterKey key) {
        this.key = key;
    }

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parameter parameter = (Parameter) o;
        if (parameter.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, parameter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Parameter{" +
            "id=" + id +
            ", key='" + key + "'" +
            ", type='" + type + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
