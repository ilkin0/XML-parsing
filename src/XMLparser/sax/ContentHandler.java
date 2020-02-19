package XMLparser.sax;

import XMLparser.domain.Currencies;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ContentHandler extends DefaultHandler {
    private List<Currencies> currencyList;
    private Currencies tempCurrency;

    private boolean isCurrencies;
    private boolean isCode;
    private boolean isNominal;
    private boolean isName;
    private boolean isValue;

    @Override
    public void startDocument() throws SAXException {

        this.currencyList = new ArrayList<>();

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (qName.equals("Valute")){
            tempCurrency = new Currencies();
            isCurrencies = true;

            String code = attributes.getValue("Code");
            tempCurrency.setCode(code);
        }else if(qName.equals("Nominal")){
            isNominal = true;
        }else if(qName.equals("Name")){
            isName = true;
        }else if(qName.equals("Value")){
            isValue = true;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String data = new String(ch, start, length);

        if (isCode){
            tempCurrency.setCode(data);
        }else if(isNominal){
            tempCurrency.setNominal(data);
        }else if(isName){
            tempCurrency.setName(data);
        }else if (isValue){
            tempCurrency.setValue(new BigDecimal(data));
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (qName.equals("Valute")){
            isCurrencies = false;
            currencyList.add(tempCurrency);
            tempCurrency = null;
        }else if (qName.equals("Code")){
            isCode = false;
        }else if (qName.equals("Nominal")){
            isNominal = false;
        }else if (qName.equals("Name")){
            isName = false;
        }else if (qName.equals("Value")){
            isValue = false;
        }

    }


    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public List<Currencies> getCurrencyList() {
        return currencyList;
    }
}
