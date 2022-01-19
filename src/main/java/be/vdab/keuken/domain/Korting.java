package be.vdab.keuken.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Korting {
    private int vanafAantal;
    private BigDecimal percentage;

    public Korting(int vanafAantal, BigDecimal percentage) {
        this.vanafAantal = vanafAantal;
        this.percentage = percentage;
    }

    public Korting() {
    }

    public int getVanafAantal() {
        return vanafAantal;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }


    //equals en hashcode baseren op 1 attribuut is voldoende, _PER_ artikel zal er vanaf een bepaald aantal maar _een_ kortingspercentage toegewezen worden
    //equals controleert dus op "vanafAantal", er kunnen geen 2 verschillende "vanafAantal" zijn per artikel
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Korting)) return false;
        Korting korting = (Korting) o;
        return vanafAantal == korting.vanafAantal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vanafAantal);
    }

/*    @Override
    public boolean equals(Object object) {
        return object instanceof Korting korting &&
                vanafAantal == korting.vanafAantal;
    }
    @Override
    public int hashCode() {
        return vanafAantal;
    }*/

}
