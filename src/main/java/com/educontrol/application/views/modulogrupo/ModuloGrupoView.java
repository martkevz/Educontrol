package com.educontrol.application.views.modulogrupo;

import com.educontrol.application.controlador.GrupoController;
import com.educontrol.application.controlador.HorarioController;
import com.educontrol.application.controlador.EstudianteController;
import com.educontrol.application.modelos.Grupo;
import com.educontrol.application.modelos.Horario;
import com.educontrol.application.modelos.Estudiante;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Modulo Grupo")
@Route(value = "modulo-grupo", layout = com.educontrol.application.views.MainLayout.class)
public class ModuloGrupoView extends Composite<VerticalLayout> {

    private final GrupoController grupoController;
    private final HorarioController horarioController;
    private final EstudianteController estudianteController;

    private Grid<Grupo> grupoGrid = new Grid<>(Grupo.class);
    private Binder<Grupo> binder = new Binder<>(Grupo.class);

    private TextField nombreField = new TextField("Nombre del grupo");
    private ComboBox<Horario> horarioComboBox = new ComboBox<>("Horario");
    private ComboBox<Estudiante> estudianteComboBox = new ComboBox<>("Estudiante");
    private Button saveButton = new Button("Guardar");
    private Button cancelButton = new Button("Limpiar formulario");

    private Grupo grupoActual;

    @Autowired
    public ModuloGrupoView(GrupoController grupoController, HorarioController horarioController, EstudianteController estudianteController) {
        this.grupoController = grupoController;
        this.horarioController = horarioController;
        this.estudianteController = estudianteController;

        H3 title = new H3("Gestión de Grupos");

        // Formulario
        FormLayout formLayout = new FormLayout();
        formLayout.add(nombreField, horarioComboBox);

        // Botones
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> guardarGrupo());

        cancelButton.addClickListener(e -> limpiarFormulario());

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);

        // Layout principal
        VerticalLayout layout = new VerticalLayout(title, formLayout, buttonsLayout, grupoGrid);
        layout.setSizeFull();

        getContent().add(layout);

        // Configuración del Grid
        configureGrid();

        // Configuración de ComboBox
        configurarComboBox();

        // Configuración de Binder
        configurarBinder();
        cargarGrupos();
    }

    private void configureGrid() {
        grupoGrid.setColumns("id_grupo");
        grupoGrid.getColumnByKey("id_grupo").setHeader("ID Grupo");

        // Columna para el nombre del grupo
        grupoGrid.addColumn(Grupo::getNombre).setHeader("Nombre del Grupo");

        // Columna para el horario (ID)
        grupoGrid.addColumn(grupo -> {
            Horario horario = grupo.getHorario();
            return horario != null ? horario.getId_horario() : "N/A";
        }).setHeader("Horario (ID)");

        //Columna para el estudiante (ID)
        grupoGrid.addColumn(grupo -> {
            return estudianteComboBox.getValue() != null ? estudianteComboBox.getValue().getId_estudiante() : "Estudiante asociado por horario";
        }).setHeader("Estudiante (ID)");

        grupoGrid.addComponentColumn(grup -> {
            Button updateButton = new Button("Actualizar");
            updateButton.addClickListener(e -> selectGrupo()); 
            return updateButton;
        });

        grupoGrid.addComponentColumn(gr -> {
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
                    grupoController.deleteById(gr.getId_grupo());
                    Notification.show("Periodo Académico eliminado");
                    cargarGrupos(); // Refrescar la tabla para reflejar los cambios
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

        grupoGrid.asSingleSelect().addValueChangeListener(event -> {
            grupoActual = event.getValue();
            if (grupoActual != null) {
                binder.readBean(grupoActual);
            } else {
                limpiarFormulario();
            }
        });
    }

    private void configurarComboBox() {
        // Configuración del ComboBox para horarios
        List<Horario> horarios = horarioController.findAll();
        horarioComboBox.setItems(horarios);
        horarioComboBox.setItemLabelGenerator(h -> h.getDia() + " - " + h.getHora());

        // Configuración del ComboBox para estudiantes ---------NO IMPLEMENTADO------------------
        List<Estudiante> estudiantes = estudianteController.findAll();
        estudianteComboBox.setItems(estudiantes);
        estudianteComboBox.setItemLabelGenerator(e -> e.getNombre() + " " + e.getApellido());
    }

    private void configurarBinder() {
        binder.forField(nombreField)
              .asRequired("El nombre del grupo es obligatorio")
              .bind(Grupo::getNombre, Grupo::setNombre);

        binder.forField(horarioComboBox)
              .asRequired("El horario es obligatorio")
              .bind(Grupo::getHorario, Grupo::setHorario);
    }

    private void guardarGrupo() {
        if (binder.validate().isOk()) {
            try {
                if (grupoActual == null) {
                    grupoActual = new Grupo();
                }
                binder.writeBeanIfValid(grupoActual);

                grupoController.save(grupoActual);
                Notification.show("Grupo guardado correctamente.");
                limpiarFormulario();
                cargarGrupos();
            } catch (Exception e) {
                Notification.show("Error al guardar el grupo: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        grupoActual = null;
        binder.readBean(null);
        estudianteComboBox.clear();
    }

    private void cargarGrupos() {
        grupoGrid.setItems(grupoController.findAll());
    }

    private void selectGrupo() {
        // Seleccionar todos los elementos visibles en el Grid
        grupoGrid.getListDataView().getItems().forEach(grupoGrid::select);
    }    
}
