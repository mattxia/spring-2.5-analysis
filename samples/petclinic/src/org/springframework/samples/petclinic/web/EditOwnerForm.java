package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.validation.OwnerValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * JavaBean Form controller that is used to edit an existing <code>Owner</code>.
 *
 * @author Juergen Hoeller
 * @author Ken Krebs
 */
@Controller
@RequestMapping("/editOwner.do")
@SessionAttributes(types = Owner.class)
public class EditOwnerForm {

	private final Clinic clinic;

	@Autowired
	public EditOwnerForm(Clinic clinic) {
		this.clinic = clinic;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(@RequestParam("ownerId") int ownerId, ModelMap model) {
		Owner owner = this.clinic.loadOwner(ownerId);
		model.addAttribute(owner);
		return "ownerForm";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute Owner owner, BindingResult result, SessionStatus status) {
		new OwnerValidator().validate(owner, result);
		if (result.hasErrors()) {
			return "ownerForm";
		}
		else {
			this.clinic.storeOwner(owner);
			status.setComplete();
			return "redirect:owner.do?ownerId=" + owner.getId();
		}
	}

}
