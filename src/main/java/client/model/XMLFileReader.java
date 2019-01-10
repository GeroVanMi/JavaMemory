package client.model;

import client.model.cards.CardPair;
import client.model.cards.ImageCard;
import client.model.cards.TextCard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLFileReader {
    private Document document;

    public XMLFileReader(String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(XMLFileReader.class.getResource(fileName).toURI().toString());
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e.getMessage());
        }
    }

    public String getValueOfSingleChild(Element elem, String tagName) {
        if(elem.getElementsByTagName(tagName).item(0) == null) {
            return null;
        }
        return elem.getElementsByTagName(tagName).item(0).getTextContent();
    }

    public CardPair[] getPairs() {
        NodeList nodeList = document.getElementsByTagName("pair");
        CardPair[] pairs = new CardPair[nodeList.getLength()];
        for(int i = 0; i < nodeList.getLength(); i++) {
            Element pair = (Element)nodeList.item(i);
            String title = getValueOfSingleChild(pair, "title");
            TextCard textCard = new TextCard(title, getValueOfSingleChild(pair, "text"));
            ImageCard imageCard = new ImageCard(title, getValueOfSingleChild(pair, "path"));
            pairs[i] = new CardPair(title, imageCard, textCard);
        }
        return pairs;
    }
}
