package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.regex.Pattern;

@FacesValidator("NameValidator")
public class NameValidator implements Validator {

  private final String pat = "([a-z]|[A-Z]-[a-z]|[A-Z])*";

  @Override
  public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {

    Pattern pattern = Pattern.compile(pat);

    if (!pattern.matcher(o.toString()).matches() || o.toString().length() > 100) {
      FacesMessage msg =
          new FacesMessage("Name validation failed.",
              "Invalid name format.");
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(msg);
    }

  }
}
