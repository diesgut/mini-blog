package com.mitocode.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mitocode.model.Usuario;
import com.mitocode.service.IUsuarioService;

@Named
@ViewScoped
public class IndexBean implements Serializable {

	private Usuario us;
	@Inject
	private IUsuarioService service;
	
	@PostConstruct
	public void init() {
		this.us = new Usuario();
	}

	public String login() {
		String redireccion = "";

		try {
			Usuario usuarioDB = service.login(us);
			if(usuarioDB.getId() != null && usuarioDB.getEstado().equalsIgnoreCase("A")) {
				//Almacenar en la sesion de JSF y proseguir con la navegacion
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuarioDB);
				redireccion = "/protegido/roles?faces-redirect=true";
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Credenciales incorrectas"));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", e.getMessage()));
		}

		return redireccion;

	}

	public Usuario getUs() {
		return us;
	}

	public void setUs(Usuario us) {
		this.us = us;
	}

}
