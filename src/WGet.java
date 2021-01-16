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


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getFileExtension(String file) {
    int posicionPunto = file.indexOf('.');
    if(posicionPunto == -1) {
      return ".html";
    }
    return "prueba";
  }

  private static void createNewFile(String dir, String extension){
    try{
      File archivo = new File(dir + extension);
      if(archivo.createNewFile()){
        System.out.println("Se creo el archivo");
        FileWriter myWriter = new FileWriter("filename.txt");
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