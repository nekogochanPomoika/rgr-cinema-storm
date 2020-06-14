package servlets.film;

import handler.DBHandler;
import handler.FilmBuilder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

@WebServlet(urlPatterns = "/upload-servlet")
public class UploadServlet extends HttpServlet {

    private boolean isMultipart;
    private String videoFilePath;
    private String imageFilePath;
    private String filePath;
    private int maxFileSize = 1024 * 1024 * 1024;
    private int maxMemSize = 16 * 1024;
    private File file;
    private String filmUrl;

    public void init( ){
        // Get the filePathile location where it would be stored.
        videoFilePath = "C:\\Users\\Egor\\IdeaProjects\\rgr-cinema-storm\\web\\resources\\videos\\";
        imageFilePath = "C:\\Users\\Egor\\IdeaProjects\\rgr-cinema-storm\\web\\resources\\images\\filmsPosters\\";

        filmUrl = FilmBuilder.values.get(FilmBuilder.keys.indexOf("url"));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check that we have a file upload request
        isMultipart = ServletFileUpload.isMultipartContent(request);

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("C:\\Users\\Egor\\IdeaProjects\\rgr-cinema-storm\\web\\resources\\temp"));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            while ( i.hasNext () ) {
                FileItem fi = (FileItem)i.next();
                if ( !fi.isFormField () ) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();

                    System.out.println(fileName);

                    if (contentType.equals("video/mp4") || contentType.equals("video/ogg") || contentType.equals("video/webm")) {
                        filePath = videoFilePath;
                        DBHandler.addVideoUrl(filePath + filmUrl, filmUrl);
                    } else {
                        filePath = imageFilePath;
                        DBHandler.addImageUrl(filePath + filmUrl, filmUrl);
                    }
                    // Write the file
                    file = new File(filePath + filmUrl + fileName.substring(fileName.lastIndexOf('.')));
                    fi.write(file);
                }
            }
        } catch(Exception ex) {
            ex.fillInStackTrace();
        }
    }
}