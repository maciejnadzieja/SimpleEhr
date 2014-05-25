package controllers;

import models.Patient;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import repositories.PatientQuery;
import repositories.PatientRepository;
import repositories.QueryResult;
import views.html.patient.createForm;
import views.html.patient.list;
import views.html.patient.show;

import static play.data.Form.form;

@Security.Authenticated(Secured.class)
public class PatientsController extends Controller {

    public static final int LIST_PAGE_SIZE = 5;
    private static PatientRepository patientRepository = new PatientRepository();

    public static Result list() {
        int page = request().getQueryString("page")==null?1:Integer.valueOf(request().getQueryString("page"));
        if (page < 1) {
            page = 1;
        }
        PatientQuery query = new PatientQuery(LIST_PAGE_SIZE, (page-1)*LIST_PAGE_SIZE, request().getQueryString("query"));
        if (query.getQuery() != null && query.getQuery().matches(".*\\(\\d+\\).*")) {
            QueryResult<Patient> result = patientRepository.find(query);
            if (result.getTotalCount() == 1) {
                return redirect(routes.PatientsController.show(result.getItems().iterator().next().id));
            }
        }
        QueryResult result = patientRepository.find(query);
        int maxPage = result.getTotalCount()/LIST_PAGE_SIZE + (result.getTotalCount()%LIST_PAGE_SIZE>0?1:0);
        return ok(list.render(result, query.getQuery(), page, maxPage));
    }

    public static Result listJson() {
        PatientQuery query = new PatientQuery(10, 0, request().getQueryString("query"));
        return ok(Json.toJson(patientRepository.find(query).getItems()));
    }

    public static Result createForm() {
        return ok(createForm.render(form(Patient.class)));
    }

    public static Result create() {
        Form<Patient> patientForm = form(Patient.class).bindFromRequest();

        if (patientForm.hasErrors()) {
            return badRequest(createForm.render(patientForm));
        }

        Patient newPatient = patientForm.get();
        patientRepository.add(newPatient);
        return redirect(routes.PatientsController.list());
    }

    public static Result show(String patientId) {
        return ok(show.render(patientRepository.findById(patientId)));
    }
}
