package com.educontrol.application.views.moduloPeriodoAcadémico;

import com.educontrol.application.controlador.PeriodoAcademicoController;
import com.educontrol.application.modelos.PeriodoAcademico;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Modulo Periodo Académico")
@Route(value = "Periodo", layout = MainLayout.class)
public class ModuloPeriodoAcadémicoView extends Composite<VerticalLayout> {

    private final PeriodoAcademicoController periodoController;

    private Grid<PeriodoAcademico> grid = new Grid<>(PeriodoAcademico.class);

    private TextField nombrePeriodo = new TextField("Nombre del periodo académico");
    private DatePicker fechaInicio = new DatePicker("Fecha de inicio");
    private DatePicker fechaFin = new DatePicker("Fecha de fin");

    private Button saveButton = new Button("Guardar", event -> guardarPeriodoAcademico());
    private Button cancelButton = new Button("Limpiar formulario");

    private PeriodoAcademico currentPeriodo;

    @Autowired
    public ModuloPeriodoAcadémicoView(PeriodoAcademicoController periodoController) {
        this.periodoController = periodoController;

        configurarVista();
        actualizarGrid();
    }

    private void configurarVista() {
        H3 titulo = new H3("Gestión de Períodos Académicos");
        FormLayout formulario = new FormLayout();
        formulario.add(nombrePeriodo, fechaInicio, fechaFin);

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addClickListener(event -> limpiarFormulario());

        HorizontalLayout botones = new HorizontalLayout(saveButton, cancelButton);

        getContent().add(titulo, formulario, botones, grid);
        grid.setColumns("nombrePeriodo", "fechaInicio", "fechaFin");
        grid.asSingleSelect().addValueChangeListener(event -> cargarFormulario(event.getValue()));

        grid.addComponentColumn(periodo -> {
            Button updateButton = new Button("Actualizar");
            updateButton.addClickListener(e -> cargarFormulario(periodo));
            return updateButton;
        });

        grid.addComponentColumn(periodo -> {
            Button deleteButton = new Button("Eliminar");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> {
                // Crear el cuadro de diálogo de confirmación
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Confirmar eliminación");
                confirmDialog.setText("¿Estás seguro de que quieres eliminar a este periodo academico :p?");
                
                // Configurar el botón de confirmación para eliminar
                confirmDialog.setConfirmButton("Eliminar!", event -> {
                    // Lógica para eliminar el periodo
                    periodoController.deleteById(periodo.getId_periodoAcademico());
                    Notification.show("Periodo Académico eliminado");
                    actualizarGrid(); // Refrescar la tabla para reflejar los cambios
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

    private void guardarPeriodoAcademico() {
        if (currentPeriodo == null) {
            currentPeriodo = new PeriodoAcademico();
        }
        currentPeriodo.setNombrePeriodo(nombrePeriodo.getValue());
        currentPeriodo.setFechaInicio(fechaInicio.getValue().toString());
        currentPeriodo.setFechaFin(fechaFin.getValue().toString());

        periodoController.save(currentPeriodo);
        Notification.show("Periodo académico guardado con éxito", 3000, Notification.Position.MIDDLE);
        actualizarGrid();
        limpiarFormulario();
    }

    private void actualizarGrid() {
        grid.setItems(periodoController.findAll());
    }

    private void limpiarFormulario() {
        currentPeriodo = null;
        nombrePeriodo.clear();
        fechaInicio.clear();
        fechaFin.clear();
        grid.asSingleSelect().clear();
    }

    private void cargarFormulario(PeriodoAcademico periodo) {
        if (periodo != null) {
            currentPeriodo = periodo;
            nombrePeriodo.setValue(periodo.getNombrePeriodo());
            fechaInicio.setValue(LocalDate.parse(periodo.getFechaInicio()));
            fechaFin.setValue(LocalDate.parse(periodo.getFechaFin()));
        } else {
            limpiarFormulario();
        }
    }
}
