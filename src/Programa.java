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
  public static URL urlPrincipal = null;

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
      System.out.println("Ejemplo: http://148.204.58.221/axel/aplicaciones/");
      Scanner entrada2 = new Scanner(System.in);
      urlPrincipal = new URL(entrada2.nextLine());
      //Obtenemos todos los links de la página
      setLinks(urlPrincipal, "/"); //Llenamos los arrays de todos los directorios y archivos que existen



      //Creamos los directorios
      for(int i=0;i<linksDirectorios.size();i++){
        String directorio = nombreCarpeta + "/" + linksDirectorios.get(i);
        crearCarpeta(directorio);
      }


      System.out.println("Directorios: " + linksDirectorios);
      System.out.println("Archivos: "+ linksArchivos);

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

  //TODO obtener todos los links de cada pagina
  private static void setLinks(URL url, String dirPadre){ // http://148.204.58.221/axel/aplicaciones/Contingencia/
    System.out.println("URL que recibo: "+ url);
    try{
      for (String link : Links.findLinks(url.toString())) { //Para cada uno de los links que encuentre en las etiquetas <a>
        if(!link.startsWith("http")){ //Ignoramos los links de otras páginas ya que podría ser un bucle casi infinito si llegará un link de una página grande como youtube.
          if(!linksArchivos.contains(link) || !linksDirectorios.contains(link)){ //Solo agregamos el link si aun no esta en la lista
            if(link.endsWith("/")){ //Es un directorio
              if(!link.startsWith("/")){
                linksDirectorios.add(urlPrincipal.toString() +"/"+link);
                System.out.println("LINK:" + link);
                System.out.println(urlPrincipal.toString()+"/"+link);
                dirPadre = link;
                URL newURL = new URL(urlPrincipal.toString()+"/"+dirPadre);
                System.out.println("URL: " + newURL);
                setLinks(newURL, "/");
              }
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


}