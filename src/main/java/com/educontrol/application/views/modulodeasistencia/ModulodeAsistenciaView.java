package com.educontrol.application.views.modulodeasistencia;

import com.educontrol.application.controlador.AsistenciaController;
import com.educontrol.application.controlador.EstudianteController;
import com.educontrol.application.controlador.HorarioController;
import com.educontrol.application.modelos.Asistencia;
import com.educontrol.application.modelos.Estudiante;
import com.educontrol.application.modelos.Horario;
import com.educontrol.application.views.MainLayout;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@PageTitle("Módulo de Asistencia")
@Route(value = "Asistencia", layout = MainLayout.class)
public class ModulodeAsistenciaView extends Composite<VerticalLayout> {
    private final AsistenciaController asistenciaController;
    private final EstudianteController estudianteController;
    private final HorarioController horarioController;

    // Componentes de la tabla
    private Grid<Asistencia> grid = new Grid<>(Asistencia.class);

    // Componentes del formulario
    private ComboBox<Estudiante> estudianteComboBox = new ComboBox<>("Estudiante");
    private ComboBox<Horario> horarioComboBox = new ComboBox<>("Horario");
    private Checkbox presenteCheckbox = new Checkbox("Presente");

    private Button saveButton = new Button("Guardar");
    private Button cancelButton = new Button("Limpiar formulario");
    private Button exportButton = new Button("Exportar en PDF");

    private Binder<Asistencia> binder = new Binder<>(Asistencia.class);
    private Asistencia asistencia;

    @Autowired
    public ModulodeAsistenciaView(AsistenciaController asistenciaController, EstudianteController estudianteController, HorarioController horarioController) {
        this.asistenciaController = asistenciaController;
        this.estudianteController = estudianteController;
        this.horarioController = horarioController;
        setupLayout();
        configureGrid();
        configureForm();
    }

    private void setupLayout() {
        H3 header = new H3("Registro de Asistencia");

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton, exportButton);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(header, createFormLayout(), buttonsLayout, grid);

        // Configuración de botones
        saveButton.addClickListener(e -> saveAsistencia());
        cancelButton.addClickListener(e -> clearForm());
        exportButton.addClickListener(e -> exportAsistenciasToPdf());
        exportButton.addThemeVariants(ButtonVariant.LUMO_WARNING);

        refreshGrid();
    }

    private void configureGrid() {
        grid.setColumns("id_asistencia", "estudiante.nombre", "estudiante.apellido", "horario.dia", "horario.hora", "presente");
        grid.asSingleSelect().addValueChangeListener(event -> selectAsistencia(event.getValue()));

        // Botón de Actualizar
        grid.addComponentColumn(asistencia -> {
            Button updateButton = new Button("Actualizar");
            updateButton.addClickListener(e -> selectAsistencia(asistencia));
            return updateButton;
        });

        grid.addComponentColumn(profesor -> {
            Button deleteButton = new Button("Eliminar");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> {
                // Crear el cuadro de diálogo de confirmación
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Confirmar eliminación");
                confirmDialog.setText("¿Estás seguro de que quieres eliminar esta asistencia :O?");
                
                // Configurar el botón de confirmación para eliminar
                confirmDialog.setConfirmButton("Eliminar!", event -> {
                    // Lógica para eliminar el profesor
                    asistenciaController.deleteById(profesor.getId_asistencia());
                    Notification.show("Asistencia eliminado");
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
    }

    private void configureForm() {
        // Configuración del ComboBox de estudiantes
        estudianteComboBox.setItems(estudianteController.findAll());
        estudianteComboBox.setItemLabelGenerator(estudiante -> estudiante.getNombre() + " " + estudiante.getApellido());

        // Configuración del ComboBox de horarios
        horarioComboBox.setItems(horarioController.findAll());
        horarioComboBox.setItemLabelGenerator(horario -> horario.getDia() + " " + horario.getHora());

        // Vincular formulario con Binder
        binder.bind(estudianteComboBox, Asistencia::getEstudiante, Asistencia::setEstudiante);
        binder.bind(horarioComboBox, Asistencia::getHorario, Asistencia::setHorario);
        binder.bind(presenteCheckbox, Asistencia::isPresente, Asistencia::setPresente);
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(estudianteComboBox, horarioComboBox, presenteCheckbox);
        return formLayout;
    }

    private void saveAsistencia() {
        if (asistencia == null) {
            asistencia = new Asistencia();
        }

        try {
            binder.writeBean(asistencia);
            asistenciaController.save(asistencia);
            Notification.show("Asistencia guardada");
            refreshGrid();
            clearForm();
        } catch (Exception e) {
            Notification.show("Error al guardar la asistencia: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void selectAsistencia(Asistencia asistencia) {
        if (asistencia != null) {
            this.asistencia = asistencia;
            binder.readBean(asistencia);
        }
    }

    private void clearForm() {
        binder.readBean(null);
        asistencia = null;
    }

    private void refreshGrid() {
        grid.setItems(asistenciaController.findAll());
    }

    private void exportAsistenciasToPdf() {
        Document document = new Document();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, outputStream);
            document.open();
    
            // Título
            document.add(new Paragraph("Lista de Asistencias"));
            document.add(new Paragraph(" "));
    
            // Tabla
            PdfPTable table = new PdfPTable(5);
            table.addCell("Estudiante");
            table.addCell("Apellido");
            table.addCell("Día");
            table.addCell("Hora");
            table.addCell("Presente");
    
            for (Asistencia asistencia : asistenciaController.findAll()) {
                table.addCell(asistencia.getEstudiante().getNombre());
                table.addCell(asistencia.getEstudiante().getApellido());
                table.addCell(asistencia.getHorario().getDia());
                table.addCell(asistencia.getHorario().getHora());
                table.addCell(asistencia.isPresente() ? "Sí" : "No");
            }
    
            document.add(table);
            document.close();
    
            // Generar un recurso descargable 
            StreamResource resource = new StreamResource("asistencias.pdf", 
                    () -> new ByteArrayInputStream(outputStream.toByteArray()));
            resource.setContentType("application/pdf");  // Asegura que se trata como un PDF
    
            // Crear el enlace para descargar
            Anchor downloadLink = new Anchor(resource, "Descargar PDF");
            downloadLink.getElement().setAttribute("download", "asistencias.pdf");  // Nombre del archivo
    
            // Personalizar y mostrar la notificación
            HorizontalLayout layout = new HorizontalLayout(new Text("¡Archivo listo! "), downloadLink);
            layout.getStyle()
                    .set("background-color", "#007BFF") // Fondo azul
                    .set("color", "white") // Texto blanco
                    .set("padding", "10px") // Espaciado
                    .set("border-radius", "5px"); // Bordes redondeados
    
            Notification notification = new Notification();
            notification.add(layout);
            notification.setDuration(5000); // Duración en milisegundos
            notification.setPosition(Notification.Position.BOTTOM_CENTER); // Configurar la posición
            notification.open();
    
        } catch (DocumentException | IOException e) {
            Notification.show("Error al generar el PDF: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }}
    