package com.mitocode.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.mindrot.jbcrypt.BCrypt;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.mitocode.model.Persona;
import com.mitocode.model.Usuario;
import com.mitocode.service.IPersonaService;
import com.mitocode.service.IUsuarioService;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

	@Inject
	private IUsuarioService service;

	private String usuarioStr;
	private Usuario usuario;
	private List<Usuario> lista;
	private String tipoDialog;
	private boolean claveVerificada;
	private String password1;
	private String password2;

	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
		this.listar();
		this.tipoDialog = "Nuevo";
	}

	public void operar(String accion) {
		try {

//			if (accion.equalsIgnoreCase("R")) {
//				this.service.registrar(this.persona);
//			} else if (accion.equalsIgnoreCase("M")) {
//				this.service.modificar(this.persona);
//			}
			this.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listar() {
		try {
			this.lista = this.service.listarUsuariosByname(usuarioStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mostrarData(Usuario p) {
		this.claveVerificada = false;
		this.usuario = p.clone();
		this.tipoDialog = "Modificar";
	}

	public void verificarPassword() {
		String controlId = "frmUsuario:txtCurPassword";
		try {
			Usuario usuario = service.login(this.usuario);
			if (usuario.getId() != null && usuario.getEstado().equalsIgnoreCase("A")) {
				FacesContext.getCurrentInstance().addMessage(controlId,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Clave validada correctamente"));
				this.claveVerificada = true;
			} else {
				FacesContext.getCurrentInstance().addMessage(controlId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso", "Credenciales incorrectas"));
			}
		} catch (Exception e) {
			this.claveVerificada = false;
			FacesContext.getCurrentInstance().addMessage(controlId,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
		}

	}

	public void changePassword() {
		try {
			String clave = this.password1;
			String claveHash = BCrypt.hashpw(clave, BCrypt.gensalt());
			Usuario usuario = this.usuario.clone();
			usuario.setContrasena(claveHash);
			this.service.changeUserPassword(usuario);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Clave cambiada correctamente"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", e.getMessage()));
		}
	}

//	
//	public void limpiarControles() {
//		this.persona = new Persona();
//		this.tipoDialog = "Nuevo";
//	}

	public String getTipoDialog() {
		return tipoDialog;
	}

	public void setTipoDialog(String tipoDialog) {
		this.tipoDialog = tipoDialog;
	}

	public String getUsuarioStr() {
		return usuarioStr;
	}

	public void setUsuarioStr(String usuarioStr) {
		this.usuarioStr = usuarioStr;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getLista() {
		return lista;
	}

	public void setLista(List<Usuario> lista) {
		this.lista = lista;
	}

	public boolean isClaveVerificada() {
		return claveVerificada;
	}

	public void setClaveVerificada(boolean claveVerificada) {
		this.claveVerificada = claveVerificada;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

}
