import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class Programa {

  private static ArrayList<String> linksArchivos = new ArrayList<String>();
  private static ArrayList<String> linksDirectorios = new ArrayList<String>();

  public static void main(String[] args) {
    Scanner entrada = new Scanner(System.in);
    String nombreCarpeta = null;
    URL url = null;

    try{
      System.out.println("--------------------------");
      System.out.println("           Wget           ");
      System.out.println("--------------------------");
      System.out.println("");
      System.out.println("Ingresa el nombre de la carpeta a crear. (Aqui se guardaran los archivos descargados)");

      nombreCarpeta = entrada.nextLine();
      crearCarpeta(nombreCarpeta);

      System.out.println("Ingresa la URL de la página a descargar");
      System.out.println("Ejemplo: http://148.204.58.221");
      url = new URL(entrada.nextLine());
      //Obtener todos los links de la página
      setLinks(url.toString(), nombreCarpeta); //Llenamos los arrays de todos los directorios y archivos que existen

      //Creamos los directorios
      for(int i=0;i<linksDirectorios.size();i++){
        String directorio = nombreCarpeta + "/" + linksDirectorios.get(i);
        crearCarpeta(directorio);
      }


      //System.out.println("Directorios: " + linksDirectorios);
      //System.out.println("Archivos: "+ linksArchivos);

      //WGet.Download(url, "./"+nombreCarpeta);

      //wGet(url);

    } catch (DataFormatException e){
      System.out.println("No se puede nombrar un directorio con el nombre: "+ nombreCarpeta);
    } catch (NoSuchElementException e){
      System.out.println("No ingresaste el nombre de la carpeta.");
    } catch (MalformedURLException e){
      System.out.println("Se ingresó una URL erronea.");
    } catch (Exception e){
      System.out.println("Error general: "+ e.getMessage());
    }

    //wGet("PaginaProfe", "http://148.204.58.221/axel/aplicaciones/");
  }

  private static void setLinks(String url, String nombreCarpeta){
    try{
      for (String link : Links.findLinks(url)) {
        if(!link.startsWith("http")){ //Ignoramos los links de otras páginas ya que podría ser un bucle casi infinito si llegará un link de una página grande como youtube.
          if(!linksArchivos.contains(link) || !linksDirectorios.contains(link)){ //Solo agregamos el link si aun no esta en la lista
            if(link.endsWith("/")){ //Es un directorio
              if(link.startsWith("/")){ //Si inicia con /
                link = link.substring(1); //Le quitamos la primer diagonal para no ocacionar bugs
              }
              linksDirectorios.add(link);
              //System.out.println(nombreCarpeta+"/"+link);
              //System.out.println(link+"/"+nombreCarpeta);
              setLinks(url+"/"+link,nombreCarpeta); //Hacemos recursividad para crear todos los directorios dentro de otros
            } else{ //Es un archivo
              linksArchivos.add(link);
            }
          }
        }
      }
    } catch (IOException e){
      System.out.println("Error al obtener uno de los links");
    }
  }

  private static void crearCarpeta(String nombreCarpeta) throws DataFormatException {
    System.out.println(nombreCarpeta);
    if(nombreCarpeta != null){
      if(nombreCarpeta.matches("[-_. A-Za-z0-9/]+")){ // \ / : * ? " < > |
        File file = new File("./"+ nombreCarpeta);
        if(file.mkdir()){
          System.out.println("Directorio creado satisfactoriamente");
        } else{
          System.out.println("Error al crear el directorio o ya existe uno creado con el mismo nombre.");
        }
      } else{
        throw new DataFormatException();
      }
    } else{
      throw new DataFormatException();
    }
  }


  private static WgetStatus wGet(URL url){
    InputStream httpIn = null;
    OutputStream fileOutput = null;
    OutputStream bufferedOut = null;
    try {
      // Checamos que exista conexion con la URL
      httpIn = new BufferedInputStream(new URL(url.toString()).openStream());
      // prep saving the file
      fileOutput = new FileOutputStream("prueba");
      bufferedOut = new BufferedOutputStream(fileOutput, 1024);
      byte data[] = new byte[1024];
      boolean fileComplete = false;
      int count = 0;
      while (!fileComplete) {
        count = httpIn.read(data, 0, 1024);
        if (count <= 0) {
          fileComplete = true;
        } else {
          bufferedOut.write(data, 0, count);
        }
      }
    } catch (MalformedURLException e) {
      return WgetStatus.MalformedUrl;
    } catch (IOException e) {
      return WgetStatus.IoException;
    } finally {
      try {
        bufferedOut.close();
        fileOutput.close();
        httpIn.close();
      } catch (IOException e) {
        return WgetStatus.UnableToCloseOutputStream;
      }
    }
    return WgetStatus.Success;
  }


  public static WgetStatus wGet(String saveAsFile, String urlOfFile) {
    InputStream httpIn = null;
    OutputStream fileOutput = null;
    OutputStream bufferedOut = null;
    try {
      // check the http connection before we do anything to the fs
      httpIn = new BufferedInputStream(new URL(urlOfFile).openStream());
      // prep saving the file
      fileOutput = new FileOutputStream(saveAsFile);
      bufferedOut = new BufferedOutputStream(fileOutput, 1024);
      byte data[] = new byte[1024];
      boolean fileComplete = false;
      int count = 0;
      while (!fileComplete) {
        count = httpIn.read(data, 0, 1024);
        if (count <= 0) {
          fileComplete = true;
        } else {
          bufferedOut.write(data, 0, count);
        }
      }
    } catch (MalformedURLException e) {
      return WgetStatus.MalformedUrl;
    } catch (IOException e) {
      return WgetStatus.IoException;
    } finally {
      try {
        bufferedOut.close();
        fileOutput.close();
        httpIn.close();
      } catch (IOException e) {
        return WgetStatus.UnableToCloseOutputStream;
      }
    }
    return WgetStatus.Success;
  }

}