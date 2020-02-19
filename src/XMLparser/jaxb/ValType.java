package XMLparser.jaxb;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "ValType")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValType {
    @XmlAttribute(name = "Type")
    private String Type;

    @XmlElement(name = "Valute")
    private List<Valute> valuteList;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public List<Valute> getValuteList() {
        return valuteList;
    }

    public void setValuteList(List<Valute> valuteList) {
        this.valuteList = valuteList;
    }

    @Override
    public String toString() {
        return "ValType{" +
                "Type='" + Type + '\'' +
                ", valuteList=\n" + valuteList +
                '}';
    }
}
