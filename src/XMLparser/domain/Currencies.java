package XMLparser.domain;
import java.math.BigDecimal;


public class Currencies {
    private String Code;
    private String Nominal;
    private String Name;
    private BigDecimal Value;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getNominal() {
        return Nominal;
    }

    public void setNominal(String nominal) {
        this.Nominal = nominal;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public BigDecimal getValue() {
        return Value;
    }

    public void setValue(BigDecimal value) {
        this.Value = value;
    }

    @Override
    public String toString() {
        return "Currencies{" +
                "Code='" + Code + '\'' +
                ", Nominal='" + Nominal + '\'' +
                ", Name='" + Name + '\'' +
                ", Value=" + Value +
                '}';
    }
}
