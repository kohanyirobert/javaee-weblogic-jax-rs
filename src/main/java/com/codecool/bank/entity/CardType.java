package com.codecool.bank.entity;

import javax.persistence.*;

@Entity
@Table(name = "card_type")
@NamedQueries({
    @NamedQuery(name = "CardType.findByTypeName", query = "SELECT ct FROM CardType ct WHERE ct.typeName = :typeName")
})
public class CardType {

    @Id
    @GeneratedValue(generator = "CardTypeSequence")
    @SequenceGenerator(name = "CardTypeSequence", sequenceName = "card_type_sequence", allocationSize = 1)
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
