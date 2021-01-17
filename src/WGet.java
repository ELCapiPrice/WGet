import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;

public class WGet {

  public static void Download(URL url, String dir){
    Document doc;
    String title = null;
    try {
      //Obtenemos la información de el URL
      doc = Jsoup.connect(url.toString()).get();
      //Creamos el archivo en el directorio que se especifica
      createNewFile(dir,".html",doc);

      //Obtenemos el titulo (sera usado para nombrar el archivo)
      title = getTitleByDoc(doc);

    }catch (HttpStatusException e){
      System.out.println("Error al acceder a la página: "+ e.getMessage());
    } catch (UnsupportedMimeTypeException e){
      System.out.println("Error en el MIME: "+ e.getMessage());
      title = url.getPath();
      System.out.println("URL PATH: "+ url.getPath());
/*
      try (BufferedInputStream in = new BufferedInputStream(new URL(url.toString()).openStream());
           FileOutputStream fileOutputStream = new FileOutputStream(titulo)) {
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
          fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
      } catch (IOException er) {
        // handle exception
      }*/


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
    try{
      String carpeta = dir.substring(0,dir.lastIndexOf('/'));
      File directorio = new File(carpeta);
      File archivo = new File(dir);
     // System.out.println("Documento: "+doc.toString());
      System.out.println("Directorio que recibo para crear: "+ dir);

      if(directorio.mkdirs()){
        System.out.println("Directorio creado con éxito");
      }
      if(archivo.createNewFile()){
        System.out.println("Se creo el archivo: "+ dir);
        FileWriter myWriter = new FileWriter(dir);
        BufferedWriter bw  = new BufferedWriter(myWriter);
        PrintWriter wr = new PrintWriter(bw);
        wr.append(doc.toString());
        wr.close();
        bw.close();

      } else{
        System.out.println("Error desconocido al crear el archivo.");
      }
    } catch (IOException e){
      System.out.println("Ocurrio un error al crear el archivo: " + e.getMessage());
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