package com.codecool.bank.entity;

import javax.persistence.*;

@Entity
@Table(name = "contact_type")
@NamedQueries({
    @NamedQuery(name = "ContactType.findByTypeName", query = "SELECT ct FROM ContactType ct WHERE ct.typeName = :typeName")
})
public class ContactType {

    @Id
    @GeneratedValue(generator = "ContactTypeSequence")
    @SequenceGenerator(name = "ContactTypeSequence", sequenceName = "contact_type_sequence", allocationSize = 1)
    private int id;

    @Column(name = "type_name", length = 255, unique = true, nullable = false)
    private String typeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
