package com.educontrol.application.views.modulohorario;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Modulo Horario")
@Route("person-form4")
@Menu(order = 4, icon = "line-awesome/svg/user.svg")
public class ModuloHorarioView extends Composite<VerticalLayout> {

    public ModuloHorarioView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        DatePicker datePicker = new DatePicker();
        TimePicker timePicker = new TimePicker();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Personal Information");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        datePicker.setLabel("Dia");
        timePicker.setLabel("Hora");
        timePicker.setWidth("min-content");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(datePicker);
        formLayout2Col.add(timePicker);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
    }
}
