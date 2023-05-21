package modules;
import persistencia.Persistencia;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public class Comunicaciones {
    public boolean enviaMensajeTelegram(String mensaje){
        String direccion; //URL de la API de mi bot en mi conversación
        String fijo=Persistencia.leerProperties("fijo");
        direccion=fijo+mensaje; //Metemos el mensaje al final
        URL url;
        boolean dev;
        dev=false;
        try {
            url = new URL(direccion); // Creando un objeto URL con la dirrecion de la API de mi bot
            URLConnection con = url.openConnection(); // Realizando la peitcion GET
            //Con esto, copiamos en in la respuesta HTTP , por si lo necesitamos
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            dev=true; //Ha tenido éxito
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return dev; //Devuelvo si ha tenido exito o no
    }
    public boolean enviaMail(String destino, Object mensaje, String asunto,String tipoMail,String ruta) {
        boolean resultado = false;
        String emisor=Persistencia.leerProperties("emisor");
        String username=Persistencia.leerProperties("username");
        String pass=Persistencia.leerProperties("pass");
        String host=Persistencia.leerProperties("host");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.user",emisor);
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        props.setProperty("mail.smtp.auth", "true");


        Session session = Session.getInstance(props, new javax.mail.Authenticator(){
            @Override
            protected
            PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username,pass);
            }
        });
        try{
            if (tipoMail.equals("application/pdf")){

                    // Adjuntar el archivo PDF
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(emisor));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(destino));   //Se podrían añadir varios de la misma manera
                    message.setSubject(asunto);
                    BodyPart text=new MimeBodyPart();
                    text.setContent(mensaje,"text/html; charset=utf-8");
                    BodyPart pdf=new MimeBodyPart();
                    pdf.setDataHandler(new DataHandler(new FileDataSource(ruta)));
                    MimeMultipart mimu=new MimeMultipart();
                    if(ruta.equals("pdf/prueba.pdf")){
                        pdf.setFileName("Reserva.pdf");
                    }else pdf.setFileName("Reservas.csv");
                    mimu.addBodyPart(text);
                    mimu.addBodyPart(pdf);
                    message.setContent(mimu);
                    Transport transport = session.getTransport("smtp");
                    transport.connect("smtp.gmail.com", emisor, pass);
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    resultado =true;
                }
                catch (MessagingException me) {
                    me.printStackTrace();  //Si se produce un error
                }
            }else{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emisor));
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destino));
                message.setSubject(asunto);
                message.setContent(mensaje,"text/html; charset=utf-8");
                Transport.send(message);
                resultado = true;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultado;
    }
}
