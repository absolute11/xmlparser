package org.example;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class Main {
    public static void main(String[] args) {
        List<Employee> employeeList = parseXML("data.xml");
        String json = listToJson(employeeList);
        writeString(json, "data2.json");

    }
    public static List<Employee> parseXML(String filename) {
        List<Employee> employeeList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filename));
            Element root = document.getDocumentElement();
            NodeList employeeNodes = root.getElementsByTagName("employee");

            for (int i = 0; i < employeeNodes.getLength(); i++) {
                Node node = employeeNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = element.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());

                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    employeeList.add(employee);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    public static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new com.google.gson.reflect.TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}