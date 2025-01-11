package com.educontrol.application.views.modulohorario;

import com.educontrol.application.controlador.HorarioController;
import com.educontrol.application.controlador.ProfesorController;
import com.educontrol.application.controlador.AsignaturaController;
import com.educontrol.application.modelos.Asignatura;
import com.educontrol.application.modelos.Horario;
import com.educontrol.application.modelos.Profesor;
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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

@PageTitle("Modulo Horario")
@Route(value = "modulo-horario", layout = com.educontrol.application.views.MainLayout.class)
public class ModuloHorarioView extends VerticalLayout {

    private final HorarioController horarioController;
    private final ProfesorController profesorController;
    private final AsignaturaController asignaturaController;

    private Grid<Horario> horarioGrid = new Grid<>(Horario.class);
    private Binder<Horario> binder = new Binder<>(Horario.class);

    private DatePicker diaPicker = new DatePicker("Día");
    private TimePicker horaPicker = new TimePicker("Hora");
    private Select<Profesor> profesorCombo = new Select<>();
    private Select<Asignatura> asignaturaCombo = new Select<>();

    private Button saveButton = new Button("Guardar");
    private Button cancelButton = new Button("Limpiar formulario");

    private Horario horarioActual;

    @Autowired
    public ModuloHorarioView(HorarioController horarioController, ProfesorController profesorController, AsignaturaController asignaturaController) {
        this.horarioController = horarioController;
        this.profesorController = profesorController;
        this.asignaturaController = asignaturaController;

        H3 title = new H3("Gestión de Horarios");

        // Configuración de ComboBoxes
        configureComboBoxes();

        // Configuración del Grid
        configureGrid();

        // Formulario
        FormLayout formLayout = new FormLayout();
        formLayout.add(diaPicker, horaPicker, profesorCombo, asignaturaCombo);

        // Botones
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> guardarHorario());

        cancelButton.addClickListener(e -> limpiarFormulario());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);

        // Layout principal
        VerticalLayout layout = new VerticalLayout(title, formLayout, buttonsLayout, horarioGrid);
        layout.setSizeFull();

        add(layout);

        // Configuración de Binder
        configurarBinder();
        cargarHorarios();
    }

    private void configureGrid() {
        horarioGrid.setColumns("dia", "hora", "profesor.id_profesor", "asignatura.id_asignatura");
        horarioGrid.getColumnByKey("profesor.id_profesor").setHeader("Profesor");
        horarioGrid.getColumnByKey("asignatura.id_asignatura").setHeader("Asignatura");

        horarioGrid.addComponentColumn(h -> {
            Button updateButton = new Button("Actualizar");
            updateButton.addClickListener(e -> selectHorario());
            return updateButton;
        });

        horarioGrid.addComponentColumn(periodo -> {
            Button deleteButton = new Button("Eliminar");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.setHeader("Confirmar eliminación");
                confirmDialog.setText("¿Estás seguro de que quieres eliminar a este horario?");
                confirmDialog.setConfirmButton("Eliminar!", event -> {
                    horarioController.deleteById(periodo.getId_horario());
                    Notification.show("Horario eliminado");
                    cargarHorarios();
                });
                confirmDialog.setCancelButton("Cancelar", event -> confirmDialog.close());
                confirmDialog.open();
            });
            return deleteButton;
        });

        horarioGrid.asSingleSelect().addValueChangeListener(event -> {
            horarioActual = event.getValue();
            if (horarioActual != null) {
                binder.readBean(horarioActual);
            } else {
                limpiarFormulario();
            }
        });
    }

    private void configureComboBoxes() {
        profesorCombo.setLabel("Profesor");
        profesorCombo.setItems(profesorController.findAll());
        profesorCombo.setItemLabelGenerator(Profesor::getNombre);

        asignaturaCombo.setLabel("Asignatura");
        asignaturaCombo.setItems(asignaturaController.findAll());
        asignaturaCombo.setItemLabelGenerator(Asignatura::getNombre);
    }

    private void configurarBinder() {
        binder.forField(diaPicker)
                .asRequired("El día es obligatorio")
                .withConverter(
                        LocalDate::toString,
                        str -> str != null ? LocalDate.parse(str) : null,
                        "Formato inválido para el día"
                )
                .bind(Horario::getDia, Horario::setDia);

        binder.forField(horaPicker)
                .asRequired("La hora es obligatoria")
                .withConverter(
                        LocalTime::toString,
                        str -> str != null ? LocalTime.parse(str) : null,
                        "Formato inválido para la hora"
                )
                .bind(Horario::getHora, Horario::setHora);

        binder.forField(profesorCombo)
                .asRequired("El profesor es obligatorio")
                .bind(
                        Horario::getProfesor,
                        Horario::setProfesor
                );

        binder.forField(asignaturaCombo)
                .asRequired("La asignatura es obligatoria")
                .bind(
                        Horario::getAsignatura,
                        Horario::setAsignatura
                );
    }

    private void guardarHorario() {
        if (binder.validate().isOk()) {
            try {
                if (horarioActual == null) {
                    horarioActual = new Horario();
                }
                binder.writeBeanIfValid(horarioActual);
                horarioController.save(horarioActual);
                Notification.show("Horario guardado correctamente.");
                limpiarFormulario();
                cargarHorarios();
            } catch (Exception e) {
                Notification.show("Error al guardar el horario: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        horarioActual = null;
        binder.readBean(null);
    }

    private void cargarHorarios() {
        horarioGrid.setItems(horarioController.findAll());
    }

    private void selectHorario() {
        horarioGrid.getListDataView().getItems().forEach(horarioGrid::select);
    }
}
