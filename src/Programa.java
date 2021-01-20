import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
  private static ArrayList<URL> linksAbsolutos = new ArrayList<URL>();
  public static URL urlPrincipal = null;

  public static void main(String[] args) {
    Scanner entrada = new Scanner(System.in);
    String nombreCarpeta = null;
    URL url = null;
    long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
    TInicio = System.currentTimeMillis();
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

      Document html = Jsoup.connect(urlPrincipal.toString()).get();
      File index = new File(nombreCarpeta+"/index.html");
      if(index.createNewFile()){

        Elements imagesEditarRuta = html.getElementsByTag("img");

        for(Element image : imagesEditarRuta){
          String rutaRelativa = image.attr("src");

          if(rutaRelativa.startsWith("/")){
            rutaRelativa = rutaRelativa.substring(1);
          }
          image.attr("src",rutaRelativa);
          System.out.println("Ruta Style: "+ rutaRelativa);
          String rutaAbsoluta = urlPrincipal.toString() + rutaRelativa;
          URL urlAbsoluto = new URL(rutaAbsoluta);
          if(linksAbsolutos.contains(urlAbsoluto)) continue;
          linksAbsolutos.add(urlAbsoluto);
        }
        FileWriter myWriter = new FileWriter(nombreCarpeta+"/index.html");
        BufferedWriter bw  = new BufferedWriter(myWriter);
        PrintWriter wr = new PrintWriter(bw);
        wr.append(html.toString());
        wr.close();
        bw.close();

        System.out.println("Index creado con éxito");
      }
      System.out.println("Leyendo las carpetas de la página...");
      setLinks(urlPrincipal);
      System.out.println("Generando las carpetas de la página, por favor espere...");
      System.out.println(linksAbsolutos);
      //System.out.println("LISTO");
      //Creamos los directorios
      for(int i=0;i<linksAbsolutos.size();i++){
        String replace = linksAbsolutos.get(i).toString().replace(urlPrincipal.toString(),"");
        String directorio = nombreCarpeta + "/" + replace;
        if(directorio.endsWith("/")){
          crearCarpeta(directorio);
        } else{
          System.out.println("Ruta Especifica: "+linksAbsolutos.get(i));
          System.out.println("Directortio especifico: "+directorio);
          WGet.Download(linksAbsolutos.get(i), directorio);
        }
      }

    } catch (DataFormatException e){
      System.out.println("Error en el nombre del directorio "+ e.getMessage());
    } catch (NoSuchElementException e){
      System.out.println("No ingresaste el nombre de la carpeta."+ e.getMessage());
    } catch (MalformedURLException e){
      System.out.println("Se ingresó una URL erronea."+ e.getMessage());
    } catch (Exception e){
      System.out.println("Error general: "+ e.getMessage());
    }

    //wGet("PaginaProfe", "http://148.204.58.221/axel/aplicaciones/");

    TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
    tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
    System.out.println("Tiempo de ejecución en milisegundos: " + tiempo); //Mostramos en pantalla el tiempo de ejecución en milisegundos
    System.out.println("Tiempo de ejecucion en segundos: "+ tiempo/1000.0);
  }

  private static void setLinks(URL url){
    try {
      Document doc = Jsoup.connect(url.toString()).get();
      Elements links = doc.getElementsByTag("a");
      Elements styles = doc.getElementsByTag("link");
      Elements javascripts = doc.getElementsByTag("script");
      Elements images = doc.getElementsByTag("img");

      for(Element image : images){
        String rutaRelativa = image.attr("src");
        System.out.println("Ruta Style: "+ rutaRelativa);


        String rutaAbsoluta = url.toString() + rutaRelativa;
        URL urlAbsoluto = new URL(rutaAbsoluta);
        if(linksAbsolutos.contains(urlAbsoluto)) continue;
        linksAbsolutos.add(urlAbsoluto);
      }

      for(Element style : styles){
        String rutaRelativa = style.attr("href");
        System.out.println("Ruta Style: "+ rutaRelativa);

        String rutaAbsoluta = url.toString() + rutaRelativa;
        URL urlAbsoluto = new URL(rutaAbsoluta);
        if(linksAbsolutos.contains(urlAbsoluto)) continue;
        linksAbsolutos.add(urlAbsoluto);
      }
      for (Element javascript : javascripts){
        String rutaRelativa = javascript.attr("src");
        System.out.println("Ruta Javascript: "+ javascript);

        String rutaAbsoluta = url.toString() + rutaRelativa;
        URL urlAbsoluto = new URL(rutaAbsoluta);
        if(linksAbsolutos.contains(urlAbsoluto)) continue;
        linksAbsolutos.add(urlAbsoluto);
      }
      for (Element link : links) {
        String rutaAbsoluta;
        String rutaRelativa = link.attr("href");

        if(rutaRelativa.startsWith("http")) continue;
        if(rutaRelativa.startsWith("?")) continue;
        if(rutaRelativa.startsWith("#")) continue;
        if(rutaRelativa.startsWith("/")) continue;
        if(rutaRelativa.endsWith("../")) continue;
        if(rutaRelativa.isEmpty()) continue;
        if(rutaRelativa.startsWith("./")) {
          rutaRelativa = rutaRelativa.replace("./","");

          rutaAbsoluta = url.toString() + rutaRelativa;
        } else{
          rutaAbsoluta = url.toString() + rutaRelativa;
        }

        URL urlAbsoluto = new URL(rutaAbsoluta);
        if(linksAbsolutos.contains(urlAbsoluto)) continue;
        linksAbsolutos.add(urlAbsoluto);
        if(urlAbsoluto.toString().endsWith("/")){
          //System.out.println("Entra hasta aca");
          System.out.println(linksAbsolutos);
          setLinks(urlAbsoluto);
        }

      }

    } catch (HttpStatusException e) {

      System.out.println("Error: " + e.getMessage());
    } catch (IOException ex) {
      System.out.println("Ocurrio un error: "+ ex.getMessage());;
    }
  }



  private static void crearCarpeta(String directorio) throws DataFormatException {
    if(directorio != null){
      if(directorio.matches("[-_. A-Za-z0-9áéíóúÁÉÍÓÚ/]+")){ // \ / : * ? " < > |
        File file = new File("./"+ directorio);
        if(file.mkdirs()){
          System.out.println("Directorio creado satisfactoriamente");
        } else{
          System.out.println("Error al crear el directorio o ya existe uno creado con el mismo nombre.");
        }
      } else{
        throw new DataFormatException("El nombre del directorio es inválido");
      }
    } else{
      throw new DataFormatException("El nombre del directorio no puede estar vacío");
    }
  }


}