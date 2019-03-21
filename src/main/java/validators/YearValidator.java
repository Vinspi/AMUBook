package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalDate;

@FacesValidator("YearValidator")
public class YearValidator implements Validator {


  @Override
  public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {

    int year;

    System.out.println("[VALIDATOR] : checking date");

    try {
      year = (int) o;

      FacesMessage msg = new FacesMessage("Input not valid", "Year format is not valid");



      if (year > LocalDate.now().getYear()) {
        System.out.println("la date est trop grande");
        throw new ValidatorException(msg);
      }

    }
    catch (ClassCastException e) {

      System.out.println("class cast exception !");

      FacesMessage msg = new FacesMessage("Input not valid", "Year format is not valid");


      throw new ValidatorException(msg);
    }

  }
}
