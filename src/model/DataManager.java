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

/**
 * Class to handle the game progress saving and loading functionality
 */
public class DataManager {

    public static final String xmlFilePath = "src/model/data/GameData.xml";

    /**
     * Parse the xml file and extract the data then load them to corresponding classes
     */
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

                    MainWindow.numberOfAISunkShips = Integer.parseInt(element.getElementsByTagName("numberOfAISunkShips").item(0).getTextContent());


                    MainWindow.gameMode = element.getElementsByTagName("gameMode").item(0).getTextContent();

                    MainWindow.playerFireTarget = new Coordinate(Integer.parseInt(element.getElementsByTagName("playerFireTargetX").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("playerFireTargetY").item(0).getTextContent()));

                    MainWindow.AIFireTarget = new Coordinate(Integer.parseInt(element.getElementsByTagName("AIFireTargetX").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("AIFireTargetY").item(0).getTextContent()));

                    if (element.getElementsByTagName("playerGaveAllShots").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                        MainWindow.playerGaveAllShots = true;
                    else
                        MainWindow.playerGaveAllShots = false;

                    MainWindow.numberOfPlayerShots = Integer.parseInt(element.getElementsByTagName("numberOfPlayerShots").item(0).getTextContent());

                    MainWindow.numberOfPlayerMaxShots = Integer.parseInt(element.getElementsByTagName("numberOfPlayerMaxShots").item(0).getTextContent());

                    ArrayList<Coordinate> targetCoordinate = new ArrayList<Coordinate>();

                    int size = Integer.parseInt(element.getElementsByTagName("playerFireTargetListSize").item(0).getTextContent());

                    for (int j = 0; j < size; j++) {
                        targetCoordinate.add(new Coordinate(Integer.parseInt(element.getElementsByTagName("x").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("y").item(j).getTextContent())));
                    }

                    MainWindow.playerFireTargetList = targetCoordinate;
                }
            }


            nodeList = (NodeList) xPath.compile("/data/frontEndShips/ship").evaluate(document, XPathConstants.NODESET);

            MainWindow.shipList = new ArrayList<FrontEndShip>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);


                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                  MainWindow.shipList.add(new FrontEndShip(Integer.parseInt(element.getElementsByTagName("size").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("direction").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("pivotX").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("pivotY").item(0).getTextContent())));

                    if (element.getElementsByTagName("sunk").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                        MainWindow.shipList.get(MainWindow.shipList.size() - 1).sunk = true;
                    else
                        MainWindow.shipList.get(MainWindow.shipList.size() - 1).sunk = false;

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


            nodeList = (NodeList) xPath.compile("/data/playerShips/ship").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int hitPositionSize = Integer.parseInt(element.getElementsByTagName("hitPositionSize").item(0).getTextContent());

                    if (hitPositionSize != 0) {

                        ArrayList<Coordinate> targetHitPosition = new ArrayList<Coordinate>();

                        for (int j = 0; j < hitPositionSize; j++) {
                            Coordinate coordinate = new Coordinate(Integer.parseInt(element.getElementsByTagName("hitPositionX").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("hitPositionY").item(j).getTextContent()));
                            targetHitPosition.add(coordinate);
                        }

                        if (element.getElementsByTagName("isAlive").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                            MainWindow.humanBoard.updateShip(i, targetHitPosition, true);
                        else
                            MainWindow.humanBoard.updateShip(i, targetHitPosition, false);
                    }
                }
            }


            // update the AI ships on AI board
            nodeList = (NodeList) xPath.compile("/data/AIShips/ship").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int size = Integer.parseInt(element.getElementsByTagName("size").item(0).getTextContent());

                    Coordinate[] shipCoordinates = new Coordinate[size];

                    for (int j = 0; j < size; j++) {
                        shipCoordinates[j] = new Coordinate(Integer.parseInt(element.getElementsByTagName("positionX").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("positionY").item(j).getTextContent()));

                    }

                    MainWindow.AIBoard.placeShip(shipCoordinates);


               }
            }


            nodeList = (NodeList) xPath.compile("/data/AIShips/ship").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int hitPositionSize = Integer.parseInt(element.getElementsByTagName("hitPositionSize").item(0).getTextContent());

                    if (hitPositionSize != 0) {

                        ArrayList<Coordinate> targetHitPosition = new ArrayList<Coordinate>();

                        for (int j = 0; j < hitPositionSize; j++) {
                            Coordinate coordinate = new Coordinate(Integer.parseInt(element.getElementsByTagName("hitPositionX").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("hitPositionY").item(j).getTextContent()));
                            targetHitPosition.add(coordinate);
                        }

                        if (element.getElementsByTagName("isAlive").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                            MainWindow.AIBoard.updateShip(i, targetHitPosition, true);
                        else
                            MainWindow.AIBoard.updateShip(i, targetHitPosition, false);
                    }
                }
            }


            nodeList = (NodeList) xPath.compile("/data/humanBoard").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    MainWindow.humanBoard.sunkNumber = Integer.parseInt(element.getElementsByTagName("sunkNumber").item(0).getTextContent());
                }
            }



            nodeList = (NodeList) xPath.compile("/data/humanBoard/waterGridState/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);


                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetBoardState[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("state").item(0).getTextContent());

               }
            }

            MainWindow.humanBoard.setBoardState(targetBoardState);


            nodeList = (NodeList) xPath.compile("/data/AIBoard").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    MainWindow.AIBoard.sunkNumber = Integer.parseInt(element.getElementsByTagName("sunkNumber").item(0).getTextContent());
                }
            }


            targetBoardState = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            nodeList = (NodeList) xPath.compile("/data/AIBoard/waterGridState/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetBoardState[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("state").item(0).getTextContent());

               }
            }

            MainWindow.AIBoard.setBoardState(targetBoardState);


            nodeList = (NodeList) xPath.compile("/data/AI").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    if (element.getElementsByTagName("targetMode").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                        MainWindow.myAI.targetMode = true;
                    else
                        MainWindow.myAI.targetMode = false;

                    if (element.getElementsByTagName("salvationMode").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                        MainWindow.myAI.setSalvationMode(true);
                    else
                        MainWindow.myAI.setSalvationMode(false);

                    MainWindow.myAI.direction = Integer.parseInt(element.getElementsByTagName("direction").item(0).getTextContent());

                    MainWindow.myAI.distanceFromHit = Integer.parseInt(element.getElementsByTagName("distanceFromHit").item(0).getTextContent());

                    if (element.getElementsByTagName("fixedDirection").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                        MainWindow.myAI.fixedDirection = true;
                    else
                        MainWindow.myAI.fixedDirection = false;

                    if (element.getElementsByTagName("seekAgain").item(0).getTextContent().compareToIgnoreCase("true") == 0)
                        MainWindow.myAI.seekAgain = true;
                    else
                        MainWindow.myAI.seekAgain = false;

                    MainWindow.myAI.axis = new Coordinate(Integer.parseInt(element.getElementsByTagName("axisX").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("axisY").item(0).getTextContent()));

                    MainWindow.myAI.setPreviousTarget(new Coordinate(Integer.parseInt(element.getElementsByTagName("previousTargetX").item(0).getTextContent()), Integer.parseInt(element.getElementsByTagName("previousTargetY").item(0).getTextContent())));

                    MainWindow.myAI.setNbDestroyerDestroyed(Integer.parseInt(element.getElementsByTagName("nbDestroyerDestroyed").item(0).getTextContent()));

                    MainWindow.myAI.setNbSubmarineDestroyed(Integer.parseInt(element.getElementsByTagName("nbSubmarineDestroyed").item(0).getTextContent()));

                    MainWindow.myAI.setNbCruiserDestroyed(Integer.parseInt(element.getElementsByTagName("nbCruiserDestroyed").item(0).getTextContent()));

                    MainWindow.myAI.setNbBattleshipDestroyed(Integer.parseInt(element.getElementsByTagName("nbBattleshipDestroyed").item(0).getTextContent()));

                    MainWindow.myAI.setNbCarrierDestroyed(Integer.parseInt(element.getElementsByTagName("nbCarrierDestroyed").item(0).getTextContent()));

               }
            }


            ArrayList<Coordinate> targetCoordinateList = new ArrayList<Coordinate>();

            nodeList = (NodeList) xPath.compile("/data/AI/previousTargetSalvation").evaluate(document, XPathConstants.NODESET);


            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int size = Integer.parseInt(element.getElementsByTagName("previousTargetSalvationSize").item(0).getTextContent());

                    for (int j = 0; j < size; j++)
                        targetCoordinateList.add(new Coordinate(Integer.parseInt(element.getElementsByTagName("x").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("y").item(j).getTextContent())));
                }
            }

            MainWindow.myAI.setPreviousTargetSalvation(targetCoordinateList);

            targetCoordinateList = MainWindow.myAI.getPreviousTargetSalvation();

            for (int i = 0; i < targetCoordinateList.size(); i++) {
                System.out.println(targetCoordinateList.get(i).x + ", " + targetCoordinateList.get(i).y + " ??");
            }


            int[][] targetGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            nodeList = (NodeList) xPath.compile("/data/AI/countGrid/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetGrid[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("value").item(0).getTextContent());

                }
            }

            MainWindow.myAI.setCountGrid(targetGrid);


            targetGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            nodeList = (NodeList) xPath.compile("/data/AI/destroyerCountGrid/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetGrid[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("value").item(0).getTextContent());

               }
            }

            MainWindow.myAI.setDestroyerCountGrid(targetGrid);


            targetGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            nodeList = (NodeList) xPath.compile("/data/AI/submarineCountGrid/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetGrid[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("value").item(0).getTextContent());

               }
            }

            MainWindow.myAI.setSubmarineCountGrid(targetGrid);


            targetGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            nodeList = (NodeList) xPath.compile("/data/AI/cruiserCountGrid/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetGrid[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("value").item(0).getTextContent());

               }
            }

            MainWindow.myAI.setCruiserCountGrid(targetGrid);


            targetGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            nodeList = (NodeList) xPath.compile("/data/AI/battleshipCountGrid/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetGrid[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("value").item(0).getTextContent());

               }
            }

            MainWindow.myAI.setBattleshipCountGrid(targetGrid);


            targetGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

            nodeList = (NodeList) xPath.compile("/data/AI/carrierCountGrid/coordinate").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    targetGrid[Integer.parseInt(element.getElementsByTagName("x").item(0).getTextContent())][Integer.parseInt(element.getElementsByTagName("y").item(0).getTextContent())] = Integer.parseInt(element.getElementsByTagName("value").item(0).getTextContent());

               }
            }

            MainWindow.myAI.setCarrierCountGrid(targetGrid);


            nodeList = (NodeList) xPath.compile("/data/AI/missToExclude").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int missToExcludeSize = Integer.parseInt(element.getElementsByTagName("size").item(0).getTextContent());

                    if (missToExcludeSize != 0) {

                        ArrayList<Coordinate> missToExcludeCoordinate = new ArrayList<Coordinate>();

                        for (int j = 0; j < missToExcludeSize; j++) {
                            Coordinate coordinate = new Coordinate(Integer.parseInt(element.getElementsByTagName("x").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("y").item(j).getTextContent()));
                            missToExcludeCoordinate.add(coordinate);

                        }

                        MainWindow.myAI.setMissToExclude(missToExcludeCoordinate);
                    }
                }
            }


            nodeList = (NodeList) xPath.compile("/data/AI/toExclude").evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int toExcludeSize = Integer.parseInt(element.getElementsByTagName("size").item(0).getTextContent());

                    if (toExcludeSize != 0) {

                        ArrayList<Coordinate> toExcludeCoordinate = new ArrayList<Coordinate>();

                        for (int j = 0; j < toExcludeSize; j++) {
                            Coordinate coordinate = new Coordinate(Integer.parseInt(element.getElementsByTagName("x").item(j).getTextContent()), Integer.parseInt(element.getElementsByTagName("y").item(j).getTextContent()));
                            toExcludeCoordinate.add(coordinate);

                        }

                        MainWindow.myAI.setToExclude(toExcludeCoordinate);
                    }
                }
            }


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

    /**
     * Write all necessary attributes to the xml file
     */
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


            // gameMode element
            Element gameModeElement = document.createElement("gameMode");
            gameModeElement.appendChild(document.createTextNode(MainWindow.gameMode));
            root.appendChild(gameModeElement);


            // playerFireTarget element
            Element playerFireTargetElement = document.createElement("playerFireTarget");
            root.appendChild(playerFireTargetElement);


            // playerFireTargetX element
            Element playerFireTargetXElement = document.createElement("playerFireTargetX");
            playerFireTargetXElement.appendChild(document.createTextNode(MainWindow.playerFireTarget.x + ""));
            playerFireTargetElement.appendChild(playerFireTargetXElement);


            // playerFireTargetY element
            Element playerFireTargetYElement = document.createElement("playerFireTargetY");
            playerFireTargetYElement.appendChild(document.createTextNode(MainWindow.playerFireTarget.y + ""));
            playerFireTargetElement.appendChild(playerFireTargetYElement);


            // playerFireTarget element
            Element AIFireTargetElement = document.createElement("AIFireTarget");
            root.appendChild(AIFireTargetElement);


            // AIFireTargetX element
            Element AIFireTargetXElement = document.createElement("AIFireTargetX");
            AIFireTargetXElement.appendChild(document.createTextNode(MainWindow.AIFireTarget.x + ""));
            AIFireTargetElement.appendChild(AIFireTargetXElement);


            // AIFireTargetY element
            Element AIFireTargetYElement = document.createElement("AIFireTargetY");
            AIFireTargetYElement.appendChild(document.createTextNode(MainWindow.AIFireTarget.y + ""));
            AIFireTargetElement.appendChild(AIFireTargetYElement);

            // playerGaveAllShots element
            Element playerGaveAllShotsElement = document.createElement("playerGaveAllShots");
            playerGaveAllShotsElement.appendChild(document.createTextNode(MainWindow.playerGaveAllShots + ""));
            root.appendChild(playerGaveAllShotsElement);


            // numberOfPlayerShots element
            Element numberOfPlayerShotsElement = document.createElement("numberOfPlayerShots");
            numberOfPlayerShotsElement.appendChild(document.createTextNode(MainWindow.numberOfPlayerShots + ""));
            root.appendChild(numberOfPlayerShotsElement);


            // numberOfPlayerMaxShots element
            Element numberOfPlayerMaxShotsElement = document.createElement("numberOfPlayerMaxShots");
            numberOfPlayerMaxShotsElement.appendChild(document.createTextNode(MainWindow.numberOfPlayerMaxShots + ""));
            root.appendChild(numberOfPlayerMaxShotsElement);


            // playerFireTargetList element
            Element playerFireTargetListElement = document.createElement("playerFireTargetList");
            root.appendChild(playerFireTargetListElement);


            // playerFireTargetListSize element
            Element playerFireTargetListSizeElement = document.createElement("playerFireTargetListSize");
            playerFireTargetListSizeElement.appendChild(document.createTextNode(MainWindow.playerFireTargetList.size() + ""));
            playerFireTargetListElement.appendChild(playerFireTargetListSizeElement);


            for (int i = 0; i < MainWindow.playerFireTargetList.size(); i++) {

                // coordinate element
                Element coordinateElement = document.createElement("coordinate");
                playerFireTargetListElement.appendChild(coordinateElement);

                // x element
                Element xElement = document.createElement("x");
                xElement.appendChild(document.createTextNode(MainWindow.playerFireTargetList.get(i).x + ""));
                coordinateElement.appendChild(xElement);

                // y element
                Element yElement = document.createElement("y");
                yElement.appendChild(document.createTextNode(MainWindow.playerFireTargetList.get(i).y + ""));
                coordinateElement.appendChild(yElement);
            }


            // frontEndShips element
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

                // x element
                Element xElement = document.createElement("pivotX");
                xElement.appendChild(document.createTextNode(MainWindow.shipList.get(i).pivotGridX + ""));
                pivotElement.appendChild(xElement);

                // y element
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

            // humanBoardSunkNumber element
            Element humanBoardSunkNumberElement = document.createElement("sunkNumber");
            humanBoardSunkNumberElement.appendChild(document.createTextNode(MainWindow.humanBoard.sunkNumber + ""));
            humanBoardElement.appendChild(humanBoardSunkNumberElement);

            // waterGridState element
            Element waterGridStateElement = document.createElement("waterGridState");
            humanBoardElement.appendChild(waterGridStateElement);

            int[][] targetBoardState = MainWindow.humanBoard.getBoardState();

            for (int i = 0; i < targetBoardState.length; i++) {
                for (int j = 0; j < targetBoardState[0].length; j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    waterGridStateElement.appendChild(coordinateElement);

                    // x element
                    Element xElement = document.createElement("x");
                    xElement.appendChild(document.createTextNode(i + ""));
                    coordinateElement.appendChild(xElement);

                    // y element
                    Element yElement = document.createElement("y");
                    yElement.appendChild(document.createTextNode(j + ""));
                    coordinateElement.appendChild(yElement);

                    // state element
                    Element stateElement = document.createElement("state");
                    stateElement.appendChild(document.createTextNode(targetBoardState[i][j] + ""));
                    coordinateElement.appendChild(stateElement);
                }
            }


            // AIBoard element
            Element AIBoardElement = document.createElement("AIBoard");
            root.appendChild(AIBoardElement);

            // AIBoardSunkNumber element
            Element AIBoardSunkNumberElement = document.createElement("sunkNumber");
            AIBoardSunkNumberElement.appendChild(document.createTextNode(MainWindow.humanBoard.sunkNumber + ""));
            AIBoardElement.appendChild(AIBoardSunkNumberElement);

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


            // salvationMode element
            Element salvationModeElement = document.createElement("salvationMode");
            salvationModeElement.appendChild(document.createTextNode(MainWindow.myAI.getSalvationMode() + ""));
            AIElement.appendChild(salvationModeElement);


            // direction element
            Element directionElement = document.createElement("direction");
            directionElement.appendChild(document.createTextNode(MainWindow.myAI.direction + ""));
            AIElement.appendChild(directionElement);


            // distanceFromHit element
            Element distanceFromHitElement = document.createElement("distanceFromHit");
            distanceFromHitElement.appendChild(document.createTextNode(MainWindow.myAI.distanceFromHit + ""));
            AIElement.appendChild(distanceFromHitElement);


            // fixedDirection element
            Element fixedDirectionElement = document.createElement("fixedDirection");
            fixedDirectionElement.appendChild(document.createTextNode(MainWindow.myAI.fixedDirection + ""));
            AIElement.appendChild(fixedDirectionElement);


            // seekAgain element
            Element seekAgainElement = document.createElement("seekAgain");
            seekAgainElement.appendChild(document.createTextNode(MainWindow.myAI.seekAgain + ""));
            AIElement.appendChild(seekAgainElement);


            // axis element
            Element axisElement = document.createElement("axis");
            AIElement.appendChild(axisElement);

            // X element
            Element xElement = document.createElement("axisX");

            if (MainWindow.myAI.axis != null)
                xElement.appendChild(document.createTextNode(MainWindow.myAI.axis.x + ""));

            axisElement.appendChild(xElement);

            // Y element
            Element yElement = document.createElement("axisY");

            if (MainWindow.myAI.axis != null)
                yElement.appendChild(document.createTextNode(MainWindow.myAI.axis.y + ""));

            axisElement.appendChild(yElement);


            Coordinate previousTarget = MainWindow.myAI.getPreviousTarget();


            // previousTarget element
            Element previousTargetElement = document.createElement("previousTarget");
            AIElement.appendChild(previousTargetElement);

            // x element
            xElement = document.createElement("previousTargetX");
            xElement.appendChild(document.createTextNode(previousTarget.x + ""));
            previousTargetElement.appendChild(xElement);

            // y element
            yElement = document.createElement("previousTargetY");
            yElement.appendChild(document.createTextNode(previousTarget.y + ""));
            previousTargetElement.appendChild(yElement);


            // previousTargetSalvation element
            Element previousTargetSalvationElement = document.createElement("previousTargetSalvation");
            AIElement.appendChild(previousTargetSalvationElement);

            ArrayList<Coordinate> targetCoordinateList = MainWindow.myAI.getPreviousTargetSalvation();

            // previousTargetSalvationSize element
            Element previousTargetSalvationSizeElement = document.createElement("previousTargetSalvationSize");
            previousTargetSalvationSizeElement.appendChild(document.createTextNode(targetCoordinateList.size() + ""));
            previousTargetSalvationElement.appendChild(previousTargetSalvationSizeElement);

            for (int i = 0; i < targetCoordinateList.size(); i++) {

                // coordinate element
                Element coordinateElement = document.createElement("coordinate");
                previousTargetSalvationElement.appendChild(coordinateElement);

                // x element
                xElement = document.createElement("x");
                xElement.appendChild(document.createTextNode(targetCoordinateList.get(i).x + ""));
                coordinateElement.appendChild(xElement);

                // y element
                yElement = document.createElement("y");
                yElement.appendChild(document.createTextNode(targetCoordinateList.get(i).y + ""));
                coordinateElement.appendChild(yElement);
            }


            // countGrid element
            Element countGridElement = document.createElement("countGrid");
            AIElement.appendChild(countGridElement);

            int[][] copyGrid = MainWindow.myAI.getCountGrid();

            for (int i = 0; i < copyGrid.length; i++) {
                for (int j = 0; j < copyGrid[0].length; j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    countGridElement.appendChild(coordinateElement);

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


            // destroyerCountGrid element
            Element destroyerCountGridElement = document.createElement("destroyerCountGrid");
            AIElement.appendChild(destroyerCountGridElement);

            copyGrid = MainWindow.myAI.getDestroyerCountGrid();

            for (int i = 0; i < copyGrid.length; i++) {
                for (int j = 0; j < copyGrid[0].length; j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    destroyerCountGridElement.appendChild(coordinateElement);

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


            // submarineCountGrid element
            Element submarineCountGridElement = document.createElement("submarineCountGrid");
            AIElement.appendChild(submarineCountGridElement);

            copyGrid = MainWindow.myAI.getSubmarineCountGrid();

            for (int i = 0; i < copyGrid.length; i++) {
                for (int j = 0; j < copyGrid[0].length; j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    submarineCountGridElement.appendChild(coordinateElement);

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


            // cruiserCountGrid element
            Element cruiserCountGridElement = document.createElement("cruiserCountGrid");
            AIElement.appendChild(cruiserCountGridElement);

            copyGrid = MainWindow.myAI.getCruiserCountGrid();

            for (int i = 0; i < copyGrid.length; i++) {
                for (int j = 0; j < copyGrid[0].length; j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    cruiserCountGridElement.appendChild(coordinateElement);

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


            // battleshipCountGrid element
            Element battleshipCountGridElement = document.createElement("battleshipCountGrid");
            AIElement.appendChild(battleshipCountGridElement);

            copyGrid = MainWindow.myAI.getBattleshipCountGrid();

            for (int i = 0; i < copyGrid.length; i++) {
                for (int j = 0; j < copyGrid[0].length; j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    battleshipCountGridElement.appendChild(coordinateElement);

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


            // carrierCountGrid element
            Element carrierCountGridElement = document.createElement("carrierCountGrid");
            AIElement.appendChild(carrierCountGridElement);

            copyGrid = MainWindow.myAI.getCarrierCountGrid();

            for (int i = 0; i < copyGrid.length; i++) {
                for (int j = 0; j < copyGrid[0].length; j++) {

                    // coordinate element
                    Element coordinateElement = document.createElement("coordinate");
                    carrierCountGridElement.appendChild(coordinateElement);

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


            // nbDestroyerDestroyed element
            Element nbDestroyerDestroyedElement = document.createElement("nbDestroyerDestroyed");
            nbDestroyerDestroyedElement.appendChild(document.createTextNode(MainWindow.myAI.getNbDestroyerDestroyed() + ""));
            AIElement.appendChild(nbDestroyerDestroyedElement);

            // nbSubmarineDestroyed element
            Element nbSubmarineDestroyedElement = document.createElement("nbSubmarineDestroyed");
            nbSubmarineDestroyedElement.appendChild(document.createTextNode(MainWindow.myAI.getNbSubmarineDestroyed() + ""));
            AIElement.appendChild(nbSubmarineDestroyedElement);

            // nbCruiserDestroyed element
            Element nbCruiserDestroyedElement = document.createElement("nbCruiserDestroyed");
            nbCruiserDestroyedElement.appendChild(document.createTextNode(MainWindow.myAI.getNbCruiserDestroyed() + ""));
            AIElement.appendChild(nbCruiserDestroyedElement);

            // nbBattleshipDestroyed element
            Element nbBattleshipDestroyedElement = document.createElement("nbBattleshipDestroyed");
            nbBattleshipDestroyedElement.appendChild(document.createTextNode(MainWindow.myAI.getNbBattleshipDestroyed() + ""));
            AIElement.appendChild(nbBattleshipDestroyedElement);

            // nbCarrierDestroyed element
            Element nbCarrierDestroyedElement = document.createElement("nbCarrierDestroyed");
            nbCarrierDestroyedElement.appendChild(document.createTextNode(MainWindow.myAI.getNbCarrierDestroyed() + ""));
            AIElement.appendChild(nbCarrierDestroyedElement);


            // missToExclude element
            Element missToExcludeElement = document.createElement("missToExclude");
            AIElement.appendChild(missToExcludeElement);

            ArrayList<Coordinate> copyArrayList = MainWindow.myAI.getMissToExclude();

            // size element
            Element sizeElement = document.createElement("size");
            sizeElement.appendChild(document.createTextNode(copyArrayList.size() + ""));
            missToExcludeElement.appendChild(sizeElement);

            for (int i = 0; i < copyArrayList.size(); i++) {

                // coordinate element
                Element coordinateElement = document.createElement("coordinate");
                missToExcludeElement.appendChild(coordinateElement);

                // x element
                xElement = document.createElement("x");
                xElement.appendChild(document.createTextNode(copyArrayList.get(i).x + ""));
                coordinateElement.appendChild(xElement);

                // y element
                yElement = document.createElement("y");
                yElement.appendChild(document.createTextNode(copyArrayList.get(i).y + ""));
                coordinateElement.appendChild(yElement);
            }


            // toExclude element
            Element toExcludeElement = document.createElement("toExclude");
            AIElement.appendChild(toExcludeElement);

            copyArrayList = MainWindow.myAI.getToExclude();

            // size element
            sizeElement = document.createElement("size");
            sizeElement.appendChild(document.createTextNode(copyArrayList.size() + ""));
            toExcludeElement.appendChild(sizeElement);

            for (int i = 0; i < copyArrayList.size(); i++) {

                // coordinate element
                Element coordinateElement = document.createElement("coordinate");
                toExcludeElement.appendChild(coordinateElement);

                // x element
                xElement = document.createElement("x");
                xElement.appendChild(document.createTextNode(copyArrayList.get(i).x + ""));
                coordinateElement.appendChild(xElement);

                // y element
                yElement = document.createElement("y");
                yElement.appendChild(document.createTextNode(copyArrayList.get(i).y + ""));
                coordinateElement.appendChild(yElement);
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