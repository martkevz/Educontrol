package com.educontrol.application.views.moduloasignatura;

import com.educontrol.application.controlador.AsignaturaController;
import com.educontrol.application.controlador.PeriodoAcademicoController;
import com.educontrol.application.modelos.Asignatura;
import com.educontrol.application.modelos.PeriodoAcademico;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Modulo Asignatura")
@Route(value = "asignatura-form", layout = com.educontrol.application.views.MainLayout.class)
@UIScope
public class ModuloAsignaturaView extends Composite<VerticalLayout> {

    private final TextField nombreField = new TextField("Nombre de la asignatura");
    private final TextField codigoField = new TextField("Código");
    private final ComboBox<PeriodoAcademico> periodoAcademicoComboBox = new ComboBox<>("Período Académico");
    private final Button saveButton = new Button("Guardar");
    private final Button deleteButton = new Button("Limpiar formulario");
    private final Grid<Asignatura> asignaturaGrid = new Grid<>(Asignatura.class);

    private final AsignaturaController asignaturaController;
    private final PeriodoAcademicoController periodoAcademicoController;

    private Asignatura asignaturaSeleccionada;

    @Autowired
    public ModuloAsignaturaView(AsignaturaController asignaturaController, PeriodoAcademicoController periodoAcademicoController) {
        this.asignaturaController = asignaturaController;
        this.periodoAcademicoController = periodoAcademicoController;

        // Configuración del layout principal
        H3 title = new H3("Gestión de Asignaturas");
        FormLayout formLayout = new FormLayout();

        // Configurar ComboBox para seleccionar el período académico
        periodoAcademicoComboBox.setItems(periodoAcademicoController.findAll());
        periodoAcademicoComboBox.setItemLabelGenerator(PeriodoAcademico::getNombrePeriodo);

        // Configurar el formulario
        formLayout.add(nombreField, codigoField, periodoAcademicoComboBox);

        // Configurar los botones
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> saveAsignatura());

        deleteButton.addClickListener(event -> clearForm());

        // Configurar el Grid
        asignaturaGrid.addColumn(Asignatura::getNombre).setHeader("Nombre");
        asignaturaGrid.addColumn(Asignatura::getCodigo).setHeader("Código");
        asignaturaGrid.addColumn(asignatura -> 
            asignatura.getPeriodoAcademico() != null ? asignatura.getPeriodoAcademico().getNombrePeriodo() : "N/A")
            .setHeader("Período Académico");

        asignaturaGrid.addComponentColumn(a -> {
             Button updateButton = new Button("Actualizar");
             updateButton.addClickListener(e -> selectAsignatura(a));
                return updateButton;
         });

        asignaturaGrid.addComponentColumn(periodo -> {
            Button deleteButton = new Button("Eliminar");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> {
                // Crear el cuadro de diálogo de confirmación
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Confirmar eliminación");
                confirmDialog.setText("¿Estás seguro de que quieres eliminar a esta asignatura :O?");
                
                // Configurar el botón de confirmación para eliminar
                confirmDialog.setConfirmButton("Eliminar!", event -> {
                    // Lógica para eliminar el asignatura
                    asignaturaController.deleteById(periodo.getId_asignatura());
                    Notification.show("Periodo Académico eliminado");
                    actualizarListaAsignaturas(); // Refrescar la tabla para reflejar los cambios
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
        
        asignaturaGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        asignaturaGrid.asSingleSelect().addValueChangeListener(event -> selectAsignatura(event.getValue()));

        // Cargar los datos en el Grid
        actualizarListaAsignaturas();

        // Configurar layout
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, deleteButton);
        VerticalLayout layout = getContent();
        layout.add(title, formLayout, buttonLayout, asignaturaGrid);
    }

    private void saveAsignatura() {
        try {
        // Verificar que los campos no estén vacíos
        if (nombreField.getValue().isEmpty() || codigoField.getValue().isEmpty()) {
            Notification.show("Por favor completa todos los campos requeridos", 3000, Notification.Position.MIDDLE);
            return;
        }
    
        // Verificar que se haya seleccionado un Período Académico
        if (periodoAcademicoComboBox.getValue() == null) {
            Notification.show("Debe seleccionar un período académico", 3000, Notification.Position.MIDDLE);
            return;
        }
    
        // Si no hay asignatura seleccionada, crear una nueva
        if (asignaturaSeleccionada == null) {
            asignaturaSeleccionada = new Asignatura();
        }
    
        // Establecer los valores en la asignatura
        asignaturaSeleccionada.setNombre(nombreField.getValue());
        asignaturaSeleccionada.setCodigo(codigoField.getValue());
        asignaturaSeleccionada.setPeriodoAcademico(periodoAcademicoComboBox.getValue());
    
        // Guardar la asignatura
        asignaturaController.save(asignaturaSeleccionada);
    
        // Mostrar notificación de éxito
        Notification.show("Asignatura guardada con éxito", 3000, Notification.Position.MIDDLE);
    
        // Actualizar el Grid de asignaturas y limpiar el formulario
        actualizarListaAsignaturas();
        clearForm();
    } catch (Exception e) {
        Notification.show("Error al guardar la asignatura: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        e.printStackTrace(); // Log completo en consola
    }
    }

    private void selectAsignatura(Asignatura asignatura) {
        if (asignatura != null) {
            asignaturaSeleccionada = asignatura;
            nombreField.setValue(asignatura.getNombre() != null ? asignatura.getNombre() : "");
            codigoField.setValue(asignatura.getCodigo() != null ? asignatura.getCodigo() : "");
            periodoAcademicoComboBox.setValue(asignatura.getPeriodoAcademico());
        } else {
            clearForm();
        }
    }

    private void clearForm() {
        nombreField.clear();
        codigoField.clear();
        periodoAcademicoComboBox.clear();
        asignaturaSeleccionada = null;
    }

    private void actualizarListaAsignaturas() {
        asignaturaGrid.setItems(asignaturaController.findAll());
    }
}