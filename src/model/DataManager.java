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


            nodeList = (NodeList) xPath.compile("/data/frontEndShipsElement/ship").evaluate(document, XPathConstants.NODESET);

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

            // update the ships on human board
            for (int i = 0; i < MainWindow.shipList.size(); i++) {

                Coordinate[] coordinateArray = new Coordinate[MainWindow.shipList.get(i).size];

                for (int j = 0; j < MainWindow.shipList.get(i).occupiedGridX.size(); j++) {
                    coordinateArray[j] = new Coordinate(MainWindow.shipList.get(i).occupiedGridX.get(j), MainWindow.shipList.get(i).occupiedGridY.get(j));
                }

                try {
                    MainWindow.humanBoard.placeShip(coordinateArray);
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

                    Coordinate[] shipCoordinates = new Coordinate[size];

                    for (int j = 0; j < size; j++) {
                        shipCoordinates[j] = new Coordinate(Integer.parseInt(element.getElementsByTagName("positionX").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("positionY").item(j).getTextContent()));

//                        System.out.println("???: " + ship1Coordinate[j].x + ", " + ship1Coordinate[j].y);
                    }

                    MainWindow.AIBoard.placeShip(shipCoordinates);


//                    System.out.println("size: " + element.getElementsByTagName("size").item(0).getTextContent());
//                    System.out.println("x: " + element.getElementsByTagName("x").item(0).getTextContent());
//                    System.out.println("y: " + element.getElementsByTagName("y").item(0).getTextContent());
                }
            }


            nodeList = (NodeList) xPath.compile("/data/humanBoard/waterGridState/coordinate").evaluate(document, XPathConstants.NODESET);

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

            nodeList = (NodeList) xPath.compile("/data/AIBoard/waterGridState/coordinate").evaluate(document, XPathConstants.NODESET);

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

            // root element
            Element root = document.createElement("data");
            document.appendChild(root);

            // userName element
            Element userNameElement = document.createElement("userName");
            userNameElement.appendChild(document.createTextNode("???"));
            root.appendChild(userNameElement);

            // score element
            Element scoreElement = document.createElement("score");
            scoreElement.appendChild(document.createTextNode("100"));
            root.appendChild(scoreElement);

            // numberOfAISunkShips element
            Element numberOfAISunkShipsElement = document.createElement("numberOfAISunkShips");
            numberOfAISunkShipsElement.appendChild(document.createTextNode(MainWindow.numberOfAISunkShips + ""));
            root.appendChild(numberOfAISunkShipsElement);

            // front end ships element
            Element frontEndShipsElement = document.createElement("frontEndShips");
            root.appendChild(frontEndShipsElement);

            for (int i = 0; i < MainWindow.shipList.size(); i++) {

                // ship element
                Element shipElement = document.createElement("ship");
                frontEndShipsElement.appendChild(shipElement);

                for (int j = 0; j < MainWindow.shipList.get(i).occupiedGridX.size(); j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    shipElement.appendChild(coordinateElement);

                    // x element
                    Element xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).occupiedGridX.get(j) + ""));
                    coordinateElement.appendChild(xElement);

                    // y element
                    Element yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).occupiedGridY.get(j) + ""));
                    coordinateElement.appendChild(yElement);
                }

                // size element
                Element sizeElement = document.createElement("size");
                sizeElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).size + ""));
                shipElement.appendChild(sizeElement);

                // pivot element
                Element pivotElement = document.createElement("pivot");
                shipElement.appendChild(pivotElement);

                // X element
                Element xElement = document.createElement("pivotX");
                xElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).pivotGridX + ""));
                pivotElement.appendChild(xElement);

                // Y element
                Element yElement = document.createElement("pivotY");
                yElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).pivotGridY + ""));
                pivotElement.appendChild(yElement);

                // direction element
                Element directionElement = document.createElement("direction");
                directionElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).direction + ""));
                shipElement.appendChild(directionElement);

                // sunk element
                Element sunkElement = document.createElement("sunk");
                sunkElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).sunk + ""));
                shipElement.appendChild(sunkElement);
            }


            // playerShips element
            Element playerShipsElement = document.createElement("playerShips");
            root.appendChild(playerShipsElement);

            ArrayList<Ship> playerShips = MainWindow.humanBoard.getShips();

            for (int i = 0; i < playerShips.size(); i++) {

                // ship element
                Element shipElement = document.createElement("ship");
                playerShipsElement.appendChild(shipElement);

                ArrayList<Coordinate> shipCoordinates = playerShips.get(i).getPosition();

                for (int j = 0; j < shipCoordinates.size(); j++) {
                    // position element
                    Element positionElement = document.createElement("position");
                    shipElement.appendChild(positionElement);

                    // position x element
                    Element positionXElement = document.createElement("positionX");
                    positionXElement.appendChild(document.createTextNode(shipCoordinates.get(j).x + ""));
                    positionElement.appendChild(positionXElement);

                    // position y element
                    Element positionYElement = document.createElement("positionY");
                    positionYElement.appendChild(document.createTextNode(shipCoordinates.get(j).y + ""));
                    positionElement.appendChild(positionYElement);
                }

                ArrayList<Coordinate> shipHitPositions = playerShips.get(i).getHitPosition();

                // hitPositionSize element
                Element hitPositionSizeElement = document.createElement("hitPositionSize");
                hitPositionSizeElement.appendChild(document.createTextNode(shipHitPositions.size() + ""));
                shipElement.appendChild(hitPositionSizeElement);

                for (int k = 0; k < shipHitPositions.size(); k++) {
                    // hitPosition element
                    Element hitPositionElement = document.createElement("hitPosition");
                    shipElement.appendChild(hitPositionElement);

                    // position x element
                    Element positionXElement = document.createElement("hitPositionX");
                    positionXElement.appendChild(document.createTextNode(shipHitPositions.get(k).x + ""));
                    hitPositionElement.appendChild(positionXElement);

                    // position y element
                    Element positionYElement = document.createElement("hitPositionY");
                    positionYElement.appendChild(document.createTextNode(shipHitPositions.get(k).y + ""));
                    hitPositionElement.appendChild(positionYElement);
                }

                // size element
                Element sizeElement = document.createElement("size");
                sizeElement.appendChild(document.createTextNode(playerShips.get(i).size + ""));
                shipElement.appendChild(sizeElement);

                // isAlive element
                Element isAliveElement = document.createElement("isAlive");
                isAliveElement.appendChild(document.createTextNode(playerShips.get(i).getIsAlive() + ""));
                shipElement.appendChild(isAliveElement);
            }


            // AIShips element
            Element AIShipsElement = document.createElement("AIShips");
            root.appendChild(AIShipsElement);

            ArrayList<Ship> AIShips = MainWindow.AIBoard.getShips();

            for (int i = 0; i < AIShips.size(); i++) {

                // ship element
                Element shipElement = document.createElement("ship");
                AIShipsElement.appendChild(shipElement);

                ArrayList<Coordinate> shipCoordinates = AIShips.get(i).getPosition();

                for (int j = 0; j < shipCoordinates.size(); j++) {
                    // position element
                    Element positionElement = document.createElement("position");
                    shipElement.appendChild(positionElement);

                    // position x element
                    Element positionXElement = document.createElement("positionX");
                    positionXElement.appendChild(document.createTextNode(shipCoordinates.get(j).x + ""));
                    positionElement.appendChild(positionXElement);

                    // position y element
                    Element positionYElement = document.createElement("positionY");
                    positionYElement.appendChild(document.createTextNode(shipCoordinates.get(j).y + ""));
                    positionElement.appendChild(positionYElement);
                }

                ArrayList<Coordinate> shipHitPositions = AIShips.get(i).getHitPosition();

                // hitPositionSize element
                Element hitPositionSizeElement = document.createElement("hitPositionSize");
                hitPositionSizeElement.appendChild(document.createTextNode(shipHitPositions.size() + ""));
                shipElement.appendChild(hitPositionSizeElement);

                for (int k = 0; k < shipHitPositions.size(); k++) {
                    // hitPosition element
                    Element hitPositionElement = document.createElement("hitPosition");
                    shipElement.appendChild(hitPositionElement);

                    // position x element
                    Element positionXElement = document.createElement("hitPositionX");
                    positionXElement.appendChild(document.createTextNode(shipHitPositions.get(k).x + ""));
                    hitPositionElement.appendChild(positionXElement);

                    // position y element
                    Element positionYElement = document.createElement("hitPositionY");
                    positionYElement.appendChild(document.createTextNode(shipHitPositions.get(k).y + ""));
                    hitPositionElement.appendChild(positionYElement);
                }

                // size element
                Element sizeElement = document.createElement("size");
                sizeElement.appendChild(document.createTextNode(AIShips.get(i).size + ""));
                shipElement.appendChild(sizeElement);

                // isAlive element
                Element isAliveElement = document.createElement("isAlive");
                isAliveElement.appendChild(document.createTextNode(AIShips.get(i).getIsAlive() + ""));
                shipElement.appendChild(isAliveElement);
            }


            // humanBoard element
            Element humanBoardElement = document.createElement("humanBoard");
            root.appendChild(humanBoardElement);

            // waterGridState element
            Element waterGridStateElement = document.createElement("waterGridState");
            humanBoardElement.appendChild(waterGridStateElement);

            int[][] targetBoardState = MainWindow.humanBoard.getBoardState();

            for (int i = 0; i < targetBoardState.length; i++) {
                for (int j = 0; j < targetBoardState[0].length; j++) {

                    // Coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    waterGridStateElement.appendChild(coordinateElement);

                    // X element
                    Element xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(i + ""));
                    coordinateElement.appendChild(xElement);

                    // Y element
                    Element yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(j + ""));
                    coordinateElement.appendChild(yElement);

                    // State element
                    Element stateElement = document.createElement("state");
                    stateElement.appendChild(document.createTextNode(targetBoardState[i][j] + ""));
                    coordinateElement.appendChild(stateElement);
                }
            }

            // AIBoard element
            Element AIBoardElement = document.createElement("AIBoard");
            root.appendChild(AIBoardElement);

            // waterGridState element
            waterGridStateElement = document.createElement("waterGridState");
            AIBoardElement.appendChild(waterGridStateElement);

            targetBoardState = MainWindow.AIBoard.getBoardState();

            for (int i = 0; i < targetBoardState.length; i++) {
                for (int j = 0; j < targetBoardState[0].length; j++) {

                    // Coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    waterGridStateElement.appendChild(coordinateElement);

                    // X element
                    Element xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(i + ""));
                    coordinateElement.appendChild(xElement);

                    // Y element
                    Element yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(j + ""));
                    coordinateElement.appendChild(yElement);

                    // State element
                    Element stateElement = document.createElement("state");
                    stateElement.appendChild(document.createTextNode(targetBoardState[i][j] + ""));
                    coordinateElement.appendChild(stateElement);
                }
            }


            // AI element
            Element AIElement = document.createElement("AI");
            root.appendChild(AIElement);

            // targetMode element
            Element targetModeElement = document.createElement("targetMode");
            targetModeElement.appendChild(document.createTextNode(MainWindow.myAI.targetMode + ""));
            AIElement.appendChild(targetModeElement);

            // direction element
            Element directionElement = document.createElement("direction");
            targetModeElement.appendChild(document.createTextNode(MainWindow.myAI.direction + ""));
            AIElement.appendChild(directionElement);

            // distanceFromHit element
            Element distanceFromHitElement = document.createElement("distanceFromHit");
            distanceFromHitElement.appendChild(document.createTextNode(MainWindow.myAI.distanceFromHit + ""));
            AIElement.appendChild(distanceFromHitElement);

            // seekAgain element
            Element seekAgainElement = document.createElement("seekAgain");
            seekAgainElement.appendChild(document.createTextNode(MainWindow.myAI.seekAgain + ""));
            AIElement.appendChild(seekAgainElement);

            // axis element
            Element axisElement = document.createElement("axis");
            axisElement.appendChild(document.createTextNode(MainWindow.myAI.axis + ""));
            AIElement.appendChild(axisElement);

            // X element
            Element xElement = document.createElement("x");
            xElement.appendChild(document.createTextNode(MainWindow.myAI.axis.x + ""));
            axisElement.appendChild(xElement);

            // Y element
            Element yElement = document.createElement("y");
            xElement.appendChild(document.createTextNode(MainWindow.myAI.axis.y + ""));
            axisElement.appendChild(yElement);


            Coordinate previousTarget = MainWindow.myAI.getPreviousTarget();

            // previousTarget element
            Element previousTargetElement = document.createElement("previousTarget");
            AIElement.appendChild(previousTargetElement);

            // X element
            xElement = document.createElement("x");
            xElement.appendChild(document.createTextNode(previousTarget.x + ""));
            previousTargetElement.appendChild(xElement);

            // Y element
            yElement = document.createElement("y");
            xElement.appendChild(document.createTextNode(previousTarget.y + ""));
            previousTargetElement.appendChild(yElement);


            // countGrid element
            Element countGridElement = document.createElement("countGrid");
            AIElement.appendChild(countGridElement);

            int[][] copyGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            for (int i = 0; i < copyGrid.length; i++) {
                for (int j = 0; j < copyGrid[0].length; j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    waterGridStateElement.appendChild(coordinateElement);

                    // x element
                    xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(i + ""));
                    coordinateElement.appendChild(xElement);

                    // y element
                    yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(j + ""));
                    coordinateElement.appendChild(yElement);

                    // value element
                    Element valueElement = document.createElement("value");
                    valueElement.appendChild(document.createTextNode(copyGrid[i][j] + ""));
                    coordinateElement.appendChild(valueElement);
                }
            }


            

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
