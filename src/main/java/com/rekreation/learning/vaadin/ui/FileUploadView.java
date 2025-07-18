package com.rekreation.learning.vaadin.ui;

import com.rekreation.learning.vaadin.excelhelper.ExcelImporter;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@RolesAllowed("ADMIN")
@Route(value = "file-upload", layout = MainLayout.class)
@PageTitle("File Upload | Sales CRM")
public class FileUploadView extends VerticalLayout {

    public FileUploadView(ExcelImporter excelImporter) {
        addClassName("file-upload-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        InMemoryUploadHandler inMemoryHandler = UploadHandler.inMemory(
                (metadata, data) -> {
                    // Get other information about the file.
                    String fileName = metadata.fileName();
                    String mimeType = metadata.contentType();
                    long contentLength = metadata.contentLength();

                    // Do something with the file data...
                    InputStream inputStream = new ByteArrayInputStream(data);
                    try {
                        processFile(inputStream, fileName, excelImporter);
                    } catch (Exception e) {
                        log.error("Error importing file: " + fileName, e);
                        Notification notification = Notification.show("Import failed: Please check with the on call support", 5000, Notification.Position.MIDDLE);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                });

        Upload upload = new Upload(inMemoryHandler);

        upload.setAcceptedFileTypes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

//        upload.addSucceededListener(event -> {
//            status.setText("File uploaded: " + event.getFileName());
//            // You can process the file here using buffer.getInputStream()
//        });

        add(upload);
    }

    private void processFile(InputStream data, String fileName, ExcelImporter excelImporter) {
        try {
            excelImporter.importAllSheets(data);
            excelImporter.importSampleData();
            Notification.show("File imported: " + fileName, 3000, Notification.Position.MIDDLE);
        } catch (Exception e) {
            Notification notification = Notification.show("Import failed: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

}