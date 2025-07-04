package com.educontrol.application.views;

import com.educontrol.application.views.modulodeestudiante.ModulodeEstudianteView;
import com.educontrol.application.views.moduloasignatura.ModuloAsignaturaView;
import com.educontrol.application.views.moduloPeriodoAcadémico.ModuloPeriodoAcadémicoView;
import com.educontrol.application.views.modulodeprofesor.ModulodeProfesorView;
import com.educontrol.application.views.modulogrupo.ModuloGrupoView;
import com.educontrol.application.views.modulohorario.ModuloHorarioView;
import com.educontrol.application.views.modulodeasistencia.ModulodeAsistenciaView;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


/**
 * The main view is a top-level placeholder for other views.
 */
/**@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {**/

@Route("")
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu principal");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        Span appName = new Span("EduControl");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.addItem
        (new SideNavItem("Modulo Estudiantes", ModulodeEstudianteView.class, LineAwesomeIcon.USER.create()));
        nav.addItem
        (new SideNavItem("Modulo Profesores", ModulodeProfesorView.class, LineAwesomeIcon.CHALKBOARD_TEACHER_SOLID.create()));
        nav.addItem
        (new SideNavItem("Modulo Periodos", ModuloPeriodoAcadémicoView.class, LineAwesomeIcon.CALENDAR_SOLID.create()));
        nav.addItem
        (new SideNavItem("Modulo Asignaturas", ModuloAsignaturaView.class, LineAwesomeIcon.BOOK_SOLID.create()));
        nav.addItem
        (new SideNavItem("Modulo Horarios", ModuloHorarioView.class, LineAwesomeIcon.CLOCK_SOLID.create()));
        nav.addItem
        (new SideNavItem("Modulo Grupos", ModuloGrupoView.class, LineAwesomeIcon.USER.create()));
        nav.addItem
        (new SideNavItem("Modulo Asistencias", ModulodeAsistenciaView.class, LineAwesomeIcon.CALENDAR_TIMES.create()));
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class); 
        return title == null ? "" : title.value(); 
    }
}
