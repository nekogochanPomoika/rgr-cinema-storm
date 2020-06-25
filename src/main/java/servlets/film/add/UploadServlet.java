package servlets.film.add;

import handler.DBHandler;
import handler.FilmBuilder;
import handler.FilmUpdater;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import storage.Manufactory;

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
    private String resources;
    private String videoFilePath;
    private String imageFilePath;
    private String tempPath;
    private String filePath;
    private int maxFileSize = 1024 * 1024 * 1024;
    private int maxMemSize = 16 * 1024;
    private File file;
    private String filmUrl;

    public void init(){
        // Get the filePath location where it would be stored.
        //web/resources
        resources = Manufactory.pathes.get("resources");
        videoFilePath = resources + "videos\\";
        imageFilePath = resources + "images\\filmsPosters\\";
        tempPath = resources + "temp\\";
        try {
            filmUrl = FilmBuilder.values.get(1);
        } catch (IndexOutOfBoundsException e) {
            filmUrl = DBHandler.getFilmUrl(Manufactory.updatedFilmId);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(1);
        java.io.PrintWriter out = response.getWriter();
        // Check that we have a file upload request
        isMultipart = ServletFileUpload.isMultipartContent(request);

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File(tempPath));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );

        try {
            System.out.println(2);
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

                    if (contentType.equals("video/mp4") || contentType.equals("video/ogg") || contentType.equals("video/webm")) {
                        filePath = videoFilePath;
                        DBHandler.addVideoUrl(filmUrl + fileName.substring(fileName.lastIndexOf('.')), filmUrl);
                    } else {
                        filePath = imageFilePath;
                        DBHandler.addImageUrl(filmUrl + fileName.substring(fileName.lastIndexOf('.')), filmUrl);
                    }
                    // Write the file
                    file = new File(filePath + filmUrl + fileName.substring(fileName.lastIndexOf('.')));
                    if (file.exists()) {
                        file.delete();
                        file = new File(filePath + filmUrl + fileName.substring(fileName.lastIndexOf('.')));
                    }
                    fi.write(file);

                    System.out.println(5);
                }
            }
        } catch(Exception ex) {
            ex.fillInStackTrace();
        }
    }
}