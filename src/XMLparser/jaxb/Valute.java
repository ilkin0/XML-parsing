package XMLparser.jaxb;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement(name = "Valute")
@XmlAccessorType(XmlAccessType.FIELD)
public class Valute {
    @XmlAttribute(name = "Code")
    private String code;

    @XmlElement(name = "Nominal")
    private String nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Value")
    private BigDecimal value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Valute{" +
                "code='" + code + '\'' +
                ", nominal='" + nominal + '\'' +
                ", name='" + name + '\'' +
                ", value=\n" + value +
                '}';
    }
}
