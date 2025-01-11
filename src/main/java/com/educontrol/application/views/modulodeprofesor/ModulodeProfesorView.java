package com.educontrol.application.views.modulodeprofesor;

import com.educontrol.application.controlador.ProfesorController;
import com.educontrol.application.modelos.Profesor;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Modulo de Profesor")
@Route(value = "profesor", layout = com.educontrol.application.views.MainLayout.class)
@UIScope
public class ModulodeProfesorView extends Composite<VerticalLayout> {

    private final ProfesorController profesorController;

    private final Grid<Profesor> grid = new Grid<>(Profesor.class, false);
    private final TextField nombreField = new TextField("Nombre");
    private final TextField apellidoField = new TextField("Apellido");
    private final EmailField emailField = new EmailField("Email");
    private final TextField numeroIdentificacionField = new TextField("Número de Identificación");
    private final TextField especialidadField = new TextField("Especialidad");
    private final TextArea direccionField = new TextArea("Dirección");
    private final Button saveButton = new Button("Guardar");
    private final Button cancelButton = new Button("Limpiar formulario");
    private final Button importButton = new Button("Importar Profesores");
    private Profesor currentProfesor;

    @Autowired
    public ModulodeProfesorView(ProfesorController profesorController) {
        this.profesorController = profesorController;

        // Configuración de la vista principal
        H3 title = new H3("Gestión de Profesores");
        FormLayout formLayout = new FormLayout(
            nombreField, apellidoField, emailField, 
            numeroIdentificacionField, especialidadField, direccionField
        );

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton, importButton);

        // Configuración de la cuadrícula
        grid.addColumn(Profesor::getNombre).setHeader("Nombre");
        grid.addColumn(Profesor::getApellido).setHeader("Apellido");
        grid.addColumn(Profesor::getEmail).setHeader("Email");
        grid.addColumn(Profesor::getNumeroIdentificacion).setHeader("Número de Identificación");
        grid.addColumn(Profesor::getEspecialidad).setHeader("Especialidad");
        grid.addColumn(Profesor::getDireccion).setHeader("Dirección");
        grid.asSingleSelect().addValueChangeListener(event -> selectProfesor(event.getValue()));

        grid.addComponentColumn(profesor -> {
            Button updateButton = new Button("Actualizar");
            updateButton.addClickListener(e -> selectProfesor(profesor));
            return updateButton;
        });

        grid.addComponentColumn(profesor -> {
            Button deleteButton = new Button("Eliminar");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> {
                // Crear el cuadro de diálogo de confirmación
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Confirmar eliminación");
                confirmDialog.setText("¿Estás seguro de que quieres eliminar a este profesor :O?");
                
                // Configurar el botón de confirmación para eliminar
                confirmDialog.setConfirmButton("Eliminar!", event -> {
                    // Lógica para eliminar el profesor
                    profesorController.deleteById(profesor.getId_profesor());
                    Notification.show("Profesor eliminado");
                    refreshGrid(); // Refrescar la tabla para reflejar los cambios
                });
        
                // Configuracion el botón de cancelar
                confirmDialog.setCancelButton("Cancelar", event -> {
                    // Cerrar el cuadro de diaklog sin hacer nada
                    confirmDialog.close();
                });
                // Abrir el cuadro 
                confirmDialog.open();
            });
            return deleteButton;
        });
        

        VerticalLayout layout = getContent();
        layout.add(title, formLayout, buttonsLayout, grid);

        // Eventos de botones
        importButton.addThemeVariants(ButtonVariant.LUMO_WARNING);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveProfesor());
        cancelButton.addClickListener(ev -> clearForm());
        importButton.addClickListener(e -> openFileUploadDialog());

        // Configuración de la cuadrícula
        grid.setItems(profesorController.findAll());
        grid.asSingleSelect().addValueChangeListener(event -> selectProfesor(event.getValue()));
    }

    private void selectProfesor(Profesor profesor) {
        if (profesor != null) {
            currentProfesor = profesor;
            nombreField.setValue(profesor.getNombre());
            apellidoField.setValue(profesor.getApellido());
            emailField.setValue(profesor.getEmail());
            numeroIdentificacionField.setValue(profesor.getNumeroIdentificacion());
            especialidadField.setValue(profesor.getEspecialidad());
            direccionField.setValue(profesor.getDireccion());
        } else {
            clearForm();
        }
    }

    private void saveProfesor() {
        if (currentProfesor == null) {
            currentProfesor = new Profesor();
        }
        currentProfesor.setNombre(nombreField.getValue());
        currentProfesor.setApellido(apellidoField.getValue());
        currentProfesor.setEmail(emailField.getValue());
        currentProfesor.setNumeroIdentificacion(numeroIdentificacionField.getValue());
        currentProfesor.setEspecialidad(especialidadField.getValue());
        currentProfesor.setDireccion(direccionField.getValue());

        profesorController.save(currentProfesor);
        refreshGrid();
        Notification.show("Profesor guardado con éxito", 3000, Notification.Position.MIDDLE);
        clearForm();
    }

    private void refreshGrid() {
        grid.setItems(profesorController.findAll());
    }

    private void clearForm() {
        currentProfesor = null;
        nombreField.clear();
        apellidoField.clear();
        emailField.clear();
        numeroIdentificacionField.clear();
        especialidadField.clear();
        direccionField.clear();
    }

    // Método para abrir el diálogo de carga de archivos
    private void openFileUploadDialog() {
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes(".xlsx");
        upload.setMaxFiles(1);

        upload.addSucceededListener(event -> {
            InputStream fileData = buffer.getInputStream();
            // Procesar el archivo Excel en un hilo separado
            Thread processingThread = new Thread(new FileProcessingTask(fileData, profesorController));
            processingThread.start();
            Notification.show("Archivo subido correctamente, procesando...");
        });

        Dialog uploadDialog = new Dialog();
        uploadDialog.add(upload);
        uploadDialog.open();
    }

    // Tarea de procesamiento de archivo Excel en un hilo separado
    private static class FileProcessingTask implements Runnable {
        private final InputStream fileData;
        private final ProfesorController profesorController;
    
        public FileProcessingTask(InputStream fileData, ProfesorController profesorController) {
            this.fileData = fileData;
            this.profesorController = profesorController;
        }
    
        @Override
        public void run() {
            try (XSSFWorkbook workbook = new XSSFWorkbook(fileData)) {
                XSSFSheet sheet = workbook.getSheetAt(0);
                List<Profesor> profesores = new ArrayList<>();
    
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;  // Ignorar la primera fila (encabezado)
                    String nombre = row.getCell(0).getStringCellValue();
                    String apellido = row.getCell(1).getStringCellValue();
                    String email = row.getCell(2).getStringCellValue();
                    String numeroIdentificacion = row.getCell(3).getStringCellValue();
                    String especialidad = row.getCell(4).getStringCellValue();
                    String direccion = row.getCell(5).getStringCellValue();
    
                    profesores.add(new Profesor(nombre, apellido, email, numeroIdentificacion, especialidad, direccion));
                }
    
                // Inserción dentro de una transacción gestionada por Spring
                profesorController.insertarProfesoresEnBatch(profesores);
                UI ui = UI.getCurrent();
                if (ui != null) {
                    ui.access(() -> {
                        Notification.show("Profesores importados correctamente", 3000, Notification.Position.MIDDLE);
                    });
                }
    
            } catch (Exception e) {
                e.printStackTrace();
                UI ui = UI.getCurrent();
                if (ui != null) {
                    ui.access(() -> {
                        Notification.show("Error al procesar el archivo: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
                    });
                }
            }
        }
    }
}
