package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator("TitleValidator")
public class TitleValidator implements Validator {

  private final String pat = "([a-z]|[A-Z]|\\s)*";

  @Override
  public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {

    Pattern pattern = Pattern.compile(pat);

    if (!pattern.matcher(o.toString()).matches() || o.toString().length() > 150) {
      System.out.println("titre invalide");
      FacesMessage msg =
          new FacesMessage("Title validation failed.",
              "Invalid title format.");
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(msg);
    }

  }
}
