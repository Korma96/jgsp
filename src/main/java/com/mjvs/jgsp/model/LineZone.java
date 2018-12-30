package com.mjvs.jgsp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Objects;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class LineZone extends EntityForDeleted {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    protected Long id;

    protected LineZone() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract Zone getZone();

	protected abstract double getPrice(PriceTicket priceTicket);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineZone lineZone = (LineZone) o;
        boolean retValue = Objects.equals(id, lineZone.id);

        if((o instanceof Line) && (this instanceof Line)) return ((Line) o).equals((Line)this) &&retValue;

        if((o instanceof Zone) && (this instanceof Zone)) return ((Zone) o).equals((Zone)this) && retValue;


        return retValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
