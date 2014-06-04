package Temp;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 * @author graval
 * 
 */
public class JSONHandling {



  private static final String jsonFilePath = "./target/tmp/jsontmp";

  public static void main(final String[] args) {

    JSONObject jsonObject = new JSONObject();

    jsonObject.put("URL", "www.javacodegeeks.com");

    jsonObject.put("Site Name", "Java Code Geeks");

    jsonObject.put("Members", 120);
    String one = "namVal1";
    JSONObject jsonInnerObj1 = new JSONObject();
    jsonInnerObj1.put("Name", one);
    jsonInnerObj1.put("Roll", 23);
    JSONObject jsonInnerObj2 = new JSONObject();
    jsonInnerObj2.put("Name", "namVal2");
    jsonInnerObj2.put("Roll", 24);
    JSONObject jsonInnerObj3 = new JSONObject();
    jsonInnerObj3.put("Name", "namVal3");
    jsonInnerObj3.put("Roll", 25);
    JSONArray jsonArray = new JSONArray();

    jsonArray.add(jsonInnerObj1);
    jsonArray.add(jsonInnerObj2);
    jsonArray.add(jsonInnerObj3);

    jsonObject.put("Students", jsonArray);

    try {

      FileWriter jsonFileWriter = new FileWriter(jsonFilePath);
      jsonFileWriter.write(jsonObject.toJSONString());
      jsonFileWriter.flush();
      jsonFileWriter.close();

      System.out.print(jsonObject);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void readJsonTest() throws Exception {

    FileReader fileReader = new FileReader(jsonFilePath);

    Gson gson = new Gson();
    JsonObject Jobj = gson.fromJson(fileReader, JsonObject.class);
    System.out.println("Output of object>>>" + Jobj);
    JsonArray jArryObj1 = (JsonArray) Jobj.get("Students");
    Iterator<JsonElement> jiterator = jArryObj1.iterator();
    System.out.println("Going to print Student values...");
    while (jiterator.hasNext()) {
      System.out.println("=============");
      JsonElement jelem = jiterator.next();
      System.out.println("Student>>> " + jelem);
      String name = String.valueOf(jelem.getAsJsonObject().get("Name").getAsString());
      System.out.println("Student Name>> " + name);
      int roll = jelem.getAsJsonObject().get("Roll").getAsInt();
      System.out.println("Student Roll>> " + roll);
      System.out.println("=============");
    }

  }

}
