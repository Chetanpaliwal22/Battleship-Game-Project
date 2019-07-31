package model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import constants.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tools.Coordinate;
import view.FrontEndShip;
import view.MainWindow;

public class DataManager {

    public static final String xmlFilePath = "src/model/data/GameData.xml";

    private static Element coordinateElement, xElement, yElement, stateElement;

    public void load() {
        try {
            File inputFile = new File(xmlFilePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;

            documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(inputFile);
            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();


            int[][] targetBoardState = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            NodeList nodeList;

            nodeList = (NodeList) xPath.compile("/data").evaluate(document, XPathConstants.NODESET);

            Node node;

            for (int i = 0; i < 1; i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

//                    System.out.print("\nUser name: " + element.getElementsByTagName("userName").item(0).getTextContent() + "\n");
//                    System.out.print("\nScore: " + element.getElementsByTagName("score").item(0).getTextContent() + "\n");
//                    System.out.print("\nAction: " + element.getElementsByTagName("action").item(0).getTextContent() + "\n");
//                    System.out.print("\nAction: " + element.getElementsByTagName("numberOfAISunkShips").item(0).getTextContent() + "\n");

                    MainWindow.numberOfAISunkShips = Integer.parseInt(element.getElementsByTagName("numberOfAISunkShips").item(0).getTextContent());
                }
            }


            nodeList = (NodeList) xPath.compile("/data/playerShips/ship").evaluate(document, XPathConstants.NODESET);

            MainWindow.shipList = new ArrayList<FrontEndShip>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

//                System.out.println("\nCurrent Element: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

//                    System.out.println("??????-----------------------");
//
//                    System.out.println("size: " + element.getElementsByTagName("size").item(0).getTextContent());
//
//                    System.out.println("direction: " + element.getElementsByTagName("direction").item(0).getTextContent());
//
//                    System.out.println("sunk: " + element.getElementsByTagName("sunk").item(0).getTextContent());
//
//                    System.out.println("pivotX: " + element.getElementsByTagName("pivotX").item(0).getTextContent());
//                    System.out.println("pivotY: " + element.getElementsByTagName("pivotY").item(0).getTextContent());

                    MainWindow.shipList.add(new FrontEndShip(Integer.parseInt(element.getElementsByTagName("size").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("direction").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("pivotX").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("pivotY").item(0).getTextContent())));

                    if (element.getElementsByTagName("sunk").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                        MainWindow.shipList.get(MainWindow.shipList.size() - 1).sunk = true;
                    else
                        MainWindow.shipList.get(MainWindow.shipList.size() - 1).sunk = false;

//                    System.out.println("x: " + element.getElementsByTagName("x").item(0).getTextContent());
//                    System.out.println("y: " + element.getElementsByTagName("y").item(0).getTextContent());


//                    System.out.println("state : " + element.getElementsByTagName("state").item(0).getTextContent());
                }
            }

            for (int i = 0; i < MainWindow.shipList.size(); i++) {

                Coordinate[] CoordinateArray = new Coordinate[MainWindow.shipList.get(i).size];

                for (int j = 0; j < MainWindow.shipList.get(i).occupiedGridX.size(); j++) {
                    CoordinateArray[j] = new Coordinate(MainWindow.shipList.get(i).occupiedGridX.get(j), MainWindow.shipList.get(i).occupiedGridY.get(j));
                }

                try {
                    MainWindow.humanBoard.placeShip(CoordinateArray);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            nodeList = (NodeList) xPath.compile("/data/AIShips/ship").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int size = Integer.parseInt(element.getElementsByTagName("size").item(0).getTextContent());

                    Coordinate[] ship1Coordinate = new Coordinate[size];

                    for (int j = 0; j < size; j++) {
                        ship1Coordinate[j] = new Coordinate(Integer.parseInt(element.getElementsByTagName("x").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("y").item(j).getTextContent()));

//                        System.out.println("???: " + ship1Coordinate[j].x + ", " + ship1Coordinate[j].y);
                    }

                    MainWindow.AIBoard.placeShip(ship1Coordinate);


//                    System.out.println("size: " + element.getElementsByTagName("size").item(0).getTextContent());
//                    System.out.println("x: " + element.getElementsByTagName("x").item(0).getTextContent());
//                    System.out.println("y: " + element.getElementsByTagName("y").item(0).getTextContent());
                }
            }


            nodeList = (NodeList) xPath.compile("/data/humanBoardState/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

//                System.out.println("\nCurrent Element: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetBoardState[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("state").item(0).getTextContent());

//                    System.out.println("x : " + element.getElementsByTagName("x").item(0).getTextContent());
//                    System.out.println("y : " + element.getElementsByTagName("y").item(0).getTextContent());
//                    System.out.println("state : " + element.getElementsByTagName("state").item(0).getTextContent());
                }
            }

            MainWindow.humanBoard.setBoardState(targetBoardState);

//            System.out.println("----------------------------");
//            System.out.println("AI board");

            targetBoardState = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            nodeList = (NodeList) xPath.compile("/data/AIBoardState/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetBoardState[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("state").item(0).getTextContent());

//                    System.out.println("x : " + element.getElementsByTagName("x").item(0).getTextContent());
//                    System.out.println("y : " + element.getElementsByTagName("y").item(0).getTextContent());
//                    System.out.println("state : " + element.getElementsByTagName("state").item(0).getTextContent());
                }
            }

            MainWindow.AIBoard.setBoardState(targetBoardState);

            targetBoardState = null;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // Root element
            Element root = document.createElement("data");
            document.appendChild(root);

            // User name element
            Element userNameElement = document.createElement("userName");
            userNameElement.appendChild(document.createTextNode("???"));
            root.appendChild(userNameElement);

            // Score element
            Element scoreElement = document.createElement("score");
            scoreElement.appendChild(document.createTextNode("100"));
            root.appendChild(scoreElement);

            // Score element
            Element numberOfAISunkShipsElement = document.createElement("numberOfAISunkShips");
            numberOfAISunkShipsElement.appendChild(document.createTextNode(MainWindow.numberOfAISunkShips + ""));
            root.appendChild(numberOfAISunkShipsElement);

            // Player ships element
            Element playerShipsElement = document.createElement("playerShips");
            root.appendChild(playerShipsElement);

            for (int i = 0; i < MainWindow.shipList.size(); i++) {

                // Ship element
                Element shipElement = document.createElement("ship");
                playerShipsElement.appendChild(shipElement);

                for (int j = 0; j < MainWindow.shipList.get(i).occupiedGridX.size(); j++) {

                    // Coordinate element
                    coordinateElement = document.createElement("coordinate");
                    shipElement.appendChild(coordinateElement);

                    // X element
                    xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).occupiedGridX.get(j) + ""));
                    coordinateElement.appendChild(xElement);

                    // Y element
                    yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).occupiedGridY.get(j) + ""));
                    coordinateElement.appendChild(yElement);
                }

                // Size element
                Element sizeElement = document.createElement("size");
                sizeElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).size + ""));
                shipElement.appendChild(sizeElement);

                // Pivot element
                Element pivotElement = document.createElement("pivot");
                shipElement.appendChild(pivotElement);

                // X element
                xElement = document.createElement("pivotX");
                xElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).pivotGridX + ""));
                pivotElement.appendChild(xElement);

                // Y element
                yElement = document.createElement("pivotY");
                yElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).pivotGridY + ""));
                pivotElement.appendChild(yElement);

                // Direction element
                Element directionElement = document.createElement("direction");
                directionElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).direction + ""));
                shipElement.appendChild(directionElement);

                // Sunk element
                Element sunkElement = document.createElement("sunk");
                sunkElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).sunk + ""));
                shipElement.appendChild(sunkElement);
            }


            // AI ships element
            Element AIShipsElement = document.createElement("AIShips");
            root.appendChild(AIShipsElement);

            ArrayList<Ship> ships = MainWindow.AIBoard.getShips();

            for (int i = 0; i < ships.size(); i++) {

                // Ship element
                Element shipElement = document.createElement("ship");
                AIShipsElement.appendChild(shipElement);

                // Size element
                Element sizeElement = document.createElement("size");
                sizeElement.appendChild(document.createTextNode(ships.get(i).size + ""));
                shipElement.appendChild(sizeElement);

                ArrayList<Coordinate> shipCoordinates = ships.get(i).getPosition();

                for (int j = 0; j < shipCoordinates.size(); j++) {

                    // Coordinate element
                    coordinateElement = document.createElement("coordinate");
                    shipElement.appendChild(coordinateElement);

                    // X element
                    xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(shipCoordinates.get(j).x + ""));
                    coordinateElement.appendChild(xElement);

                    // Y element
                    yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(shipCoordinates.get(j).y + ""));
                    coordinateElement.appendChild(yElement);
                }
            }

            // Human board state element
            Element humanBoardStateElement = document.createElement("humanBoardState");
            root.appendChild(humanBoardStateElement);

            int[][] targetBoardState = MainWindow.humanBoard.getBoardState();

            for (int i = 0; i < targetBoardState.length; i++) {
                for (int j = 0; j < targetBoardState[0].length; j++) {

                    // Coordinate element
                    coordinateElement = document.createElement("coordinate");
                    humanBoardStateElement.appendChild(coordinateElement);

                    // X element
                    xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(i + ""));
                    coordinateElement.appendChild(xElement);

                    // Y element
                    yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(j + ""));
                    coordinateElement.appendChild(yElement);

                    // State element
                    stateElement = document.createElement("state");
                    stateElement.appendChild(document.createTextNode(targetBoardState[i][j] + ""));
                    coordinateElement.appendChild(stateElement);
                }
            }

            targetBoardState = null;

            // AI board state element
            Element AIBoardStateElement = document.createElement("AIBoardState");
            root.appendChild(AIBoardStateElement);

            targetBoardState = MainWindow.AIBoard.getBoardState();

            for (int i = 0; i < targetBoardState.length; i++) {
                for (int j = 0; j < targetBoardState[0].length; j++) {

                    // Coordinate element
                    coordinateElement = document.createElement("coordinate");
                    AIBoardStateElement.appendChild(coordinateElement);

                    // X element
                    xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(i + ""));
                    coordinateElement.appendChild(xElement);

                    // Y element
                    yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(j + ""));
                    coordinateElement.appendChild(yElement);

                    // State element
                    stateElement = document.createElement("state");
                    stateElement.appendChild(document.createTextNode(targetBoardState[i][j] + ""));
                    coordinateElement.appendChild(stateElement);
                }
            }

            targetBoardState = null;

            // create the xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Transform the DOM Object to an XML File
            DOMSource domSource = new DOMSource(document);

            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            transformer.transform(domSource, streamResult);
        } catch (
                ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (
                TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
