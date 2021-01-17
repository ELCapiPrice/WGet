import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class WGet {

  public static void Download(URL url, String dir){
    Document doc;
    String title = null;
    try {
      //Obtenemos la información de el URL
      doc = Jsoup.connect(url.toString()).get();

      //Obtenemos el titulo (sera usado para nombrar el archivo)
      title = getTitleByDoc(doc);
      //Creamos el archivo en el directorio que se especifica
      System.out.println("Directorio: "+ url.getFile());
      createNewFile(url.getFile(), getFileExtension(url.getPath()), doc);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //todo obtener la extension del archivo
  //
  private static String getFileExtension(String file) {
    //
    //prueba.html
    //prueba
    //rez/highlight.js
    //rez/highlight.php
    int posicionPunto = file.indexOf('.'); //Contine un punto
    if(posicionPunto == -1) {
      return ".html";
    }
    return "prueba";
  }

  //TODO ESCRIBIR EL HTML DEL ARCHIVO
  private static void createNewFile(String dir, String extension, Document doc){
    // /smite/index.html
    try{
      File archivo = new File(dir + extension);
      if(archivo.createNewFile()){
        System.out.println("Se creo el archivo");
        FileWriter myWriter = new FileWriter(dir + extension);

        // pegar el doc

      } else{
        System.out.println("Error al crear el archivo.");
      }
    } catch (IOException e){
      System.out.println("Ocurrio un error al crear el archivo.");
    }
  }


  private static String getTitleByDoc(Document doc) {
    String titulo = null;
    try{
      titulo = doc.title();
    } catch (Exception e){
      System.out.println("Error al obtener el título de la página");
    }
    return (titulo != null) ? titulo : "unnamed";
  }

}