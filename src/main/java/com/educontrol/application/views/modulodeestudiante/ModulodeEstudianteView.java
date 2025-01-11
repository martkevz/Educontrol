package com.educontrol.application.views.modulodeestudiante;

import com.educontrol.application.controlador.EstudianteController;
import com.educontrol.application.modelos.Estudiante;
import com.educontrol.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.ZoneId;

@PageTitle("Modulo de Estudiante")
@Route(value = "Estudiante", layout = MainLayout.class)
public class ModulodeEstudianteView extends Composite<VerticalLayout> {
    private final EstudianteController estudianteController;

    // Componentes de formulario
    private TextField nombreField = new TextField("Nombre");
    private TextField apellidoField = new TextField("Apellido");
    private DatePicker fechaNacimientoField = new DatePicker("Fecha de nacimiento");
    private TextField nivelAcademicoField = new TextField("Nivel Académico");
    private TextField fotoUrlField = new TextField("Foto (URL - Ingreso manual o cargando el archivo)");
    private DatePicker fechaInscripcionField = new DatePicker("Fecha de Inscripción");
    private EmailField emailField = new EmailField("Email");
    private TextField numeroIdentificacionField = new TextField("Número de identificación");
    private TextArea direccionField = new TextArea("Dirección");

    private Button saveButton = new Button("Guardar");
    private Button cancelButton = new Button("Limpiar formulario");

    private Grid<Estudiante> grid = new Grid<>(Estudiante.class);
    private Estudiante Estudi;

    // Componentes adicionales para manejar la foto del estudiante
    private MemoryBuffer buffer = new MemoryBuffer();
    private Upload upload = new Upload(buffer);
    private Image image = new Image();

    @Autowired
    public ModulodeEstudianteView(EstudianteController estudianteController) {
        this.estudianteController = estudianteController;
        setupLayout();
        configureGrid();
        configureForm();
        configureUpload();
    }

    private void setupLayout() {
        H3 header = new H3("Personal Information");
        FormLayout formLayout = new FormLayout();

        formLayout.add(nombreField, apellidoField, fechaNacimientoField, nivelAcademicoField,
                fotoUrlField, fechaInscripcionField, emailField, numeroIdentificacionField, direccionField);

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Layout de la imagen
        VerticalLayout imageLayout = new VerticalLayout(upload, image);
        imageLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        getContent().add(header, formLayout, buttonsLayout, imageLayout, grid);

        // Configuración de botones
        saveButton.addClickListener(e -> saveEstudiante());
        cancelButton.addClickListener(e -> clearForm());

        refreshGrid();
    }

    private void configureGrid() {
        grid.setColumns("id_estudiante", "nombre", "apellido", "numeroIdentificacion", "direccion",
                "email", "fechaNacimiento", "nivelAcademico", "fechaInscripcion", "fotoUrl");
        grid.asSingleSelect().addValueChangeListener(event -> selectEstudiante(event.getValue()));

     // Botón de Actualizar
     grid.addComponentColumn(estudiante -> {
        Button updateButton = new Button("Actualizar");
        updateButton.addClickListener(e -> selectEstudiante(estudiante));
        return updateButton;
    });

    // Botón de Eliminar con confirmación
    grid.addComponentColumn(estudiante -> {
        Button deleteButton = new Button("Eliminar");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Confirmar eliminación");
        confirmDialog.setText("¿Estás seguro de que quieres eliminar este estudiante?");
        confirmDialog.setConfirmButton("Eliminar", event -> {
            estudianteController.deleteById(estudiante.getId_estudiante());
            Notification.show("Estudiante eliminado");
            refreshGrid();
        });
        confirmDialog.setCancelButton("Cancelar", event -> {
            confirmDialog.close();
        });
        confirmDialog.open();

        });
        return deleteButton;
    });
}

    private void configureForm() {
        clearForm();
    }

    private void configureUpload() {
        upload.addSucceededListener(e -> {
            String fileName = buffer.getFileName();
            String filePath = "uploads/" + fileName; 
            image.setSrc(filePath); // Muestra la imagen cargada
            fotoUrlField.setValue(filePath); // Guarda la URL de la foto en el campo fotoUrlField
    
            // Verifica si Estudi no es null antes de intentar acceder a sus propiedades
            if (Estudi != null) {
                Estudi.setFotoUrl(filePath); // Guarda la URL de la foto en el estudiante
            } else {
                Notification.show("No se ha seleccionado un estudiante.");
            }
        });
    }
    
    private void clearForm() {
        Estudi = null;
        nombreField.clear();
        apellidoField.clear();
        fechaNacimientoField.clear();
        nivelAcademicoField.clear();
        fotoUrlField.clear();
        fechaInscripcionField.clear();
        emailField.clear();
        numeroIdentificacionField.clear();
        direccionField.clear();
    }

    private void selectEstudiante(Estudiante estudiante) {
        if (estudiante != null) {
            Estudi = estudiante;
            nombreField.setValue(estudiante.getNombre());
            apellidoField.setValue(estudiante.getApellido());
            fechaNacimientoField.setValue(estudiante.getFechaNacimiento().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            nivelAcademicoField.setValue(estudiante.getNivelAcademico());
            fotoUrlField.setValue(estudiante.getFotoUrl());
            fechaInscripcionField.setValue(estudiante.getFechaInscripcion().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            emailField.setValue(estudiante.getEmail());
            numeroIdentificacionField.setValue(estudiante.getNumeroIdentificacion());
            direccionField.setValue(estudiante.getDireccion());
        }
    }

    private void saveEstudiante() {
        if (Estudi == null) {
            Estudi = new Estudiante();
        }

        Estudi.setNombre(nombreField.getValue());
        Estudi.setApellido(apellidoField.getValue());
        Estudi.setFechaNacimiento(java.sql.Date.valueOf(fechaNacimientoField.getValue()));
        Estudi.setNivelAcademico(nivelAcademicoField.getValue());
        Estudi.setFotoUrl(fotoUrlField.getValue());  // Aquí se guarda la URL de la foto
        Estudi.setFechaInscripcion(java.sql.Date.valueOf(fechaInscripcionField.getValue()));
        Estudi.setEmail(emailField.getValue());
        Estudi.setNumeroIdentificacion(numeroIdentificacionField.getValue());
        Estudi.setDireccion(direccionField.getValue());

        estudianteController.save(Estudi);
        Notification.show("Estudiante guardado");
        refreshGrid();
        clearForm();
    }

    private void refreshGrid() {
        grid.setItems(estudianteController.findAll());
    }
}