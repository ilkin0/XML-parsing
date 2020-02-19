package XMLparser.jaxb;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValCurs {
    @XmlAttribute(name = "Date")
    private String date;

    @XmlAttribute(name = "Name")
    private String name;

    @XmlAttribute(name = "Description")
    private String description;

    @XmlElement(name = "ValType")
    private List<ValType> valTypeList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ValType> getValTypeList() {
        return valTypeList;
    }

    public void setValTypeList(List<ValType> valTypeList) {
        this.valTypeList = valTypeList;
    }

    @Override
    public String toString() {
        return "ValCurs{" +
                "date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", valTypeList=\n" + valTypeList +
                '}';
    }
}

// preparedStatement.setString(1, list.get(i).getValTypeList().get(i).getValuteList().get(i).getCode());
//         preparedStatement.setString(2, list.get(i).getValTypeList().get(i).getValuteList().get(i).getNominal());
//         preparedStatement.setString(3, list.get(i).getValTypeList().get(i).getValuteList().get(i).getName());
//         preparedStatement.setBigDecimal(4, list.get(i).getValTypeList().get(i).getValuteList().get(i).getValue());
